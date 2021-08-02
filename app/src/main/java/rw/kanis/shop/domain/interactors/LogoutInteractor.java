package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.LogoutResponse;

public interface LogoutInteractor {
    interface CallBack {

        void onLoggedOut(LogoutResponse logoutResponse);

        void onLoggedOutError();
    }
}
