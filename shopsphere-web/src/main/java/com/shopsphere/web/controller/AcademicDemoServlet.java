package com.shopsphere.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller providing educational RTU syllabus demonstration endpoints:
 * - JSP scriptlets vs EL/JSTL
 * - JSTL XML feed parsing
 * - JSTL SQL tags demonstration
 * - URL / URLConnection networking
 * - JNDI naming lookup concepts
 * - Historical Applet lifecycle
 */
@WebServlet(name = "AcademicDemoServlet", urlPatterns = {
        "/academic-demos",
        "/demo/scriptlets",
        "/demo/jstl-xml",
        "/demo/jstl-sql",
        "/demo/jndi",
        "/demo/url-connection",
        "/demo/applets-history"
})
public class AcademicDemoServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AcademicDemoServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        switch (servletPath) {
            case "/demo/scriptlets" -> req.getRequestDispatcher("/demo/jsp-elements.jsp").forward(req, resp);
            case "/demo/jstl-xml" -> req.getRequestDispatcher("/demo/jstl-xml.jsp").forward(req, resp);
            case "/demo/jstl-sql" -> req.getRequestDispatcher("/demo/jstl-sql.jsp").forward(req, resp);
            case "/demo/jndi" -> req.getRequestDispatcher("/demo/jndi-url.jsp").forward(req, resp);
            case "/demo/applets-history" -> req.getRequestDispatcher("/demo/applets-history.jsp").forward(req, resp);
            case "/demo/url-connection" -> handleUrlConnectionDemo(req, resp);
            default -> req.getRequestDispatcher("/demo/index.jsp").forward(req, resp);
        }
    }

    private void handleUrlConnectionDemo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String targetUrl = req.getParameter("url");
        if (targetUrl == null || targetUrl.trim().isEmpty()) {
            // Safe default internal endpoint to demonstrate URLConnection
            targetUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() +
                    req.getContextPath() + "/academic-demos";
        }

        StringBuilder content = new StringBuilder();
        int statusCode = 200;
        String contentType = "unknown";

        try {
            URL url = URI.create(targetUrl).toURL();
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.connect();

            contentType = connection.getContentType();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                int linesRead = 0;
                while ((line = in.readLine()) != null && linesRead < 30) {
                    content.append(line).append("\n");
                    linesRead++;
                }
            }
            req.setAttribute("urlSuccess", true);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "URLConnection demo exception: " + e.getMessage());
            req.setAttribute("urlError", "Failed to connect to URL: " + e.getMessage());
            content.append("Connection timed out or host unreachable: ").append(e.getMessage());
        }

        req.setAttribute("targetUrl", targetUrl);
        req.setAttribute("contentType", contentType);
        req.setAttribute("snippetContent", content.toString());

        req.getRequestDispatcher("/demo/jndi-url.jsp").forward(req, resp);
    }
}
