package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.OrderResponse;

public interface CODInteractor {
    interface CallBack {

        void onCODOrderSubmitted(OrderResponse orderResponse);

        void onCODOrderSubmitError();
    }
}
