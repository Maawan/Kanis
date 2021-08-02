package rw.kanis.shop.domain.interactors.impl;

import android.util.Log;

import rw.kanis.shop.Network.ApiClient;
import rw.kanis.shop.Network.response.CartResponse;
import rw.kanis.shop.Network.services.CartApiInterface;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.CartInteractor;
import rw.kanis.shop.domain.interactors.base.AbstractInteractor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartInteractorImpl extends AbstractInteractor {
    private CartInteractor.CallBack mCallback;
    private CartApiInterface apiService;
    private int user_id;
    private String token;

    public CartInteractorImpl(Executor threadExecutor, MainThread mainThread, CartInteractor.CallBack callBack, int id, String token) {
        super(threadExecutor, mainThread);
        mCallback = callBack;
        this.user_id = id;
        this.token = "Bearer "+token;
    }

    @Override
    public void run() {
        apiService = ApiClient.getClient().create(CartApiInterface.class);
        Call<CartResponse> call = apiService.getCartItems(token,"carts/"+user_id);
        String t = token;

        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                try {
                    mCallback.onCartLodaded(response.body().getData());
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                mCallback.onCartError();
            }
        });

    }
}
