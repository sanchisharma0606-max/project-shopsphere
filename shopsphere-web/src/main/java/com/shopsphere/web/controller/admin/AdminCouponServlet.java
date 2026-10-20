package com.shopsphere.web.controller.admin;

import com.shopsphere.model.Coupon;
import com.shopsphere.service.CouponService;
import com.shopsphere.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for Administrator Coupon Management.
 */
@WebServlet(name = "AdminCouponServlet", urlPatterns = {"/admin/coupons", "/admin/coupons/add", "/admin/coupons/edit", "/admin/coupons/delete"})
public class AdminCouponServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminCouponServlet.class.getName());

    private CouponService couponService;

    @Override
    public void init() {
        this.couponService = new CouponService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        try {
            if ("/admin/coupons/add".equals(servletPath)) {
                req.getRequestDispatcher("/admin/coupon-form.jsp").forward(req, resp);
                return;
            }

            if ("/admin/coupons/edit".equals(servletPath)) {
                int id = ValidationUtil.parsePositiveInt(req.getParameter("id"), -1);
                Optional<Coupon> couponOpt = couponService.getCouponById(id);
                if (couponOpt.isEmpty()) {
                    resp.sendRedirect(req.getContextPath() + "/admin/coupons");
                    return;
                }
                req.setAttribute("coupon", couponOpt.get());
                req.getRequestDispatcher("/admin/coupon-form.jsp").forward(req, resp);
                return;
            }

            List<Coupon> coupons = couponService.getAllCoupons();
            req.setAttribute("coupons", coupons);
            req.getRequestDispatcher("/admin/coupons.jsp").forward(req, resp);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading coupons", e);
            req.setAttribute("errorMessage", "Error loading coupon data.");
            req.getRequestDispatcher("/admin/coupons.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();

        if ("/admin/coupons/delete".equals(servletPath)) {
            handleDelete(req, resp);
            return;
        }

        handleSave(req, resp);
    }

    private void handleSave(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int couponId = ValidationUtil.parsePositiveInt(req.getParameter("couponId"), 0);
        String code = req.getParameter("code");
        String typeParam = req.getParameter("discountType");
        BigDecimal discountValue = ValidationUtil.parseBigDecimal(req.getParameter("discountValue"), BigDecimal.ZERO);
        BigDecimal minOrder = ValidationUtil.parseBigDecimal(req.getParameter("minimumOrder"), BigDecimal.ZERO);
        String maxDiscParam = req.getParameter("maximumDiscount");
        BigDecimal maxDiscount = (maxDiscParam != null && !maxDiscParam.trim().isEmpty())
                ? ValidationUtil.parseBigDecimal(maxDiscParam, null) : null;
        String expiryParam = req.getParameter("expiryDate");
        String statusParam = req.getParameter("status");

        try {
            Coupon.DiscountType type = Coupon.DiscountType.valueOf(typeParam);
            Coupon.Status status = Coupon.Status.valueOf(statusParam);
            Date expiryDate = Date.valueOf(expiryParam);

            Coupon coupon = new Coupon(couponId, code, type, discountValue, minOrder, maxDiscount, expiryDate, status);
            couponService.saveCoupon(coupon);
            req.getSession().setAttribute("flashSuccess", "Coupon '" + code + "' saved successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error saving coupon: " + e.getMessage());
            req.getSession().setAttribute("flashError", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/admin/coupons");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int couponId = ValidationUtil.parsePositiveInt(req.getParameter("couponId"), -1);

        try {
            couponService.deleteCoupon(couponId);
            req.getSession().setAttribute("flashSuccess", "Coupon deleted.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting coupon #" + couponId, e);
            req.getSession().setAttribute("flashError", "Failed to delete coupon.");
        }

        resp.sendRedirect(req.getContextPath() + "/admin/coupons");
    }
}
