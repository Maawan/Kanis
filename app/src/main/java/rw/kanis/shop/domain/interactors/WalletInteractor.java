package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.OrderResponse;

public interface WalletInteractor {
    interface CallBack {

        void onWalletOrderSubmitted(OrderResponse orderResponse);

        void onWalletOrderSubmitError();
    }
}
