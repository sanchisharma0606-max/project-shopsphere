package com.shopsphere.web.controller;

import com.shopsphere.model.Order;
import com.shopsphere.model.User;
import com.shopsphere.service.OrderService;
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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for customer order history, details timeline tracking, and cancellations.
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/orders", "/order-details", "/order/cancel"})
public class OrderServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(OrderServlet.class.getName());

    private OrderService orderService;

    @Override
    public void init() {
        this.orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        if ("/order-details".equals(servletPath)) {
            handleOrderDetails(req, resp);
        } else {
            handleOrderList(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        if ("/order/cancel".equals(servletPath)) {
            handleCancelOrder(req, resp);
        } else {
            doGet(req, resp);
        }
    }

    private void handleOrderList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        try {
            List<Order> orders = orderService.getCustomerOrders(user.getUserId());
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/orders.jsp").forward(req, resp);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading orders for user " + user.getUserId(), e);
            req.setAttribute("errorMessage", "Error loading order history.");
            req.getRequestDispatcher("/orders.jsp").forward(req, resp);
        }
    }

    private void handleOrderDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        int orderId = ValidationUtil.parsePositiveInt(req.getParameter("id"), -1);
        if (orderId <= 0) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        try {
            Optional<Order> orderOpt = orderService.getOrderDetails(orderId, user.getUserId(), user.isAdmin());
            if (orderOpt.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found or access denied.");
                return;
            }

            req.setAttribute("order", orderOpt.get());
            req.getRequestDispatcher("/order-details.jsp").forward(req, resp);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading order details for order #" + orderId, e);
            resp.sendRedirect(req.getContextPath() + "/orders");
        }
    }

    private void handleCancelOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        int orderId = ValidationUtil.parsePositiveInt(req.getParameter("orderId"), -1);

        try {
            boolean cancelled = orderService.cancelOrder(orderId, user.getUserId(), user.isAdmin());
            if (cancelled) {
                session.setAttribute("flashSuccess", "Order #" + orderId + " has been successfully cancelled.");
            } else {
                session.setAttribute("flashError", "Unable to cancel order #" + orderId);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error cancelling order #" + orderId + ": " + e.getMessage());
            session.setAttribute("flashError", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/order-details?id=" + orderId);
    }
}
