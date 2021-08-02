package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.User;

public interface UserInfoInteractor {
    interface CallBack {

        void onUserInfoLodaded(User user);

        void onUserInfoError();
    }
}
