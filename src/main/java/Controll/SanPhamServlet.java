package Controll;

import Data.DanhMuc;
import Data.SanPham;
import Model.Model_DanhMuc;
import Model.Model_SanPham;
import Model.Model_NguoiDung;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet("/SanPhamServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10,      // 10MB
                 maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class SanPhamServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private SanPham sanPhamDAO;
    private DanhMuc danhMucDAO;
    private static final String UPLOAD_DIRECTORY = "images"; // Thư mục lưu ảnh trong webapp

    @Override
    public void init() throws ServletException {
        super.init();
        sanPhamDAO = new SanPham();
        danhMucDAO = new DanhMuc();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Model_NguoiDung loggedInUser = (session != null) ? (Model_NguoiDung) session.getAttribute("loggedInUser") : null;
        String userRole = (loggedInUser != null && loggedInUser.getVaiTro() != null) ? loggedInUser.getVaiTro().trim() : null;

        // Kiểm tra quyền truy cập ADMIN
        if (loggedInUser == null || !"admin".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp?errorMessage=" + URLEncoder.encode("Bạn không có quyền truy cập trang quản lý sản phẩm.", StandardCharsets.UTF_8.toString()));
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showForm(request, response, "new");
                    break;
                case "delete":
                    deleteSanPham(request, response);
                    break;
                case "edit":
                    showForm(request, response, "edit");
                    break;
                case "list":
                default:
                    listSanPhams(request, response);
                    break;
            }
        } catch (Exception ex) { // Bắt tất cả các Exception để xử lý tập trung
            ex.printStackTrace(); // In lỗi ra console server để debug
            String userFriendlyMessage = "Đã xảy ra lỗi: " + ex.getMessage();
            if (ex instanceof SQLException) {
                userFriendlyMessage = "Lỗi cơ sở dữ liệu: " + ex.getMessage();
            } else if (ex instanceof NumberFormatException) {
                userFriendlyMessage = "Dữ liệu nhập vào không hợp lệ (ID sản phẩm không đúng định dạng).";
            }
            request.setAttribute("errorMessage", userFriendlyMessage);
            request.getRequestDispatcher("/error.jsp").forward(request, response); // Hoặc chuyển hướng đến trang lỗi tùy chỉnh
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Model_NguoiDung loggedInUser = (session != null) ? (Model_NguoiDung) session.getAttribute("loggedInUser") : null;
        String userRole = (loggedInUser != null && loggedInUser.getVaiTro() != null) ? loggedInUser.getVaiTro().trim() : null;

        // Kiểm tra quyền truy cập ADMIN
        if (loggedInUser == null || !"admin".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp?errorMessage=" + URLEncoder.encode("Bạn không có quyền thực hiện thao tác này.", StandardCharsets.UTF_8.toString()));
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "insert":
                    insertSanPham(request, response);
                    break;
                case "update":
                    updateSanPham(request, response);
                    break;
                default:
                    listSanPhams(request, response);
                    break;
            }
        } catch (Exception ex) { // Bắt tất cả các exception ở đây để xử lý chung
            ex.printStackTrace(); // In lỗi ra console server để debug
            String userFriendlyMessage = "Đã xảy ra lỗi khi thực hiện thao tác " + action + ": " + ex.getMessage();

            if (ex instanceof SQLException) {
                SQLException sqlEx = (SQLException) ex;
                // Kiểm tra lỗi Foreign Key Constraint
                if (sqlEx.getMessage() != null && sqlEx.getMessage().contains("foreign key constraint fails") && sqlEx.getMessage().contains("MaDanhMuc")) {
                    userFriendlyMessage = "Lỗi: Mã danh mục bạn chọn không tồn tại. Vui lòng chọn một danh mục hợp lệ.";
                } else {
                    userFriendlyMessage = "Lỗi cơ sở dữ liệu: " + sqlEx.getMessage();
                }
            } else if (ex instanceof NumberFormatException) {
                userFriendlyMessage = "Dữ liệu nhập vào không hợp lệ (giá, số lượng, mã danh mục). Vui lòng kiểm tra lại.";
            } else if (ex instanceof NullPointerException) { // Xử lý NPE có thể xảy ra khi parse param
                userFriendlyMessage = "Dữ liệu nhập vào bị thiếu hoặc không đúng định dạng. Vui lòng kiểm tra lại.";
            }

            request.setAttribute("errorMessage", userFriendlyMessage);

            // Cố gắng giữ lại dữ liệu form và chuyển hướng về FormCN.jsp
            Model_SanPham sanPhamToKeep = new Model_SanPham();
            try {
                String maSanPhamParam = request.getParameter("maSanPham");
                if (maSanPhamParam != null && !maSanPhamParam.isEmpty()) {
                    sanPhamToKeep.setMaSanPham(Integer.parseInt(maSanPhamParam));
                }
                sanPhamToKeep.setTenSanPham(request.getParameter("tenSanPham"));
                sanPhamToKeep.setMoTa(request.getParameter("moTa"));

                // Xử lý giá
                String giaParam = request.getParameter("gia");
                if (giaParam != null && !giaParam.isEmpty()) {
                    try {
                        // Loại bỏ dấu phẩy nếu người dùng nhập số có dấu phẩy (ví dụ: 10,000)
                        sanPhamToKeep.setGia(new BigDecimal(giaParam.replace(",", "")));
                    } catch (NumberFormatException nfe) {
                        sanPhamToKeep.setGia(BigDecimal.ZERO); // Gán 0 nếu lỗi
                    }
                }

                // Xử lý số lượng
                String soLuongParam = request.getParameter("soLuong");
                if (soLuongParam != null && !soLuongParam.isEmpty()) {
                     try {
                        sanPhamToKeep.setSoLuongTonKho(Integer.parseInt(soLuongParam));
                    } catch (NumberFormatException nfe) {
                        sanPhamToKeep.setSoLuongTonKho(0); // Gán 0 nếu lỗi
                    }
                }

                // Xử lý mã danh mục
                String maDanhMucParam = request.getParameter("maDanhMuc");
                if (maDanhMucParam != null && !maDanhMucParam.isEmpty()) {
                    try {
                        sanPhamToKeep.setMaDanhMuc(Integer.parseInt(maDanhMucParam));
                    } catch (NumberFormatException nfe) {
                        sanPhamToKeep.setMaDanhMuc(0); // Gán 0 nếu lỗi
                    }
                }
                // Giữ lại hinhAnhUrlCu để hiển thị lại nếu có lỗi
                sanPhamToKeep.setHinhAnh(request.getParameter("hinhAnhUrlCu")); // Đây là tên file ảnh hiện tại trong DB

            } catch (Exception e) {
                e.printStackTrace(); // In lỗi ra để debug việc tái tạo form
                request.setAttribute("errorMessage", request.getAttribute("errorMessage") + "<br>Lỗi nội bộ khi tải lại form: " + e.getMessage());
            }

            request.setAttribute("sanPham", sanPhamToKeep);

            try {
                request.setAttribute("danhMucList", danhMucDAO.getAllDanhMuc());
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", request.getAttribute("errorMessage") + "<br>Lỗi khi tải danh mục.");
            }

            request.getRequestDispatcher("/FormCN.jsp").forward(request, response);
        }
    }

    private void listSanPhams(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<Model_SanPham> listSanPhams = sanPhamDAO.getAllSanPhams();
        // Lấy danh sách danh mục để hiển thị các nút lọc trên JSP
        List<Model_DanhMuc> listDanhMuc = danhMucDAO.getAllDanhMuc(); // <--- DÒNG ĐÃ THÊM

        request.setAttribute("sanPhams", listSanPhams);
        request.setAttribute("danhMucList", listDanhMuc); // <--- DÒNG ĐÃ THÊM
        request.getRequestDispatcher("/danh_sach_san_pham.jsp").forward(request, response);
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, String formType) throws ServletException, IOException, SQLException {
        Model_SanPham sanPham = null;
        if ("edit".equals(formType)) {
            try {
                int maSanPham = Integer.parseInt(request.getParameter("id"));
                sanPham = sanPhamDAO.getSanPhamById(maSanPham); // Đảm bảo phương thức này trả về null nếu không tìm thấy
                if (sanPham == null) {
                    // Nếu không tìm thấy sản phẩm, chuyển hướng với thông báo lỗi
                    String encodedErrorMessage = URLEncoder.encode("Không tìm thấy sản phẩm cần chỉnh sửa (ID: " + maSanPham + ").", StandardCharsets.UTF_8.toString());
                    response.sendRedirect(request.getContextPath() + "/SanPhamServlet?action=list&errorMessage=" + encodedErrorMessage);
                    return;
                }
            } catch (NumberFormatException e) {
                // Nếu ID không phải là số hợp lệ
                String encodedErrorMessage = URLEncoder.encode("ID sản phẩm không hợp lệ: " + request.getParameter("id"), StandardCharsets.UTF_8.toString());
                response.sendRedirect(request.getContextPath() + "/SanPhamServlet?action=list&errorMessage=" + encodedErrorMessage);
                return;
            }
        }

        List<Model_DanhMuc> listDanhMuc = danhMucDAO.getAllDanhMuc();

        request.setAttribute("sanPham", sanPham); // sanPham sẽ là null nếu là form "new"
        request.setAttribute("danhMucList", listDanhMuc);
        request.getRequestDispatcher("/FormCN.jsp").forward(request, response);
    }

    /**
     * Xử lý tải lên file ảnh, lưu file mới và xóa file cũ nếu có.
     * @param request HttpServletRequest
     * @param partName Tên của phần file trong form (ví dụ: "hinhAnhFile")
     * @param currentImageUrl Tên file ảnh hiện tại trong DB (ví dụ: "abc.jpg"). Có thể là null hoặc rỗng nếu là thêm mới.
     * @return Tên file ảnh mới được lưu (luôn có đuôi .jpg) hoặc tên file ảnh cũ nếu không có file mới được tải lên.
     * @throws IOException
     * @throws ServletException
     */
    private String uploadFile(HttpServletRequest request, String partName, String currentImageUrl) throws IOException, ServletException {
        Part filePart = request.getPart(partName); // Lấy phần file từ request
        String newFileName = currentImageUrl; // Mặc định là giữ ảnh cũ

        // Kiểm tra xem có file mới được tải lên không
        if (filePart != null && filePart.getSize() > 0 && filePart.getSubmittedFileName() != null && !filePart.getSubmittedFileName().isEmpty()) {
            // Lấy tên file gốc từ client
            String originalFileNameWithExtension = getFileName(filePart);

            // Loại bỏ phần mở rộng cũ để chúng ta có thể gán .jpg một cách an toàn
            String baseFileNameWithoutExtension = "";
            int dotIndex = originalFileNameWithExtension.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < originalFileNameWithExtension.length() - 1) { // Đảm bảo có chấm và không phải ở cuối
                baseFileNameWithoutExtension = originalFileNameWithExtension.substring(0, dotIndex);
            } else {
                baseFileNameWithoutExtension = originalFileNameWithExtension; // Không có phần mở rộng
            }

            // Loại bỏ ký tự không hợp lệ hoặc đường dẫn từ tên file gốc (chỉ giữ lại phần tên)
            baseFileNameWithoutExtension = baseFileNameWithoutExtension.replaceAll("[^a-zA-Z0-9.\\-]", "_");

            // Tạo tên file duy nhất bằng UUID và **LUÔN ĐẢM BẢO ĐỊNH DẠNG .jpg**
            // Điều này giải quyết vấn đề .jfif của bạn
            newFileName = UUID.randomUUID().toString() + "_" + baseFileNameWithoutExtension + ".jpg"; // <--- Đã sửa ở đây

            // Đường dẫn tuyệt đối trên hệ thống server để lưu file
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir(); // Tạo thư mục nếu chưa tồn tại
            }

            // Xóa ảnh cũ nếu có và ảnh mới được tải lên
            // Tránh xóa "default.jpg" hoặc ảnh đang được sử dụng bởi các sản phẩm khác
            // Và tránh xóa ảnh mới nếu newFileName bằng currentImageUrl (khi upload lại cùng file)
            if (currentImageUrl != null && !currentImageUrl.isEmpty() && !"default.jpg".equalsIgnoreCase(currentImageUrl) && !currentImageUrl.equals(newFileName)) {
                String oldFilePath = uploadPath + File.separator + currentImageUrl;
                File oldFile = new File(oldFilePath);
                if (oldFile.exists() && oldFile.isFile()) {
                    try {
                        if (oldFile.delete()) {
                            System.out.println("Deleted old image: " + oldFilePath);
                        } else {
                            System.out.println("Failed to delete old image: " + oldFilePath + ". File might be in use or permissions issue.");
                        }
                    } catch (SecurityException e) {
                        System.err.println("Security Exception while deleting old image: " + e.getMessage());
                    }
                }
            }

            // Ghi file mới vào thư mục
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, new File(uploadDir, newFileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            return newFileName; // Trả về tên file mới
        }
        // Nếu không có file mới được tải lên, trả về tên file ảnh cũ
        return currentImageUrl;
    }

    private void insertSanPham(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        String tenSanPham = request.getParameter("tenSanPham");
        String moTa = request.getParameter("moTa");
        // Xử lý giá
        BigDecimal gia = null;
        try {
            String giaParam = request.getParameter("gia");
            if (giaParam != null && !giaParam.isEmpty()) {
                gia = new BigDecimal(giaParam.replace(",", "")); // Loại bỏ dấu phẩy
            }
        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu giá không phải số
            throw new IllegalArgumentException("Giá không hợp lệ.", e);
        }

        // Xử lý số lượng
        int soLuongTonKho = 0;
        try {
            String soLuongParam = request.getParameter("soLuong");
            if (soLuongParam != null && !soLuongParam.isEmpty()) {
                soLuongTonKho = Integer.parseInt(soLuongParam);
            }
        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu số lượng không phải số
            throw new IllegalArgumentException("Số lượng tồn kho không hợp lệ.", e);
        }

        // Xử lý mã danh mục
        int maDanhMuc = 0;
        try {
            String maDanhMucParam = request.getParameter("maDanhMuc");
            if (maDanhMucParam != null && !maDanhMucParam.isEmpty()) {
                maDanhMuc = Integer.parseInt(maDanhMucParam);
            }
        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu mã danh mục không phải số
            throw new IllegalArgumentException("Mã danh mục không hợp lệ.", e);
        }


        // Khi insert, currentImageUrl là rỗng ("") hoặc null
        String hinhAnh = uploadFile(request, "hinhAnhFile", null);
        if (hinhAnh == null || hinhAnh.isEmpty()) {
             hinhAnh = "default.jpg"; // Chỉ lưu tên file default vào DB
        }

        Model_SanPham newSanPham = new Model_SanPham(tenSanPham, moTa, gia, soLuongTonKho, hinhAnh, maDanhMuc);

        int newId = sanPhamDAO.insertSanPham(newSanPham);
        if (newId > 0) {
            // Mã hóa thông báo thành công
            String encodedMessage = URLEncoder.encode("Thêm sản phẩm thành công!", StandardCharsets.UTF_8.toString());
            response.sendRedirect(request.getContextPath() + "/SanPhamServlet?action=list&message=" + encodedMessage);
        } else {
            request.setAttribute("errorMessage", "Không thể thêm sản phẩm mới. Vui lòng thử lại.");
            request.setAttribute("sanPham", newSanPham);
            request.setAttribute("danhMucList", danhMucDAO.getAllDanhMuc());
            request.getRequestDispatcher("/FormCN.jsp").forward(request, response);
        }
    }

    private void updateSanPham(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int maSanPham = 0;
        try {
            maSanPham = Integer.parseInt(request.getParameter("maSanPham"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Mã sản phẩm không hợp lệ.", e);
        }

        String tenSanPham = request.getParameter("tenSanPham");
        String moTa = request.getParameter("moTa");
        // Xử lý giá
        BigDecimal gia = null;
        try {
            String giaParam = request.getParameter("gia");
            if (giaParam != null && !giaParam.isEmpty()) {
                gia = new BigDecimal(giaParam.replace(",", "")); // Loại bỏ dấu phẩy
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá không hợp lệ.", e);
        }

        // Xử lý số lượng
        int soLuongTonKho = 0;
        try {
            String soLuongParam = request.getParameter("soLuong");
            if (soLuongParam != null && !soLuongParam.isEmpty()) {
                soLuongTonKho = Integer.parseInt(soLuongParam);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số lượng tồn kho không hợp lệ.", e);
        }

        // Xử lý mã danh mục
        int maDanhMuc = 0;
        try {
            String maDanhMucParam = request.getParameter("maDanhMuc");
            if (maDanhMucParam != null && !maDanhMucParam.isEmpty()) {
                maDanhMuc = Integer.parseInt(maDanhMucParam);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Mã danh mục không hợp lệ.", e);
        }

        String hinhAnhCu = request.getParameter("hinhAnhUrlCu"); // Lấy tên file ảnh cũ từ hidden field

        // Gọi uploadFile, nó sẽ xử lý việc xóa ảnh cũ và trả về tên file mới hoặc tên file cũ
        // hinhAnhCu ở đây sẽ là tên file (vd: "old_image.jpg")
        String hinhAnhMoi = uploadFile(request, "hinhAnhFile", hinhAnhCu);

        // Đảm bảo hinhAnhMoi không bao giờ là null hoặc rỗng, gán default.jpg nếu không có ảnh nào
        if (hinhAnhMoi == null || hinhAnhMoi.isEmpty()) {
            hinhAnhMoi = "default.jpg";
        }


        Model_SanPham sanPham = new Model_SanPham(maSanPham, tenSanPham, moTa, gia, soLuongTonKho, hinhAnhMoi, maDanhMuc);

        boolean updated = sanPhamDAO.updateSanPham(sanPham);
        if (updated) {
            // Mã hóa thông báo thành công
            String encodedMessage = URLEncoder.encode("Cập nhật sản phẩm thành công!", StandardCharsets.UTF_8.toString());
            response.sendRedirect(request.getContextPath() + "/SanPhamServlet?action=list&message=" + encodedMessage);
        } else {
            request.setAttribute("errorMessage", "Không thể cập nhật sản phẩm. Vui lòng thử lại.");
            request.setAttribute("sanPham", sanPham);
            request.setAttribute("danhMucList", danhMucDAO.getAllDanhMuc());
            request.getRequestDispatcher("/FormCN.jsp").forward(request, response);
        }
    }

    private void deleteSanPham(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int maSanPham = 0;
        try {
            maSanPham = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            String encodedErrorMessage = URLEncoder.encode("ID sản phẩm không hợp lệ khi xóa.", StandardCharsets.UTF_8.toString());
            response.sendRedirect(request.getContextPath() + "/SanPhamServlet?action=list&errorMessage=" + encodedErrorMessage);
            return;
        }

        // Lấy thông tin sản phẩm để lấy tên ảnh và xóa ảnh vật lý
        Model_SanPham sanPhamToDelete = sanPhamDAO.getSanPhamById(maSanPham);
        String hinhAnhFileName = null;
        if (sanPhamToDelete != null) {
            hinhAnhFileName = sanPhamToDelete.getHinhAnh();
        }

        boolean deleted = sanPhamDAO.deleteSanPham(maSanPham);
        if (deleted) {
            // Xóa file ảnh vật lý sau khi xóa thành công trong DB
            if (hinhAnhFileName != null && !hinhAnhFileName.isEmpty() && !"default.jpg".equalsIgnoreCase(hinhAnhFileName)) {
                String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
                String filePathToDelete = uploadPath + File.separator + hinhAnhFileName;
                File fileToDelete = new File(filePathToDelete);
                if (fileToDelete.exists() && fileToDelete.isFile()) {
                    try {
                        if (fileToDelete.delete()) {
                            System.out.println("Deleted image file: " + filePathToDelete);
                        } else {
                            System.out.println("Failed to delete image file: " + filePathToDelete + ". File might be in use or permissions issue.");
                        }
                    } catch (SecurityException e) {
                        System.err.println("Security Exception while deleting image file: " + e.getMessage());
                    }
                }
            }
            // Mã hóa thông báo thành công
            String encodedMessage = URLEncoder.encode("Xóa sản phẩm thành công!", StandardCharsets.UTF_8.toString());
            response.sendRedirect(request.getContextPath() + "/SanPhamServlet?action=list&message=" + encodedMessage);
        } else {
            // Mã hóa thông báo lỗi
            String encodedErrorMessage = URLEncoder.encode("Không thể xóa sản phẩm. Vui lòng thử lại.", StandardCharsets.UTF_8.toString());
            response.sendRedirect(request.getContextPath() + "/SanPhamServlet?action=list&errorMessage=" + encodedErrorMessage);
        }
    }

    // Phương thức này đã được cải tiến để xử lý tên file an toàn hơn
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String s : contentDisp.split(";")) {
            if (s.trim().startsWith("filename")) {
                String fileName = s.substring(s.indexOf("=") + 1).trim().replace("\"", "");
                // Xử lý trường hợp đường dẫn file có thể là tuyệt đối trên client (ví dụ: C:\fakepath\image.jpg)
                // Chỉ lấy phần tên file cuối cùng
                return fileName.substring(fileName.lastIndexOf(File.separator) + 1);
            }
        }
        return null;
    }
}