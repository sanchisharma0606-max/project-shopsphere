package com.shopsphere.rmi.server;

import com.shopsphere.rmi.remote.InventoryService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server launcher that starts the RMI Registry and binds the InventoryService.
 * RTU Syllabus Topic: RMI Registry & Naming lookup.
 */
public class RmiServer {

    private static final Logger LOGGER = Logger.getLogger(RmiServer.class.getName());
    public static final int DEFAULT_PORT = 1099;
    public static final String SERVICE_NAME = "InventoryService";

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        try {
            LOGGER.info("Starting Java RMI Registry on port " + port + "...");
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(port);
                LOGGER.info("Created new local RMI Registry on port " + port);
            } catch (Exception e) {
                // Registry may already be running on this port
                registry = LocateRegistry.getRegistry(port);
                LOGGER.info("Connected to existing RMI Registry on port " + port);
            }

            InventoryService service = new InventoryServiceImpl();
            registry.rebind(SERVICE_NAME, service);

            System.out.println("==========================================================");
            System.out.println(" ShopSphere Java RMI Server RUNNING");
            System.out.println(" Service bound successfully as: " + SERVICE_NAME);
            System.out.println(" RMI Registry URL: rmi://localhost:" + port + "/" + SERVICE_NAME);
            System.out.println(" Press Ctrl+C to terminate server.");
            System.out.println("==========================================================");

            // Keep main thread alive
            Thread.currentThread().join();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize RMI Server: " + e.getMessage(), e);
        }
    }
}
