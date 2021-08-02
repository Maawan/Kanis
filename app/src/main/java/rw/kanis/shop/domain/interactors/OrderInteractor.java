package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.OrderResponse;

public interface OrderInteractor {
    interface CallBack {

        void onOrderSubmitted(OrderResponse orderResponse);

        void onOrderSubmitError();
    }
}
