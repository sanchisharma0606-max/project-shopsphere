package com.shopsphere.web.filter;

import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

/**
 * Filter protecting customer routes that require an authenticated session.
 * Preserves the original destination URL to redirect users back after successful login.
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {
        "/cart", "/cart/*",
        "/checkout", "/checkout/*",
        "/orders", "/order-details", "/order/*",
        "/profile", "/profile/*",
        "/wishlist", "/wishlist/*",
        "/addresses", "/addresses/*",
        "/review/*"
})
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Save requested destination for post-login return
            String requestURI = req.getRequestURI();
            String queryString = req.getQueryString();
            String fullTarget = (queryString != null && !queryString.isEmpty())
                    ? requestURI + "?" + queryString
                    : requestURI;

            HttpSession currentSession = req.getSession(true);
            currentSession.setAttribute("redirectAfterLogin", fullTarget);

            res.sendRedirect(req.getContextPath() + "/login?error=auth_required");
            return;
        }

        chain.doFilter(request, response);
    }
}
