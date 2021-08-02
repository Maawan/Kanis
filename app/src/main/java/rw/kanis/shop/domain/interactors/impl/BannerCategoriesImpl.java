package rw.kanis.shop.domain.interactors.impl;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rw.kanis.shop.Models.Banner;
import rw.kanis.shop.Network.ApiClient;
import rw.kanis.shop.Network.BannerCategoriesApiInterface;
import rw.kanis.shop.Network.response.BannerCategoryResponse;
import rw.kanis.shop.Network.response.BannerResponse;
import rw.kanis.shop.Network.response.ProductResponse;
import rw.kanis.shop.Network.services.TempApiClient;
import rw.kanis.shop.Network.services.TodaysDealApiInterface;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.BannerCategoryInteractor;
import rw.kanis.shop.domain.interactors.TodaysDealInteractor;
import rw.kanis.shop.domain.interactors.base.AbstractInteractor;

public class BannerCategoriesImpl extends AbstractInteractor {
    private BannerCategoryInteractor.CallBack mCallback;
    private BannerCategoriesApiInterface apiService;


    public BannerCategoriesImpl(Executor threadExecutor, MainThread mainThread, BannerCategoryInteractor.CallBack callBack) {
        super(threadExecutor, mainThread);
        this.mCallback = callBack;
    }

    @Override
    public void run() {
        apiService = ApiClient.getClient().create(BannerCategoriesApiInterface.class);
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
