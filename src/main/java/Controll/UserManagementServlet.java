package Controll;

import Data.NguoiDung;
import Model.Model_NguoiDung;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/UserManagementServlet")
public class UserManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private NguoiDung nguoiDungDao;

    @Override
    public void init() {
        nguoiDungDao = new NguoiDung();
    }

    /* ==========================  GET  ========================== */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        /* ─── Kiểm tra quyền admin ─── */
        HttpSession ss = req.getSession(false);
        Model.Model_NguoiDung admin = (ss != null) ? (Model_NguoiDung) ss.getAttribute("loggedInUser") : null;
        if (admin == null || !"admin".equalsIgnoreCase(admin.getVaiTro())) {
            String msg = URLEncoder.encode("Bạn không có quyền truy cập!", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/Login.jsp?errorMessage=" + msg);
            return;
        }

        String action = req.getParameter("action");
        try {
            if ("editUser".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Model_NguoiDung u = nguoiDungDao.getUserById(id);
                if (u == null) {
                    req.setAttribute("errorMessage", "Không tìm thấy người dùng!");
                    req.getRequestDispatcher("/error.jsp").forward(req, resp);
                    return;
                }
                req.setAttribute("editUser", u);
                req.getRequestDispatcher("/editUserInfo.jsp").forward(req, resp);
                return;
            }

            /* Mặc định: hiển thị danh sách */
            List<Model_NguoiDung> list = nguoiDungDao.getAllUsers();
            req.setAttribute("users", list);
            req.getRequestDispatcher("/danh_sach_nguoi_dung.jsp").forward(req, resp);
        } catch (Exception ex) {
            req.setAttribute("errorMessage", "Lỗi: " + ex.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    /* ==========================  POST  ========================= */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        /* ─── Kiểm tra quyền admin ─── */
        HttpSession ss = req.getSession(false);
        Model_NguoiDung admin = (ss != null) ? (Model_NguoiDung) ss.getAttribute("loggedInUser") : null;
        if (admin == null || !"admin".equalsIgnoreCase(admin.getVaiTro())) {
            String msg = URLEncoder.encode("Bạn không có quyền!", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/Login.jsp?errorMessage=" + msg);
            return;
        }

        String action = req.getParameter("action");
        String message = "";
        String redirectUrl = req.getContextPath() + "/UserManagementServlet?action=list"; // Mặc định về danh sách

        try {
            if ("updateUserInfo".equals(action)) {
                int maNguoiDung = Integer.parseInt(req.getParameter("maNguoiDung"));
                String hoTen = req.getParameter("hoTen");
                String email = req.getParameter("email");
                String soDienThoai = req.getParameter("soDienThoai");
                String diaChi = req.getParameter("diaChi");
                String vaiTro = req.getParameter("vaiTro");

                Model_NguoiDung existingUser = nguoiDungDao.getUserById(maNguoiDung);
                if (existingUser == null) {
                    throw new SQLException("Không tìm thấy người dùng có ID: " + maNguoiDung);
                }

                existingUser.setHoTen(hoTen);
                existingUser.setEmail(email);
                existingUser.setSoDienThoai(soDienThoai);
                existingUser.setDiaChi(diaChi);
                existingUser.setVaiTro(vaiTro);

                boolean ok = nguoiDungDao.updateAllUserInfo(existingUser);

                message = ok ? "Cập nhật thông tin người dùng thành công!" : "Cập nhật thông tin người dùng thất bại!";
                // redirectUrl đã được set mặc định ở trên, không cần thay đổi ở đây nữa
            } else {
                resp.sendRedirect(redirectUrl);
                return;
            }

            String param = URLEncoder.encode(message, StandardCharsets.UTF_8);
            resp.sendRedirect(redirectUrl + "&message=" + param); // Chuyển hướng về danh sách với thông báo

        } catch (Exception ex) {
            req.setAttribute("errorMessage", "Lỗi xử lý: " + ex.getMessage());
            // Nếu lỗi khi cập nhật, vẫn quay lại trang chỉnh sửa với thông báo lỗi
            try {
                int id = Integer.parseInt(req.getParameter("maNguoiDung"));
                Model_NguoiDung u = nguoiDungDao.getUserById(id);
                req.setAttribute("editUser", u);
                req.getRequestDispatcher("/editUserInfo.jsp").forward(req, resp);
            } catch (NumberFormatException | SQLException e) {
                 req.setAttribute("errorMessage", "Lỗi khi lấy lại thông tin người dùng sau khi cập nhật: " + e.getMessage());
                 req.getRequestDispatcher("/error.jsp").forward(req, resp);
            }
        }
    }
}