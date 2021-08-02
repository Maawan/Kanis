package rw.kanis.shop.domain.interactors.impl;

import android.util.Log;

import rw.kanis.shop.Network.ApiClient;
import rw.kanis.shop.Network.response.ReviewResponse;
import rw.kanis.shop.Network.services.ReviewApiInterface;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.ReviewInteractor;
import rw.kanis.shop.domain.interactors.base.AbstractInteractor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewInteractorImpl extends AbstractInteractor {
    private ReviewInteractor.CallBack mCallback;
    private ReviewApiInterface apiService;
    private String url;

    public ReviewInteractorImpl(Executor threadExecutor, MainThread mainThread, ReviewInteractor.CallBack callBack, String url) {
        super(threadExecutor, mainThread);
        mCallback = callBack;
        this.url = url;
    }

    @Override
    public void run() {
        apiService = ApiClient.getClient().create(ReviewApiInterface.class);
        Call<ReviewResponse> call = apiService.getReviews(url);

        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                try {
                    mCallback.onReviewLodaded(response.body().getData());
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                mCallback.onReviewError();
            }
        });

    }
}
