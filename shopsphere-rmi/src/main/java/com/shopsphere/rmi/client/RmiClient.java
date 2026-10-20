package com.shopsphere.rmi.client;

import com.shopsphere.model.Product;
import com.shopsphere.rmi.remote.InventoryService;
import com.shopsphere.rmi.server.RmiServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Java RMI Client demonstrating distributed remote method invocations against ShopSphere RMI Server.
 */
public class RmiClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = RmiServer.DEFAULT_PORT;
        String serviceName = RmiServer.SERVICE_NAME;

        if (args.length >= 1) host = args[0];
        if (args.length >= 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {}
        }

        System.out.println("==========================================================");
        System.out.println(" ShopSphere Java RMI Client Demonstration");
        System.out.println(" Connecting to Registry at " + host + ":" + port + "...");
        System.out.println("==========================================================");

        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            System.out.println("Looking up service '" + serviceName + "' in RMI Registry...");
            InventoryService service = (InventoryService) registry.lookup(serviceName);

            // 1. Remote Ping
            System.out.println("\n[1] Invoking remote ping()...");
            String pingResponse = service.ping();
            System.out.println("    Remote Response: " + pingResponse);

            // 2. Remote getStock()
            int productId = 1;
            System.out.println("\n[2] Invoking remote getStock(" + productId + ")...");
            int stock = service.getStock(productId);
            System.out.println("    Remote Response: Current stock for Product #" + productId + " = " + stock + " units");

            // 3. Remote getProduct()
            System.out.println("\n[3] Invoking remote getProduct(" + productId + ") to retrieve serialized DTO...");
            Product product = service.getProduct(productId);
            if (product != null) {
                System.out.println("    Received Serialized Product: " + product.getName() + " [Brand: " + product.getBrand() + "]");
                System.out.println("    Price: ₹" + product.getPrice() + " | Discount: " + product.getDiscount() + "%");
                System.out.println("    Calculated Discounted Price: ₹" + product.getDiscountedPrice());
            } else {
                System.out.println("    Product #" + productId + " not found in database.");
            }

            System.out.println("\n==========================================================");
            System.out.println(" RMI Distributed Method Invocation: SUCCESSFUL");
            System.out.println("==========================================================");

        } catch (Exception e) {
            System.err.println("RMI Client execution error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
