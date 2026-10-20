package com.shopsphere.web.listener;

import com.shopsphere.util.DBConnection;
import com.shopsphere.util.DatabaseConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ServletContextListener demonstrating application-level lifecycle events.
 * Initializes global context attributes and checks database connectivity on startup.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        LOGGER.info(">>> ShopSphere Web Application Initializing [Tomcat 11 / Jakarta EE 10] <<<");

        // Set application-wide metadata for JSP access
        ctx.setAttribute("appName", "ShopSphere");
        ctx.setAttribute("appTagline", "Next-Gen Advanced Java E-Commerce Platform");
        ctx.setAttribute("appVersion", "1.0.0");
        ctx.setAttribute("year", java.time.Year.now().getValue());

        // Test database availability at startup
        boolean dbOk = DBConnection.isAvailable();
        ctx.setAttribute("dbConnected", dbOk);

        if (dbOk) {
            LOGGER.info("Database connectivity verified at startup: " + DatabaseConfig.getSanitizedUrl());
        } else {
            LOGGER.log(Level.WARNING, "ShopSphere database is currently unreachable. Configure SHOPSPHERE_DB_* or db.properties. Targeted URL: " + DatabaseConfig.getSanitizedUrl());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info(">>> ShopSphere Web Application Shutting Down Cleanly <<<");
    }
}
