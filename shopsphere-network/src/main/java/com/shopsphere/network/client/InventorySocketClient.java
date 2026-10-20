package com.shopsphere.network.client;

import com.shopsphere.network.protocol.InventoryProtocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Java Socket Client demonstrating java.net.Socket communication with ShopSphere Inventory Server.
 */
public class InventorySocketClient {

    private final String host;
    private final int port;

    public InventorySocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void runInteractive() {
        System.out.println("Connecting to ShopSphere Inventory Server at " + host + ":" + port + "...");

        try (
            Socket socket = new Socket(host, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            Scanner scanner = new Scanner(System.in)
        ) {
            // Read welcome banner
            String serverBanner = in.readLine();
            System.out.println("SERVER: " + serverBanner);
            System.out.println("Enter commands (e.g. 'PING', 'GET_PRODUCT 1', 'GET_STOCK 1', 'SEARCH_PRODUCT Apple', 'QUIT'):");

            while (true) {
                System.out.print("ShopSphere-Client> ");
                if (!scanner.hasNextLine()) break;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                out.println(line);
                String response = in.readLine();
                if (response == null) {
                    System.out.println("[Server closed connection]");
                    break;
                }

                System.out.println("RESPONSE: " + response);

                if (InventoryProtocol.CMD_QUIT.equalsIgnoreCase(line)) {
                    break;
                }
            }

        } catch (Exception e) {
            System.err.println("Connection failed to " + host + ":" + port + " -> " + e.getMessage());
        }
    }

    /**
     * Executes an automated sequence of protocol commands and prints the verification results.
     */
    public boolean runAutomatedTest() {
        System.out.println("[Socket Client Test] Connecting to " + host + ":" + port + "...");
        try (
            Socket socket = new Socket(host, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)
        ) {
            socket.setSoTimeout(4000);
            String banner = in.readLine();
            System.out.println(">> Banner: " + banner);

            String[] testCommands = {"PING", "GET_PRODUCT 1", "GET_STOCK 1", "QUIT"};
            for (String cmd : testCommands) {
                out.println(cmd);
                String resp = in.readLine();
                System.out.println(">> Command '" + cmd + "' => " + resp);
            }
            return true;
        } catch (Exception e) {
            System.err.println("[Socket Client Test Error]: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = InventoryProtocol.DEFAULT_PORT;

        if (args.length > 0 && "--test".equalsIgnoreCase(args[0])) {
            new InventorySocketClient(host, port).runAutomatedTest();
            return;
        }

        if (args.length >= 2) {
            host = args[0];
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {}
        }

        new InventorySocketClient(host, port).runInteractive();
    }
}
