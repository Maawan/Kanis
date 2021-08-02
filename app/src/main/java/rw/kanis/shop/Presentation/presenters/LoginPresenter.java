package rw.kanis.shop.Presentation.presenters;

import rw.kanis.shop.Network.response.AuthResponse;
import rw.kanis.shop.Presentation.ui.activities.LoginView;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.LoginInteractor;
import rw.kanis.shop.domain.interactors.SocialLoginInteractor;
import rw.kanis.shop.domain.interactors.impl.LoginInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.SocialLoginInteractorImpl;

public class LoginPresenter extends AbstractPresenter implements LoginInteractor.CallBack, SocialLoginInteractor.CallBack {

    private LoginView loginView;

    public LoginPresenter(Executor executor, MainThread mainThread, LoginView loginView) {
        super(executor, mainThread);
        this.loginView = loginView;
    }

    public void validLogin(String email, String password) {
        new LoginInteractorImpl(mExecutor, mMainThread, this, email, password).execute();
    }

    public void validSocialLogin(String id, String name, String email) {
        new SocialLoginInteractorImpl(mExecutor, mMainThread, this, id, name, email).execute();
    }

    @Override
    public void onValidLogin(AuthResponse authResponse) {
        if (loginView != null){
            loginView.setLoginResponse(authResponse);
        }
    }

    @Override
    public void onLoginError() {



    }
}
