package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.AuthResponse;

public interface SocialLoginInteractor {
    interface CallBack {

        void onValidLogin(AuthResponse authResponse);

        void onLoginError();
    }
}
