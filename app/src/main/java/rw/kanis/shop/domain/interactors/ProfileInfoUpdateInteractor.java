package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.ProfileInfoUpdateResponse;

public interface ProfileInfoUpdateInteractor {
    interface CallBack {

        void onProfileInfoUpdated(ProfileInfoUpdateResponse profileInfoUpdateResponse);

        void onProfileInfoUpdatedError();
    }
}
