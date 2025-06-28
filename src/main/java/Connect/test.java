package Connect;

import Connect.ConnectSQL; 

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet("/TestConnection")
public class test extends HttpServlet {

   
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            try (Connection conn = ConnectSQL.getConnection()) {
                if (conn != null) {
                    out.println("✅ Kết nối thành công tới CSDL!");
                } else {
                    out.println("❌ Không thể kết nối đến cơ sở dữ liệu.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.println("❌ Lỗi kết nối: " + e.getMessage());
            }
        }
    }
}
