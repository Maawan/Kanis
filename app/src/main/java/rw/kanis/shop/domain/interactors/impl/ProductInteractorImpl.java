package rw.kanis.shop.domain.interactors.impl;

import android.util.Log;

import rw.kanis.shop.Network.ApiClient;
import rw.kanis.shop.Network.response.ProductResponse;
import rw.kanis.shop.Network.services.ProductApiInterface;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.ProductInteractor;
import rw.kanis.shop.domain.interactors.base.AbstractInteractor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductInteractorImpl extends AbstractInteractor {
    private ProductInteractor.CallBack mCallback;
    private ProductApiInterface apiService;
    private String url;

    public ProductInteractorImpl(Executor threadExecutor, MainThread mainThread, ProductInteractor.CallBack callBack, String url) {
        super(threadExecutor, mainThread);
        mCallback = callBack;
        this.url = url;
    }

    @Override
    public void run() {
        apiService = ApiClient.getClient().create(ProductApiInterface.class);
        Call<ProductResponse> call = apiService.getProducts(url);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                try {
                    mCallback.onProductDownloaded(response.body().getData());
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                mCallback.onProductDownloadError();
            }
        });

    }
}
