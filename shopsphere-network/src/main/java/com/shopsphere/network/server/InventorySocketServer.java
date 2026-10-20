package com.shopsphere.network.server;

import com.shopsphere.network.protocol.InventoryProtocol;
import com.shopsphere.service.ProductService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Multi-threaded Java Socket Server for remote inventory lookups and updates.
 * Demonstrates java.net.ServerSocket and concurrent client connection handling.
 */
public class InventorySocketServer {

    private static final Logger LOGGER = Logger.getLogger(InventorySocketServer.class.getName());

    private final int port;
    private final ProductService productService;
    private final ExecutorService threadPool;
    private volatile boolean running = true;
    private ServerSocket serverSocket;

    public InventorySocketServer(int port) {
        this.port = port;
        this.productService = new ProductService();
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            LOGGER.info("=================================================");
            LOGGER.info(" ShopSphere Inventory Socket Server RUNNING");
            LOGGER.info(" Listening on TCP port: " + port);
            LOGGER.info(" Supported commands: PING, GET_PRODUCT, GET_STOCK,");
            LOGGER.info(" SEARCH_PRODUCT, UPDATE_STOCK, QUIT");
            LOGGER.info("=================================================");

            Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.submit(new ClientHandler(clientSocket, productService));
                } catch (IOException e) {
                    if (!running) {
                        break;
                    }
                    LOGGER.log(Level.WARNING, "Exception accepting socket connection", e);
                }
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not bind server socket to port " + port, e);
        } finally {
            stop();
        }
    }

    public void stop() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {}
        }
        threadPool.shutdown();
        LOGGER.info("Inventory Socket Server terminated cleanly.");
    }

    public static void main(String[] args) {
        int port = InventoryProtocol.DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }
        new InventorySocketServer(port).start();
    }
}
