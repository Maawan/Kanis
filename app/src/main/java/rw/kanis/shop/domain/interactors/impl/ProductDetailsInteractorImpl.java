package rw.kanis.shop.domain.interactors.impl;

import rw.kanis.shop.Network.ApiClient;
import rw.kanis.shop.Network.response.ProductDetialsResponse;
import rw.kanis.shop.Network.services.ProductDetailsApiInterface;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.ProductDetailsInteractor;
import rw.kanis.shop.domain.interactors.base.AbstractInteractor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsInteractorImpl extends AbstractInteractor {
    private ProductDetailsInteractor.CallBack mCallback;
    private ProductDetailsApiInterface apiService;
    private String url;

    public ProductDetailsInteractorImpl(Executor threadExecutor, MainThread mainThread, ProductDetailsInteractor.CallBack callBack, String url) {
        super(threadExecutor, mainThread);
        mCallback = callBack;
        this.url = url;
    }

    @Override
    public void run() {
        apiService = ApiClient.getClient().create(ProductDetailsApiInterface.class);
        Call<ProductDetialsResponse> call = apiService.getProductDetails(url);

        call.enqueue(new Callback<ProductDetialsResponse>() {
            @Override
            public void onResponse(Call<ProductDetialsResponse> call, Response<ProductDetialsResponse> response) {
                try {
                    //Log.d("Mehedi", response.toString());
                    mCallback.onProductDetailsDownloaded(response.body().getData().get(0));
                } catch (Exception e) {
                    //Log.d("Mehedi", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ProductDetialsResponse> call, Throwable t) {
                //Log.d("Mehedi", t.getLocalizedMessage());
                mCallback.onProductDetailsDownloadError();
            }
        });

    }
}
