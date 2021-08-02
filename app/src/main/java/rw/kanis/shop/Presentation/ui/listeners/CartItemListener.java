package rw.kanis.shop.Presentation.ui.listeners;

import rw.kanis.shop.Models.CartModel;

public interface CartItemListener {
    void onCartRemove(CartModel cartModel);
    void onQuantityUpdate(int quantity, CartModel cartModel);
}
