package com.shopsphere.web.controller;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.User;
import com.shopsphere.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for Customer Profile and Security Settings.
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile", "/profile/update", "/profile/change-password"})
public class ProfileServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProfileServlet.class.getName());

    private UserService userService;
    private AddressDAO addressDAO;

    @Override
    public void init() {
        this.userService = new UserService();
        this.addressDAO = new AddressDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        try {
            // Refresh user from DB
            user = userService.getUserById(user.getUserId()).orElse(user);
            session.setAttribute("user", user);

            List<Address> addresses = addressDAO.findByUserId(user.getUserId());
            req.setAttribute("addresses", addresses);
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading profile for user #" + user.getUserId(), e);
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();

        if ("/profile/change-password".equals(servletPath)) {
            handleChangePassword(req, resp);
        } else {
            handleUpdateProfile(req, resp);
        }
    }

    private void handleUpdateProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String name = req.getParameter("name");
        String mobile = req.getParameter("mobile");

        try {
            userService.updateProfile(user.getUserId(), name, mobile);
            user.setName(name);
            user.setMobile(mobile);
            session.setAttribute("user", user);
            session.setAttribute("flashSuccess", "Profile details updated successfully.");
        } catch (Exception e) {
            session.setAttribute("flashError", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/profile");
    }

    private void handleChangePassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            session.setAttribute("flashError", "New passwords do not match.");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        try {
            boolean changed = userService.changePassword(user.getUserId(), currentPassword, newPassword);
            if (changed) {
                session.setAttribute("flashSuccess", "Password changed successfully.");
            } else {
                session.setAttribute("flashError", "Failed to change password.");
            }
        } catch (Exception e) {
            session.setAttribute("flashError", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}
