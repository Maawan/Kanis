package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.OrderResponse;

public interface PaypalInteractor {
    interface CallBack {

        void onPayaplOrderSubmitted(OrderResponse orderResponse);

        void onPayaplOrderSubmitError();
    }
}
