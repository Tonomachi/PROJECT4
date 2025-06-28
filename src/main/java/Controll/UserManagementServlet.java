package Controll;

import Data.NguoiDung;
import Model.Model_NguoiDung;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/UserManagementServlet")
public class UserManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private NguoiDung nguoiDungDao;

    @Override
    public void init() throws ServletException {
        super.init();
        nguoiDungDao = new NguoiDung();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Model_NguoiDung loggedInUser = (session != null) ? (Model_NguoiDung) session.getAttribute("loggedInUser") : null;
        String userRole = (loggedInUser != null) ? loggedInUser.getVaiTro().trim() : null; // Đảm bảo trim()

        // PHÂN QUYỀN: CHỈ ADMIN MỚI CÓ THỂ TRUY CẬP
        if (loggedInUser == null || !"admin".equals(userRole)) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp?errorMessage=Bạn không có quyền truy cập trang quản lý người dùng.");
            return;
        }

        String action = request.getParameter("action");
        if ("editRole".equals(action)) {
            try {
                int maNguoiDung = Integer.parseInt(request.getParameter("id"));
                Model_NguoiDung userToEdit = nguoiDungDao.getUserById(maNguoiDung);

                if (userToEdit != null) {
                    request.setAttribute("userToEdit", userToEdit);
                    request.getRequestDispatcher("/editUserRole.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Không tìm thấy người dùng với ID: " + maNguoiDung);
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                }
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Lỗi khi lấy thông tin người dùng: " + e.getMessage());
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else if ("list".equals(action) || action == null) {
            try {
                List<Model_NguoiDung> users = nguoiDungDao.getAllUsers();
                request.setAttribute("users", users);
                request.getRequestDispatcher("/danh_sach_nguoi_dung.jsp").forward(request, response); // Đảm bảo đường dẫn đúng
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Lỗi khi tải danh sách người dùng: " + e.getMessage());
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/UserManagementServlet?action=list");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Model_NguoiDung loggedInUser = (session != null) ? (Model_NguoiDung) session.getAttribute("loggedInUser") : null;
        String userRole = (loggedInUser != null) ? loggedInUser.getVaiTro().trim() : null; // Đảm bảo trim()

        // PHÂN QUYỀN: CHỈ ADMIN MỚI CÓ THỂ THAY ĐỔI
        if (loggedInUser == null || !"admin".equals(userRole)) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp?errorMessage=Bạn không có quyền thực hiện thao tác này.");
            return;
        }

        String action = request.getParameter("action");
        if ("updateRole".equals(action)) {
            try {
                int maNguoiDung = Integer.parseInt(request.getParameter("maNguoiDung"));
                String vaiTroMoi = request.getParameter("vaiTro");

                Model_NguoiDung userToUpdate = nguoiDungDao.getUserById(maNguoiDung);
                if (userToUpdate != null) {
                    userToUpdate.setVaiTro(vaiTroMoi); // Set vai trò mới vào đối tượng
                    boolean updated = nguoiDungDao.updateUserRole(userToUpdate); // Gọi phương thức update vai trò

                    if (updated) {
                         response.sendRedirect(request.getContextPath() + "/UserManagementServlet?action=list&message=Cập nhật vai trò thành công.");
                    } else {
                         request.setAttribute("errorMessage", "Cập nhật vai trò thất bại.");
                         request.getRequestDispatcher("/error.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("errorMessage", "Không tìm thấy người dùng để cập nhật.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                }
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Lỗi khi cập nhật vai trò người dùng: " + e.getMessage());
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/UserManagementServlet?action=list");
        }
    }
}