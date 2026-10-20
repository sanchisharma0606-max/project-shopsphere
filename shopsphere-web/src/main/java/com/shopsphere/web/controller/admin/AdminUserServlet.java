package com.shopsphere.web.controller.admin;

import com.shopsphere.model.User;
import com.shopsphere.service.UserService;
import com.shopsphere.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for Administrator Customer Management.
 * Password hashes are strictly omitted from UI presentation.
 */
@WebServlet(name = "AdminUserServlet", urlPatterns = {"/admin/users", "/admin/users/status"})
public class AdminUserServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminUserServlet.class.getName());

    private UserService userService;

    @Override
    public void init() {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search = req.getParameter("q");

        try {
            List<User> users = userService.searchUsers(search);
            req.setAttribute("users", users);
            req.setAttribute("searchQuery", search);
            req.getRequestDispatcher("/admin/users.jsp").forward(req, resp);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading users list", e);
            req.setAttribute("errorMessage", "Error loading customer list.");
            req.getRequestDispatcher("/admin/users.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int userId = ValidationUtil.parsePositiveInt(req.getParameter("userId"), -1);
        String statusParam = req.getParameter("status");

        try {
            User.Status newStatus = User.Status.valueOf(statusParam);
            userService.toggleUserStatus(userId, newStatus);
            req.getSession().setAttribute("flashSuccess", "User #" + userId + " status updated to " + newStatus);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error changing status for user #" + userId, e);
            req.getSession().setAttribute("flashError", "Failed to update user status.");
        }

        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}
