package com.shopsphere.rmi.remote;

import com.shopsphere.model.Product;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote Interface defining distributed inventory services via Java RMI.
 * RTU Syllabus Topic: RMI Architecture, java.rmi.Remote, RemoteException.
 */
public interface InventoryService extends Remote {

    String ping() throws RemoteException;

    Product getProduct(int productId) throws RemoteException;

    int getStock(int productId) throws RemoteException;

    boolean updateStock(int productId, int quantity) throws RemoteException;

    List<Product> getLowStockAlerts(int threshold) throws RemoteException;
}
