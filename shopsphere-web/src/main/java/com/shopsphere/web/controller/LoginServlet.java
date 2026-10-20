package com.shopsphere.web.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for Customer and Administrator Authentication.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    private UserService userService;
    private CartService cartService;

    @Override
    public void init() {
        this.userService = new UserService();
        this.cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (user.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
            return;
        }

        String error = req.getParameter("error");
        if ("auth_required".equals(error)) {
            req.setAttribute("errorMessage", "Please log in to access this feature.");
        } else if ("admin_required".equals(error)) {
            req.setAttribute("errorMessage", "Administrator credentials required.");
        } else if ("invalid_credentials".equals(error)) {
            req.setAttribute("errorMessage", "Invalid email address or password.");
        }

        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            Optional<User> userOpt = userService.authenticate(email, password);

            if (userOpt.isPresent()) {
                User user = userOpt.get();

                // Create fresh session to avoid session fixation
                HttpSession session = req.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("role", user.getRole().name());

                // Cache cart item count in session for navbar badge
                try {
                    int cartCount = cartService.getCartItemCount(user.getUserId());
                    session.setAttribute("cartCount", cartCount);
                } catch (Exception ignored) {}

                LOGGER.info("User logged in successfully: " + user.getEmail() + " [Role: " + user.getRole() + "]");

                // Check for saved target URL after login
                String redirectTarget = (String) session.getAttribute("redirectAfterLogin");
                session.removeAttribute("redirectAfterLogin");

                if (redirectTarget != null && !redirectTarget.isEmpty()) {
                    resp.sendRedirect(redirectTarget);
                } else if (user.isAdmin()) {
                    resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/home");
                }
            } else {
                req.setAttribute("email", email);
                req.setAttribute("errorMessage", "Invalid email address or password.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during login", e);
            req.setAttribute("errorMessage", "Database connection error. Please verify database service.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
