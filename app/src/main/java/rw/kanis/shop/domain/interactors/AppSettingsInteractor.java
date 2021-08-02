package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.AppSettingsResponse;

public interface AppSettingsInteractor {
    interface CallBack {

        void onAppSettingsLoaded(AppSettingsResponse appSettingsResponse);

        void onAppSettingsLoadedError();
    }
}
