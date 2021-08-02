package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.AuthResponse;

public interface LoginInteractor {
    interface CallBack {

        void onValidLogin(AuthResponse authResponse);

        void onLoginError();
    }
}
