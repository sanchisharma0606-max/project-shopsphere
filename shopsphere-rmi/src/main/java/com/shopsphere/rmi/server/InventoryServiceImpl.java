package com.shopsphere.rmi.server;

import com.shopsphere.model.Product;
import com.shopsphere.rmi.remote.InventoryService;
import com.shopsphere.service.ProductService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the remote InventoryService interface.
 * Extends UnicastRemoteObject for remote method dispatch over TCP.
 */
public class InventoryServiceImpl extends UnicastRemoteObject implements InventoryService {

    private static final Logger LOGGER = Logger.getLogger(InventoryServiceImpl.class.getName());

    private final ProductService productService;

    public InventoryServiceImpl() throws RemoteException {
        super();
        this.productService = new ProductService();
    }

    @Override
    public String ping() throws RemoteException {
        return "PONG from ShopSphere RMI Server at " + System.currentTimeMillis();
    }

    @Override
    public Product getProduct(int productId) throws RemoteException {
        LOGGER.info("RMI Request: getProduct(" + productId + ")");
        try {
            Optional<Product> prodOpt = productService.getProductById(productId);
            return prodOpt.orElse(null);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in getProduct(" + productId + ")", e);
            throw new RemoteException("Database error in remote inventory service: " + e.getMessage(), e);
        }
    }

    @Override
    public int getStock(int productId) throws RemoteException {
        LOGGER.info("RMI Request: getStock(" + productId + ")");
        try {
            Optional<Product> prodOpt = productService.getProductById(productId);
            return prodOpt.map(Product::getStock).orElse(-1);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in getStock(" + productId + ")", e);
            throw new RemoteException("Database error in getStock: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateStock(int productId, int quantity) throws RemoteException {
        LOGGER.info("RMI Request: updateStock(" + productId + ", " + quantity + ")");
        try {
            return productService.updateStock(productId, quantity);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error updating stock for #" + productId, e);
            throw new RemoteException("Database error updating stock: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getLowStockAlerts(int threshold) throws RemoteException {
        LOGGER.info("RMI Request: getLowStockAlerts(" + threshold + ")");
        try {
            return productService.getLowStockProducts(threshold);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error getting low stock alerts", e);
            throw new RemoteException("Database error in getLowStockAlerts: " + e.getMessage(), e);
        }
    }
}
