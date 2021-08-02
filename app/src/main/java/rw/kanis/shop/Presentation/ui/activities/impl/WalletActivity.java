package rw.kanis.shop.Presentation.ui.activities.impl;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import rw.kanis.shop.Models.Wallet;
import rw.kanis.shop.Network.response.AuthResponse;
import rw.kanis.shop.Presentation.presenters.WalletPresenter;
import rw.kanis.shop.Presentation.ui.activities.WalletView;
import rw.kanis.shop.Presentation.ui.adapters.WalletHistoryAdapter;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.Utils.RecyclerViewMargin;
import rw.kanis.shop.Utils.UserPrefs;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;

import java.util.List;

public class WalletActivity extends BaseActivity implements WalletView {
    private AuthResponse authResponse;
    private TextView tv_balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        if (statusBarHeight <= AppConfig.convertDpToPixel(24))
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_wallet);

        initializeActionBar();
        setTitle("My Wallet");
        initviews();

        authResponse = new UserPrefs(getApplicationContext()).getAuthPreferenceObjectJson("auth_response");
        if(authResponse != null && authResponse.getUser() != null){
            new WalletPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).getWalletBalance(authResponse.getUser().getId(), authResponse.getAccessToken());
        }
        else {
            //startActivityForResult(new Intent(getActivity(), LoginActivity.class), 100);
            //getActivity().finish();
        }
    }

    private void initviews(){
        tv_balance = findViewById(R.id.balance);
    }

    @Override
    public void setWalletBalance(Double balance) {
        tv_balance.setText(AppConfig.convertPrice(getApplicationContext(), balance));
        new WalletPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).getWalletHistory(authResponse.getUser().getId(), authResponse.getAccessToken());
    }

    @Override
    public void setWalletHistory(List<Wallet> walletList) {
        RecyclerView recyclerView = findViewById(R.id.rv_wallet_history);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        RecyclerViewMargin decoration = new RecyclerViewMargin(AppConfig.convertDpToPx(this,10), 1);
        recyclerView.addItemDecoration(decoration);
        WalletHistoryAdapter adapter = new WalletHistoryAdapter(getApplicationContext(), walletList);
        recyclerView.setAdapter(adapter);
    }
}