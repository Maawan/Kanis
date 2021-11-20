package rw.kanis.shop.Presentation.presenters;

import rw.kanis.shop.Network.response.CouponResponse;
import rw.kanis.shop.Network.response.OrderResponse;
import rw.kanis.shop.Presentation.ui.activities.PaymentView;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.CODInteractor;
import rw.kanis.shop.domain.interactors.CouponInteractor;
import rw.kanis.shop.domain.interactors.OrderInteractor;
import rw.kanis.shop.domain.interactors.PaypalInteractor;
import rw.kanis.shop.domain.interactors.WalletInteractor;
import rw.kanis.shop.domain.interactors.impl.CODInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.CouponInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.OrderInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.PaypalInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.WalletInteractorImpl;
import com.google.gson.JsonObject;

public class PaymentPresenter extends AbstractPresenter implements CouponInteractor.CallBack, PaypalInteractor.CallBack,  CODInteractor.CallBack, OrderInteractor.CallBack, WalletInteractor.CallBack {
    private PaymentView paymentView;


    public PaymentPresenter(Executor executor, MainThread mainThread, PaymentView paymentView) {
        super(executor, mainThread);
        this.paymentView = paymentView;
    }



    public void applyCoupon(int user_id, String code, String token) {
        new CouponInteractorImpl(mExecutor, mMainThread, this, user_id, code, token).execute();
    }

    public void submitPaypalOrder(String token, JsonObject jsonObject) {
        new PaypalInteractorImpl(mExecutor, mMainThread, this, token, jsonObject).execute();
    }



    public void submitWalletOrder(String token, JsonObject jsonObject) {
        new WalletInteractorImpl(mExecutor, mMainThread, this, token, jsonObject).execute();
    }

    public void submitCODOrder(String token, JsonObject jsonObject) {
        new CODInteractorImpl(mExecutor, mMainThread, this, token, jsonObject).execute();
    }

    public void submitOrder(String token, JsonObject jsonObject) {
        new OrderInteractorImpl(mExecutor, mMainThread, this, token, jsonObject).execute();
    }

    @Override
    public void onCouponApplied(CouponResponse couponResponse) {
        if (paymentView != null){
            paymentView.onCouponApplied(couponResponse);
        }
    }

    @Override
    public void onCouponAppliedError() {

    }

    @Override
    public void onPayaplOrderSubmitted(OrderResponse orderResponse) {
        if (paymentView != null){
            paymentView.onOrderSubmitted(orderResponse);
        }
    }

    @Override
    public void onPayaplOrderSubmitError() {

    }



    @Override
    public void onCODOrderSubmitted(OrderResponse orderResponse) {
        if (paymentView != null){
            paymentView.onOrderSubmitted(orderResponse);
        }
    }

    @Override
    public void onCODOrderSubmitError() {

    }

    @Override
    public void onOrderSubmitted(OrderResponse orderResponse) {
        if (paymentView != null){
            paymentView.onOrderSubmitted(orderResponse);
        }
    }

    @Override
    public void onOrderSubmitError() {

    }

    @Override
    public void onWalletOrderSubmitted(OrderResponse orderResponse) {
        if (paymentView != null){
            paymentView.onOrderSubmitted(orderResponse);
        }
    }

    @Override
    public void onWalletOrderSubmitError() {

    }
}
