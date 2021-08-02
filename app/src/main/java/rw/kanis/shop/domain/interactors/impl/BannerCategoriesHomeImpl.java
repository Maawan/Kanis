package rw.kanis.shop.domain.interactors.impl;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rw.kanis.shop.Network.ApiClient;
import rw.kanis.shop.Network.BannerCategoriesApiInterface;
import rw.kanis.shop.Network.response.BannerCategoryResponse;
import rw.kanis.shop.Network.services.BannerCategoriesHomeApiInterface;
import rw.kanis.shop.Network.services.TempApiClient;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.BannerCategoryInteractor;
import rw.kanis.shop.domain.interactors.base.AbstractInteractor;

public class BannerCategoriesHomeImpl extends AbstractInteractor {
    private BannerCategoryInteractor.CallBack mCallback;
    private BannerCategoriesHomeApiInterface apiService;


    public BannerCategoriesHomeImpl(Executor threadExecutor, MainThread mainThread, BannerCategoryInteractor.CallBack callBack) {
        super(threadExecutor, mainThread);
        this.mCallback = callBack;
    }

    @Override
    public void run() {
        apiService = ApiClient.getClient().create(BannerCategoriesHomeApiInterface.class);
        Call<BannerCategoryResponse> call = apiService.getBannerResponse();

        call.enqueue(new Callback<BannerCategoryResponse>() {
            @Override
            public void onResponse(Call<BannerCategoryResponse> call, Response<BannerCategoryResponse> response) {
                try {
                    mCallback.onBannerDataDownloaded(response.body().getData());

                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<BannerCategoryResponse> call, Throwable t) {
                mCallback.onBannerDataDownloadError(t);
            }
        });

    }
}


