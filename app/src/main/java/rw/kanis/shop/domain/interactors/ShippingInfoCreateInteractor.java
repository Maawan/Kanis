package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.ShippingInfoResponse;

public interface ShippingInfoCreateInteractor {
    interface CallBack {

        void onShippingInfoCreated(ShippingInfoResponse shippingInfoResponse);

        void onShippingInfoCreateError();
    }
}
