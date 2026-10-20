package com.shopsphere.web.controller.admin;

import com.shopsphere.model.Order;
import com.shopsphere.service.OrderService;
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
 * Controller for Administrator Order Management and Status Lifecycle Transitions.
 */
@WebServlet(name = "AdminOrderServlet", urlPatterns = {"/admin/orders", "/admin/orders/status"})
public class AdminOrderServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminOrderServlet.class.getName());

    private OrderService orderService;

    @Override
    public void init() {
        this.orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Order> orders = orderService.getAllOrders();
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/admin/orders.jsp").forward(req, resp);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading orders for admin", e);
            req.setAttribute("errorMessage", "Error loading customer orders: " + e.getMessage());
            req.getRequestDispatcher("/admin/orders.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int orderId = ValidationUtil.parsePositiveInt(req.getParameter("orderId"), -1);
        String statusParam = req.getParameter("orderStatus");

        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(statusParam);
            orderService.updateOrderStatus(orderId, newStatus);

            // Automatically mark payment as PAID if DELIVERED or CONFIRMED
            if (newStatus == Order.OrderStatus.DELIVERED) {
                orderService.updatePaymentStatus(orderId, Order.PaymentStatus.PAID);
            }

            req.getSession().setAttribute("flashSuccess", "Order #" + orderId + " updated to " + newStatus);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error updating status for order #" + orderId, e);
            req.getSession().setAttribute("flashError", "Failed to update order: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/admin/orders");
    }
}
