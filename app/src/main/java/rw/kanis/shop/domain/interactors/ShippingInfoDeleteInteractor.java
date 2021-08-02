package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.ShippingInfoResponse;

public interface ShippingInfoDeleteInteractor {
    interface CallBack {

        void onShippingInfoDeleted(ShippingInfoResponse shippingInfoResponse);

        void onShippingInfoDeleteError();
    }
}
