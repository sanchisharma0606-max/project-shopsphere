package com.shopsphere.web.controller;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.CouponService;
import com.shopsphere.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for Shopping Cart management (add, update, remove, coupon calculation).
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart", "/cart/add", "/cart/update", "/cart/remove", "/cart/apply-coupon", "/cart/remove-coupon"})
public class CartServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CartServlet.class.getName());

    private CartService cartService;
    private CouponService couponService;

    @Override
    public void init() {
        this.cartService = new CartService();
        this.couponService = new CouponService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        if ("/cart/remove".equals(servletPath)) {
            handleRemoveFromCart(req, resp);
            return;
        }

        handleViewCart(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        switch (servletPath) {
            case "/cart/add" -> handleAddToCart(req, resp);
            case "/cart/update" -> handleUpdateQuantity(req, resp);
            case "/cart/remove" -> handleRemoveFromCart(req, resp);
            case "/cart/apply-coupon" -> handleApplyCoupon(req, resp);
            case "/cart/remove-coupon" -> handleRemoveCoupon(req, resp);
            default -> handleViewCart(req, resp);
        }
    }

    private void handleViewCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        try {
            List<CartItem> cartItems = cartService.getCart(user.getUserId());
            BigDecimal subtotal = cartService.calculateCartTotal(cartItems);

            // Update cached count
            int count = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
            session.setAttribute("cartCount", count);

            // Check applied coupon from session
            String couponCode = (String) session.getAttribute("appliedCouponCode");
            BigDecimal couponDiscount = BigDecimal.ZERO;
            Coupon coupon = null;

            if (couponCode != null && !couponCode.isEmpty()) {
                try {
                    Optional<Coupon> cOpt = couponService.validateCoupon(couponCode, subtotal, user.getUserId());
                    if (cOpt.isPresent()) {
                        coupon = cOpt.get();
                        couponDiscount = coupon.calculateDiscount(subtotal);
                    } else {
                        session.removeAttribute("appliedCouponCode");
                    }
                } catch (Exception e) {
                    session.removeAttribute("appliedCouponCode");
                    req.setAttribute("couponError", e.getMessage());
                }
            }

            BigDecimal grandTotal = subtotal.subtract(couponDiscount);
            if (grandTotal.compareTo(BigDecimal.ZERO) < 0) {
                grandTotal = BigDecimal.ZERO;
            }

            req.setAttribute("cartItems", cartItems);
            req.setAttribute("subtotal", subtotal);
            req.setAttribute("coupon", coupon);
            req.setAttribute("couponDiscount", couponDiscount);
            req.setAttribute("grandTotal", grandTotal);

            req.getRequestDispatcher("/cart.jsp").forward(req, resp);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading cart", e);
            req.setAttribute("errorMessage", "Error loading your shopping cart.");
            req.getRequestDispatcher("/cart.jsp").forward(req, resp);
        }
    }

    private void handleAddToCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        int productId = ValidationUtil.parsePositiveInt(req.getParameter("productId"), -1);
        int quantity = ValidationUtil.parsePositiveInt(req.getParameter("quantity"), 1);

        try {
            cartService.addToCart(user.getUserId(), productId, quantity);
            session.setAttribute("cartCount", cartService.getCartItemCount(user.getUserId()));
            session.setAttribute("flashSuccess", "Item added to cart!");
        } catch (IllegalArgumentException e) {
            session.setAttribute("flashError", e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error adding to cart", e);
            session.setAttribute("flashError", "Failed to add product to cart.");
        }

        String referer = req.getHeader("Referer");
        if (referer != null && !referer.contains("/cart")) {
            resp.sendRedirect(referer);
        } else {
            resp.sendRedirect(req.getContextPath() + "/cart");
        }
    }

    private void handleUpdateQuantity(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        int productId = ValidationUtil.parsePositiveInt(req.getParameter("productId"), -1);
        int quantity = ValidationUtil.parsePositiveInt(req.getParameter("quantity"), 1);

        try {
            cartService.updateQuantity(user.getUserId(), productId, quantity);
            session.setAttribute("cartCount", cartService.getCartItemCount(user.getUserId()));
        } catch (IllegalArgumentException e) {
            session.setAttribute("flashError", e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating cart quantity", e);
            session.setAttribute("flashError", "Unable to update cart quantity.");
        }

        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void handleRemoveFromCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        int productId = ValidationUtil.parsePositiveInt(req.getParameter("productId"), -1);

        try {
            cartService.removeFromCart(user.getUserId(), productId);
            session.setAttribute("cartCount", cartService.getCartItemCount(user.getUserId()));
            session.setAttribute("flashSuccess", "Product removed from cart.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing item from cart", e);
            session.setAttribute("flashError", "Unable to remove item from cart.");
        }

        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void handleApplyCoupon(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String code = req.getParameter("couponCode");

        try {
            List<CartItem> cartItems = cartService.getCart(user.getUserId());
            BigDecimal subtotal = cartService.calculateCartTotal(cartItems);

            Optional<Coupon> couponOpt = couponService.validateCoupon(code, subtotal, user.getUserId());
            if (couponOpt.isPresent()) {
                session.setAttribute("appliedCouponCode", couponOpt.get().getCode());
                session.setAttribute("flashSuccess", "Coupon '" + couponOpt.get().getCode() + "' applied successfully!");
            }
        } catch (IllegalArgumentException e) {
            session.setAttribute("flashError", e.getMessage());
        } catch (SQLException e) {
            session.setAttribute("flashError", "Database error validating coupon.");
        }

        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void handleRemoveCoupon(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        session.removeAttribute("appliedCouponCode");
        session.setAttribute("flashSuccess", "Coupon removed.");
        resp.sendRedirect(req.getContextPath() + "/cart");
    }
}
