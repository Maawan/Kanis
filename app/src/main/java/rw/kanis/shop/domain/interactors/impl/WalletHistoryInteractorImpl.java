package rw.kanis.shop.domain.interactors.impl;

import android.util.Log;

import rw.kanis.shop.Network.ApiClient;
import rw.kanis.shop.Network.response.WalletHistoryResponse;
import rw.kanis.shop.Network.services.WalletHistoryApiInterface;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.WalletHistoryInteractor;
import rw.kanis.shop.domain.interactors.base.AbstractInteractor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletHistoryInteractorImpl extends AbstractInteractor {
    private WalletHistoryInteractor.CallBack mCallback;
    private WalletHistoryApiInterface apiService;
    private int user_id;
    private String auth_token;

    public WalletHistoryInteractorImpl(Executor threadExecutor, MainThread mainThread, WalletHistoryInteractor.CallBack callBack, int user_id, String auth_token) {
        super(threadExecutor, mainThread);
        mCallback = callBack;
        this.user_id = user_id;
        this.auth_token = "Bearer "+auth_token;
    }

    @Override
    public void run() {
        apiService = ApiClient.getClient().create(WalletHistoryApiInterface.class);

        Call<WalletHistoryResponse> call = apiService.getWalletHistory(auth_token, user_id);

        call.enqueue(new Callback<WalletHistoryResponse>() {
            @Override
            public void onResponse(Call<WalletHistoryResponse> call, Response<WalletHistoryResponse> response) {
                try {
                    mCallback.onWalletHistoryLodaded(response.body().getData());
                } catch (Exception e) {
                    Log.e("Wallet Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<WalletHistoryResponse> call, Throwable t) {
                Log.d("Wallet Test", t.getMessage());
                mCallback.onWalletHistoryLoadError();
            }
        });

    }
}
