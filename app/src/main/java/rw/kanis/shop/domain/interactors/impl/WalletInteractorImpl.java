package rw.kanis.shop.domain.interactors.impl;

import android.util.Log;

import rw.kanis.shop.Network.ApiClient;
import rw.kanis.shop.Network.response.OrderResponse;
import rw.kanis.shop.Network.services.WalletApiInterface;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.WalletInteractor;
import rw.kanis.shop.domain.interactors.base.AbstractInteractor;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletInteractorImpl extends AbstractInteractor {
    private WalletInteractor.CallBack mCallback;
    private WalletApiInterface apiService;
    private JsonObject jsonObject;
    private String auth_token;

    public WalletInteractorImpl(Executor threadExecutor, MainThread mainThread, WalletInteractor.CallBack callBack, String auth_token, JsonObject jsonObject) {
        super(threadExecutor, mainThread);
        mCallback = callBack;
        this.auth_token = "Bearer "+auth_token;
        this.jsonObject = jsonObject;
    }

    @Override
    public void run() {
        apiService = ApiClient.getClient().create(WalletApiInterface.class);

        Call<OrderResponse> call = apiService.sendPlaceOrderRequest(auth_token, jsonObject);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                try {
                    //Log.d("Test", response.body().getVariant());
                    mCallback.onWalletOrderSubmitted(response.body());
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                //Log.d("Test", String.valueOf(call.isExecuted()));
                mCallback.onWalletOrderSubmitError();
            }
        });

    }
}
