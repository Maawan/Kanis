package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.RegistrationResponse;

public interface RegisterInteractor {
    interface CallBack {

        void onRegistrationDone(RegistrationResponse registrationResponse);

        void onRegistrationError();
    }
}
