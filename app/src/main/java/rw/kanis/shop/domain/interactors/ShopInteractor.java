package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.Shop;

public interface ShopInteractor {
    interface CallBack {

        void onShopLoaded(Shop shop);

        void onShopLoadError();
    }
}
