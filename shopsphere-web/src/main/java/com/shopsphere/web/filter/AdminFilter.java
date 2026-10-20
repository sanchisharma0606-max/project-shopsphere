package com.shopsphere.web.filter;

import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter strictly protecting all /admin/* routes.
 * Enforces server-side role validation (User.Role.ADMIN).
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin", "/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=admin_required");
            return;
        }

        if (user.getRole() != User.Role.ADMIN) {
            // User is logged in but lacks ADMIN privileges
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Administrator privileges required.");
            return;
        }

        chain.doFilter(request, response);
    }
}
