package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.CartModel;

import java.util.List;

public interface CartInteractor {
    interface CallBack {

        void onCartLodaded(List<CartModel> cartModel);

        void onCartError();
    }
}
