package com.shopsphere.web.controller;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.User;
import com.shopsphere.util.ValidationUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for Managing Customer Delivery Addresses.
 */
@WebServlet(name = "AddressServlet", urlPatterns = {"/addresses/add", "/addresses/edit", "/addresses/delete"})
public class AddressServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AddressServlet.class.getName());

    private AddressDAO addressDAO;

    @Override
    public void init() {
        this.addressDAO = new AddressDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();

        switch (servletPath) {
            case "/addresses/add" -> handleAddAddress(req, resp);
            case "/addresses/edit" -> handleEditAddress(req, resp);
            case "/addresses/delete" -> handleDeleteAddress(req, resp);
            default -> resp.sendRedirect(req.getContextPath() + "/profile");
        }
    }

    private void handleAddAddress(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String line = req.getParameter("addressLine");
        String city = req.getParameter("city");
        String state = req.getParameter("state");
        String pincode = req.getParameter("pincode");
        String typeParam = req.getParameter("addressType");

        if (!ValidationUtil.isNotEmpty(line) || !ValidationUtil.isNotEmpty(city) ||
                !ValidationUtil.isNotEmpty(state) || !ValidationUtil.isValidPincode(pincode)) {
            session.setAttribute("flashError", "Please fill in all address fields with a valid 6-digit pincode.");
        } else {
            try {
                Address.AddressType type = Address.AddressType.HOME;
                try {
                    type = Address.AddressType.valueOf(typeParam);
                } catch (Exception ignored) {}

                Address address = new Address(user.getUserId(), line.trim(), city.trim(), state.trim(), pincode.trim(), type);
                addressDAO.createAddress(address);
                session.setAttribute("flashSuccess", "Address added successfully.");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error adding address", e);
                session.setAttribute("flashError", "Failed to add address.");
            }
        }

        String redirect = req.getParameter("redirect");
        if ("checkout".equals(redirect)) {
            resp.sendRedirect(req.getContextPath() + "/checkout");
        } else {
            resp.sendRedirect(req.getContextPath() + "/profile");
        }
    }

    private void handleEditAddress(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        int addressId = ValidationUtil.parsePositiveInt(req.getParameter("addressId"), -1);
        String line = req.getParameter("addressLine");
        String city = req.getParameter("city");
        String state = req.getParameter("state");
        String pincode = req.getParameter("pincode");
        String typeParam = req.getParameter("addressType");

        try {
            Address.AddressType type = Address.AddressType.HOME;
            try {
                type = Address.AddressType.valueOf(typeParam);
            } catch (Exception ignored) {}

            Address address = new Address(addressId, user.getUserId(), line.trim(), city.trim(), state.trim(), pincode.trim(), type, null);
            addressDAO.updateAddress(address);
            session.setAttribute("flashSuccess", "Address updated.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating address", e);
            session.setAttribute("flashError", "Failed to update address.");
        }

        resp.sendRedirect(req.getContextPath() + "/profile");
    }

    private void handleDeleteAddress(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        int addressId = ValidationUtil.parsePositiveInt(req.getParameter("addressId"), -1);

        try {
            addressDAO.deleteAddress(addressId, user.getUserId());
            session.setAttribute("flashSuccess", "Address removed.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting address", e);
            session.setAttribute("flashError", "Failed to delete address.");
        }

        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}
