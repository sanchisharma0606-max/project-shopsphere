package com.shopsphere.web.controller;

import com.shopsphere.model.User;
import com.shopsphere.model.WishlistItem;
import com.shopsphere.service.WishlistService;
import com.shopsphere.util.ValidationUtil;
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
 * Controller for Customer Wishlist operations.
 */
@WebServlet(name = "WishlistServlet", urlPatterns = {"/wishlist", "/wishlist/add", "/wishlist/remove", "/wishlist/move-to-cart"})
public class WishlistServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(WishlistServlet.class.getName());

    private WishlistService wishlistService;

    @Override
    public void init() {
        this.wishlistService = new WishlistService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        if ("/wishlist/remove".equals(servletPath)) {
            handleRemove(req, resp);
            return;
        }

        handleViewWishlist(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        switch (servletPath) {
            case "/wishlist/add" -> handleAdd(req, resp);
            case "/wishlist/remove" -> handleRemove(req, resp);
            case "/wishlist/move-to-cart" -> handleMoveToCart(req, resp);
            default -> handleViewWishlist(req, resp);
        }
    }

    private void handleViewWishlist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        try {
            List<WishlistItem> wishlist = wishlistService.getWishlist(user.getUserId());
            req.setAttribute("wishlist", wishlist);
            req.getRequestDispatcher("/wishlist.jsp").forward(req, resp);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading wishlist", e);
            req.setAttribute("errorMessage", "Error loading your saved items.");
            req.getRequestDispatcher("/wishlist.jsp").forward(req, resp);
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        int productId = ValidationUtil.parsePositiveInt(req.getParameter("productId"), -1);

        try {
            wishlistService.addToWishlist(user.getUserId(), productId);
            session.setAttribute("flashSuccess", "Saved to your wishlist!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving to wishlist", e);
            session.setAttribute("flashError", "Failed to add item to wishlist.");
        }

        String referer = req.getHeader("Referer");
        resp.sendRedirect(referer != null ? referer : req.getContextPath() + "/wishlist");
    }

    private void handleRemove(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        int productId = ValidationUtil.parsePositiveInt(req.getParameter("productId"), -1);

        try {
            wishlistService.removeFromWishlist(user.getUserId(), productId);
            session.setAttribute("flashSuccess", "Item removed from wishlist.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing from wishlist", e);
            session.setAttribute("flashError", "Failed to remove item.");
        }

        resp.sendRedirect(req.getContextPath() + "/wishlist");
    }

    private void handleMoveToCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        int productId = ValidationUtil.parsePositiveInt(req.getParameter("productId"), -1);

        try {
            wishlistService.moveToCart(user.getUserId(), productId);
            session.setAttribute("flashSuccess", "Moved item to your shopping cart!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error moving from wishlist to cart", e);
            session.setAttribute("flashError", "Failed to move item to cart.");
        }

        resp.sendRedirect(req.getContextPath() + "/cart");
    }
}
