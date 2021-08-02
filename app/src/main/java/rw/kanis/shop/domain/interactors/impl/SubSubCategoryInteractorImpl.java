package rw.kanis.shop.domain.interactors.impl;

import android.util.Log;

import rw.kanis.shop.Network.ApiClient;
import rw.kanis.shop.Network.response.SubSubCategoryResponse;
import rw.kanis.shop.Network.services.SubSubCategoryApiInterface;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.SubSubCategoryInteractor;
import rw.kanis.shop.domain.interactors.base.AbstractInteractor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubSubCategoryInteractorImpl extends AbstractInteractor {
    private SubSubCategoryInteractor.CallBack mCallback;
    private SubSubCategoryApiInterface apiService;
    private String url;

    public SubSubCategoryInteractorImpl(Executor threadExecutor, MainThread mainThread, SubSubCategoryInteractor.CallBack callBack, String url) {
        super(threadExecutor, mainThread);
        mCallback = callBack;
        this.url = url;
    }

    @Override
    public void run() {
        apiService = ApiClient.getClient().create(SubSubCategoryApiInterface.class);
        Call<SubSubCategoryResponse> call = apiService.getSubSubcategories(url);

        call.enqueue(new Callback<SubSubCategoryResponse>() {
            @Override
            public void onResponse(Call<SubSubCategoryResponse> call, Response<SubSubCategoryResponse> response) {
                try {
                    mCallback.onSubSubCategoriesDownloaded(response.body().getData());
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SubSubCategoryResponse> call, Throwable t) {
                mCallback.onSubSubCategoriesDownloadError();
            }
        });

    }
}
