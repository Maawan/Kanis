package rw.kanis.shop.Presentation.ui.fragments;

import rw.kanis.shop.Models.CartModel;
import rw.kanis.shop.Network.response.CartQuantityUpdateResponse;
import rw.kanis.shop.Network.response.RemoveCartResponse;

import java.util.List;

public interface CartView {
    void setCartItems(List<CartModel> cartItems);
    void showRemoveCartMessage(RemoveCartResponse removeCartResponse);
    void showCartQuantityUpdateMessage(CartQuantityUpdateResponse cartQuantityUpdateResponse);
}
