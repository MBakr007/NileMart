package org.route.nilemartspringboot.repository.cart;

import org.route.nilemartspringboot.model.Cart;
import org.route.nilemartspringboot.model.CartItem;

public interface CartRepository {

    Cart getCart(String customerId);

    void addItem(CartItem cartItem, String customerId);
}
