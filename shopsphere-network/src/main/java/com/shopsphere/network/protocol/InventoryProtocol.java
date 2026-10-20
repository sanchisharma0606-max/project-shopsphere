package com.shopsphere.network.protocol;

/**
 * Protocol definition for ShopSphere Socket Networking.
 */
public class InventoryProtocol {

    public static final int DEFAULT_PORT = 9090;

    // Supported Client Commands
    public static final String CMD_PING = "PING";
    public static final String CMD_GET_PRODUCT = "GET_PRODUCT";
    public static final String CMD_GET_STOCK = "GET_STOCK";
    public static final String CMD_SEARCH = "SEARCH_PRODUCT";
    public static final String CMD_UPDATE_STOCK = "UPDATE_STOCK";
    public static final String CMD_QUIT = "QUIT";

    // Server Response Status Prefixes
    public static final String RESP_OK = "200 OK";
    public static final String RESP_NOT_FOUND = "404 NOT_FOUND";
    public static final String RESP_BAD_REQUEST = "400 BAD_REQUEST";
    public static final String RESP_SERVER_ERROR = "500 SERVER_ERROR";
    public static final String RESP_BYE = "200 BYE";

    private InventoryProtocol() {}
}
