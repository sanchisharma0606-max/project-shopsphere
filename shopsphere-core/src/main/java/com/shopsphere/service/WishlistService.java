package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.WishlistDAO;
import com.shopsphere.model.WishlistItem;

import java.sql.SQLException;
import java.util.List;

/**
 * Service managing customer wishlist and moving items to cart.
 */
public class WishlistService {

    private final WishlistDAO wishlistDAO;
    private final CartDAO cartDAO;

    public WishlistService() {
        this.wishlistDAO = new WishlistDAO();
        this.cartDAO = new CartDAO();
    }

    public WishlistService(WishlistDAO wishlistDAO, CartDAO cartDAO) {
        this.wishlistDAO = wishlistDAO;
        this.cartDAO = cartDAO;
    }

    public boolean addToWishlist(int userId, int productId) throws SQLException {
        return wishlistDAO.addToWishlist(userId, productId);
    }

    public boolean removeFromWishlist(int userId, int productId) throws SQLException {
        return wishlistDAO.removeFromWishlist(userId, productId);
    }

    public boolean isInWishlist(int userId, int productId) throws SQLException {
        return wishlistDAO.isInWishlist(userId, productId);
    }

    public List<WishlistItem> getWishlist(int userId) throws SQLException {
        return wishlistDAO.getWishlistByUserId(userId);
    }

    public int getWishlistCount(int userId) throws SQLException {
        return wishlistDAO.getWishlistCount(userId);
    }

    public boolean moveToCart(int userId, int productId) throws SQLException {
        boolean added = cartDAO.addToCart(userId, productId, 1);
        if (added) {
            wishlistDAO.removeFromWishlist(userId, productId);
            return true;
        }
        return false;
    }
}
