package rw.kanis.shop.domain.interactors.impl;

import android.util.Log;

import rw.kanis.shop.Network.ApiClient;
import rw.kanis.shop.Network.response.BrandResponse;
import rw.kanis.shop.Network.services.BrandApiInterface;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.BrandInteractor;
import rw.kanis.shop.domain.interactors.base.AbstractInteractor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandInteractorImpl extends AbstractInteractor {
    private BrandInteractor.CallBack mCallback;
    private BrandApiInterface apiService;

    public BrandInteractorImpl(Executor threadExecutor, MainThread mainThread, BrandInteractor.CallBack callBack) {
        super(threadExecutor, mainThread);
        mCallback = callBack;
    }

    @Override
    public void run() {
        apiService = ApiClient.getClient().create(BrandApiInterface.class);
        Call<BrandResponse> call = apiService.getBrands();

        call.enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                try {
                    mCallback.onBrandsDownloaded(response.body().getData());
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {
                mCallback.onBrandsDownloadError();
            }
        });

    }
}
