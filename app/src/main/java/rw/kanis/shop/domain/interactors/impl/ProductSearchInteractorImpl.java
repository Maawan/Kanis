package rw.kanis.shop.domain.interactors.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import rw.kanis.shop.Network.ApiClient;
import rw.kanis.shop.Network.response.ProductSearchResponse;
import rw.kanis.shop.Network.services.ProductSearchApiInterface;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.ProductSearchInteractor;
import rw.kanis.shop.domain.interactors.base.AbstractInteractor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductSearchInteractorImpl extends AbstractInteractor {
    private ProductSearchInteractor.CallBack mCallback;
    private ProductSearchApiInterface apiService;
    private String url;

    public ProductSearchInteractorImpl(Executor threadExecutor, MainThread mainThread, ProductSearchInteractor.CallBack callBack, String url) {
        super(threadExecutor, mainThread);
        mCallback = callBack;
        this.url = url;
    }

    @Override
    public void run() {
        apiService = ApiClient.getClient().create(ProductSearchApiInterface.class);
        Call<ProductSearchResponse> call = apiService.getSearchedProducts(url);

        call.enqueue(new Callback<ProductSearchResponse>() {
            @Override
            public void onResponse(Call<ProductSearchResponse> call, Response<ProductSearchResponse> response) {
                try {
                    mCallback.onProductSearched(response.body());
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ProductSearchResponse> call, Throwable t) {
                Log.e("Exception", t.getMessage());
                mCallback.onProductSearchedError();
            }
        });

    }
}
