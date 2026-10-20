package com.shopsphere.web.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * HttpSessionListener demonstrating session lifecycle events and active session counting.
 */
@WebListener
public class SessionTrackerListener implements HttpSessionListener {

    private static final Logger LOGGER = Logger.getLogger(SessionTrackerListener.class.getName());
    private static final AtomicInteger ACTIVE_SESSIONS = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int count = ACTIVE_SESSIONS.incrementAndGet();
        se.getSession().getServletContext().setAttribute("activeSessions", count);
        LOGGER.fine("HTTP Session created: " + se.getSession().getId() + " | Active sessions: " + count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int count = ACTIVE_SESSIONS.decrementAndGet();
        if (count < 0) {
            ACTIVE_SESSIONS.set(0);
            count = 0;
        }
        se.getSession().getServletContext().setAttribute("activeSessions", count);
        LOGGER.fine("HTTP Session destroyed: " + se.getSession().getId() + " | Active sessions: " + count);
    }

    public static int getActiveSessionCount() {
        return ACTIVE_SESSIONS.get();
    }
}
