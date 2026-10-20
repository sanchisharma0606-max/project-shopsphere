package com.shopsphere.web.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.ProductService;
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
 * Controller for submitting customer product reviews and ratings.
 */
@WebServlet(name = "ReviewServlet", urlPatterns = {"/review/add"})
public class ReviewServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ReviewServlet.class.getName());

    private ProductService productService;

    @Override
    public void init() {
        this.productService = new ProductService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        int productId = ValidationUtil.parsePositiveInt(req.getParameter("productId"), -1);
        int rating = ValidationUtil.parsePositiveInt(req.getParameter("rating"), 5);
        String reviewText = req.getParameter("reviewText");

        try {
            productService.addReview(user.getUserId(), productId, rating, reviewText);
            session.setAttribute("flashSuccess", "Thank you! Your review has been published.");
        } catch (IllegalArgumentException e) {
            session.setAttribute("flashError", e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving review", e);
            session.setAttribute("flashError", "Failed to submit review.");
        }

        resp.sendRedirect(req.getContextPath() + "/product-details?id=" + productId);
    }
}
