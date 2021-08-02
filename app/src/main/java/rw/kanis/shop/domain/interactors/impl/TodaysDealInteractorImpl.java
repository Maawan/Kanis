package rw.kanis.shop.domain.interactors.impl;

import android.util.Log;
import android.widget.Toast;

import rw.kanis.shop.Network.ApiClient;
import rw.kanis.shop.Network.response.ProductResponse;
import rw.kanis.shop.Network.services.TodaysDealApiInterface;
import rw.kanis.shop.Presentation.ui.activities.impl.MainActivity;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.TodaysDealInteractor;
import rw.kanis.shop.domain.interactors.base.AbstractInteractor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodaysDealInteractorImpl extends AbstractInteractor {
    private TodaysDealInteractor.CallBack mCallback;
    private TodaysDealApiInterface apiService;

    public TodaysDealInteractorImpl(Executor threadExecutor, MainThread mainThread, TodaysDealInteractor.CallBack callBack) {
        super(threadExecutor, mainThread);
        mCallback = callBack;
    }

    @Override
    public void run() {
        apiService = ApiClient.getClient().create(TodaysDealApiInterface.class);
        Call<ProductResponse> call = apiService.getTodaysDeal();

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                try {
                    mCallback.onTodaysDealProductDownloaded(response.body().getData());

                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                mCallback.onTodaysDealProductDownloadError();
            }
        });

    }
}
