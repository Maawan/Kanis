package rw.kanis.shop.domain.interactors.impl;

import android.util.Log;

import rw.kanis.shop.Models.Banner;
import rw.kanis.shop.Network.ApiClient;
import rw.kanis.shop.Network.response.BannerResponse;
import rw.kanis.shop.Network.services.BannerApiInterface;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.BannerInteractor;
import rw.kanis.shop.domain.interactors.base.AbstractInteractor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BannerInteractorImpl extends AbstractInteractor {
    private BannerInteractor.CallBack mCallback;
    private BannerApiInterface apiService;

    public BannerInteractorImpl(Executor threadExecutor, MainThread mainThread, BannerInteractor.CallBack callBack) {
        super(threadExecutor, mainThread);
        mCallback = callBack;
    }

    @Override
    public void run() {
        apiService = ApiClient.getClient().create(BannerApiInterface.class);
        Call<BannerResponse> call = apiService.getBanners();

        call.enqueue(new Callback<BannerResponse>() {
            @Override
            public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {
                try {
                    mCallback.onBannersDownloaded(response.body().getData());
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                mCallback.onBannersDownloadError();
            }
        });

    }
}
