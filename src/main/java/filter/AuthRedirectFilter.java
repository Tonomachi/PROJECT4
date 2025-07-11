// src/Controll/AuthRedirectFilter.java
package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter({ "/thong_tin_nguoi_dung.jsp",          // thêm mọi trang bảo vệ
             "/gio_hang.jsp",
             "/nguoi_dung_sua.jsp" })
public class AuthRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession ss   = req.getSession(false);
        boolean loggedIn = (ss != null && ss.getAttribute("loggedInUser") != null);

        if (!loggedIn) {
            resp.sendRedirect(req.getContextPath() + "/SanPhamServlet");
            return;                 // dừng filter – không vào trang đích
        }

        chain.doFilter(request, response);   // tiếp tục nếu đã đăng nhập
    }
}
