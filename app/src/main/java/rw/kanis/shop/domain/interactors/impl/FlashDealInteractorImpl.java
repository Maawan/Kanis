package rw.kanis.shop.domain.interactors.impl;

import android.util.Log;

import rw.kanis.shop.Network.ApiClient;
import rw.kanis.shop.Network.response.FlashDealResponse;
import rw.kanis.shop.Network.services.FlashDealApiInterface;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.FlashDealInteractor;
import rw.kanis.shop.domain.interactors.base.AbstractInteractor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlashDealInteractorImpl extends AbstractInteractor {
    private FlashDealInteractor.CallBack mCallback;
    private FlashDealApiInterface apiService;

    public FlashDealInteractorImpl(Executor threadExecutor, MainThread mainThread, FlashDealInteractor.CallBack callBack) {
        super(threadExecutor, mainThread);
        mCallback = callBack;
    }

    @Override
    public void run() {
        apiService = ApiClient.getClient().create(FlashDealApiInterface.class);
        Call<FlashDealResponse> call = apiService.getFlashDeal();

        call.enqueue(new Callback<FlashDealResponse>() {
            @Override
            public void onResponse(Call<FlashDealResponse> call, Response<FlashDealResponse> response) {
                try {
                    //Log.d("FlashDeal", response.body().getData().getTitle());
                    mCallback.onFlashDealProductDownloaded(response.body().getData());
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<FlashDealResponse> call, Throwable t) {
                mCallback.onFlashDealProductDownloadError();
            }
        });

    }
}