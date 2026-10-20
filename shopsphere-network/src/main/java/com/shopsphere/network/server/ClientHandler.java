package com.shopsphere.network.server;

import com.shopsphere.model.Product;
import com.shopsphere.network.protocol.InventoryProtocol;
import com.shopsphere.service.ProductService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runnable worker handling a client connection in a separate thread.
 */
public class ClientHandler implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

    private final Socket clientSocket;
    private final ProductService productService;

    public ClientHandler(Socket socket, ProductService productService) {
        this.clientSocket = socket;
        this.productService = productService;
    }

    @Override
    public void run() {
        String clientAddress = clientSocket.getRemoteSocketAddress().toString();
        LOGGER.info("Client connected from: " + clientAddress);

        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8)
        ) {
            // Send welcome greeting
            writer.println(InventoryProtocol.RESP_OK + " | ShopSphere Inventory Socket Server v1.0");

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                LOGGER.fine("Received command from " + clientAddress + ": " + line);

                if (InventoryProtocol.CMD_QUIT.equalsIgnoreCase(line)) {
                    writer.println(InventoryProtocol.RESP_BYE + " | Connection closed");
                    break;
                }

                String response = processCommand(line);
                writer.println(response);
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Connection error with " + clientAddress + ": " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                LOGGER.info("Client disconnected: " + clientAddress);
            } catch (Exception ignored) {}
        }
    }

    private String processCommand(String commandLine) {
        String[] parts = commandLine.split("\\s+", 3);
        String action = parts[0].toUpperCase();

        try {
            switch (action) {
                case InventoryProtocol.CMD_PING -> {
                    return InventoryProtocol.RESP_OK + " | PONG";
                }

                case InventoryProtocol.CMD_GET_PRODUCT -> {
                    if (parts.length < 2) {
                        return InventoryProtocol.RESP_BAD_REQUEST + " | Usage: GET_PRODUCT <productId>";
                    }
                    int id = Integer.parseInt(parts[1]);
                    Optional<Product> prod = productService.getProductById(id);
                    if (prod.isPresent()) {
                        Product p = prod.get();
                        return InventoryProtocol.RESP_OK + " | ID=" + p.getProductId() +
                                ";NAME=" + p.getName() +
                                ";BRAND=" + p.getBrand() +
                                ";PRICE=" + p.getPrice() +
                                ";STOCK=" + p.getStock() +
                                ";STATUS=" + p.getStatus();
                    } else {
                        return InventoryProtocol.RESP_NOT_FOUND + " | Product #" + id + " not found";
                    }
                }

                case InventoryProtocol.CMD_GET_STOCK -> {
                    if (parts.length < 2) {
                        return InventoryProtocol.RESP_BAD_REQUEST + " | Usage: GET_STOCK <productId>";
                    }
                    int id = Integer.parseInt(parts[1]);
                    Optional<Product> prod = productService.getProductById(id);
                    if (prod.isPresent()) {
                        return InventoryProtocol.RESP_OK + " | PRODUCT_ID=" + id + ";STOCK=" + prod.get().getStock();
                    } else {
                        return InventoryProtocol.RESP_NOT_FOUND + " | Product #" + id + " not found";
                    }
                }

                case InventoryProtocol.CMD_SEARCH -> {
                    if (parts.length < 2) {
                        return InventoryProtocol.RESP_BAD_REQUEST + " | Usage: SEARCH_PRODUCT <keyword>";
                    }
                    String keyword = commandLine.substring(parts[0].length()).trim();
                    List<Product> results = productService.searchAndFilter(keyword, null, null, null, null, 1, 5);
                    if (results.isEmpty()) {
                        return InventoryProtocol.RESP_NOT_FOUND + " | No matching products";
                    }
                    StringBuilder sb = new StringBuilder(InventoryProtocol.RESP_OK + " | COUNT=" + results.size() + " | ");
                    for (Product p : results) {
                        sb.append("[").append(p.getProductId()).append(":").append(p.getName())
                          .append(" (Stock:").append(p.getStock()).append(")] ");
                    }
                    return sb.toString().trim();
                }

                case InventoryProtocol.CMD_UPDATE_STOCK -> {
                    if (parts.length < 3) {
                        return InventoryProtocol.RESP_BAD_REQUEST + " | Usage: UPDATE_STOCK <productId> <newStock>";
                    }
                    int id = Integer.parseInt(parts[1]);
                    int stock = Integer.parseInt(parts[2]);
                    boolean updated = productService.updateStock(id, stock);
                    if (updated) {
                        return InventoryProtocol.RESP_OK + " | Updated Product #" + id + " stock to " + stock;
                    } else {
                        return InventoryProtocol.RESP_NOT_FOUND + " | Failed to update Product #" + id;
                    }
                }

                default -> {
                    return InventoryProtocol.RESP_BAD_REQUEST + " | Unknown command: '" + action + "'. Valid: PING, GET_PRODUCT, GET_STOCK, SEARCH_PRODUCT, UPDATE_STOCK, QUIT";
                }
            }
        } catch (NumberFormatException e) {
            return InventoryProtocol.RESP_BAD_REQUEST + " | Invalid numeric parameter format";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Server error handling command: " + commandLine, e);
            return InventoryProtocol.RESP_SERVER_ERROR + " | " + e.getMessage();
        }
    }
}
