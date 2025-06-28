package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class AuthFilter implements Filter {

    private static final Set<String> PUBLIC_URLS = Set.of(
            "/login.jsp", "/LoginServlet",
            "/register.jsp", "/RegisterServlet",
            "/error.jsp", "/index.jsp"
    );

    private static final Set<String> PUBLIC_RESOURCE_PATHS = Set.of(
            "/css/", "/js/", "/images/", "/uploads/"
    );

    private static final Set<String> ADMIN_URLS = Set.of(
            "/danh_sach_san_pham.jsp", "/FormCN.jsp", "/SanPhamServlet",
            "/danh_sach_nguoi_dung.jsp", "/UserManagementServlet", "/edit_user_role.jsp"
    );

    @Override
    public void init(FilterConfig filterConfig) { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String path = req.getRequestURI().substring(contextPath.length());

        HttpSession session = req.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("loggedInUser") != null;
        String userRole = session != null ? (String) session.getAttribute("userRole") : null;

        if (isPublicResource(path) || PUBLIC_URLS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        if (ADMIN_URLS.contains(path)) {
            if (loggedIn && "admin".equalsIgnoreCase(userRole)) {
                chain.doFilter(request, response);
            } else {
                redirectToLogin(resp, contextPath, "Bạn cần đăng nhập với tài khoản quản trị để truy cập chức năng này.");
            }
            return;
        }

        if (!loggedIn) {
            redirectToLogin(resp, contextPath, "Bạn cần đăng nhập để truy cập trang này.");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicResource(String path) {
        return PUBLIC_RESOURCE_PATHS.stream().anyMatch(path::startsWith);
    }

    private void redirectToLogin(HttpServletResponse resp, String contextPath, String message) throws IOException {
        String encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8);
        resp.sendRedirect(contextPath + "/login.jsp?errorMessage=" + encodedMsg);
    }

    @Override
    public void destroy() { }
}
