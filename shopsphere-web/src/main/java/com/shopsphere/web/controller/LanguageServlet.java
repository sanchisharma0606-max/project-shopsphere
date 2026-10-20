package com.shopsphere.web.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Locale;

/**
 * Controller for switching user interface language/locale (English / Hindi).
 */
@WebServlet(name = "LanguageServlet", urlPatterns = {"/language"})
public class LanguageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String lang = req.getParameter("lang");
        HttpSession session = req.getSession(true);

        if ("hi".equalsIgnoreCase(lang)) {
            session.setAttribute("selectedLocale", Locale.of("hi", "IN"));
            session.setAttribute("langCode", "hi");
        } else {
            session.setAttribute("selectedLocale", Locale.ENGLISH);
            session.setAttribute("langCode", "en");
        }

        String referer = req.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            resp.sendRedirect(referer);
        } else {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }
}
