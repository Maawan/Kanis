package rw.kanis.shop.Presentation.ui.activities.impl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.PurchaseHistory;
import rw.kanis.shop.Network.response.AuthResponse;
import rw.kanis.shop.Presentation.presenters.PurchaseHistoryPresenter;
import rw.kanis.shop.Presentation.ui.activities.PurchaseHistoryView;
import rw.kanis.shop.Presentation.ui.adapters.PurchaseHistoryAdapter;
import rw.kanis.shop.Presentation.ui.listeners.PurchaseHistoryCliclListener;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.Utils.RecyclerViewMargin;
import rw.kanis.shop.Utils.UserPrefs;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;

import java.util.List;

public class PurchaseHistoryActivity extends BaseActivity implements PurchaseHistoryView, PurchaseHistoryCliclListener {
    private AuthResponse authResponse;
    private PurchaseHistoryPresenter purchaseHistoryPresenter;
    private ProgressBar progressBar;
    private TextView purchase_history_empty_text;

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
        setContentView(R.layout.activity_purchase_history);

        initializeActionBar();
        setTitle("Purchase History");

        progressBar = findViewById(R.id.item_progress_bar);
        purchase_history_empty_text = findViewById(R.id.purchase_history_empty_text);

        purchaseHistoryPresenter = new PurchaseHistoryPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this);

        authResponse = new UserPrefs(getApplicationContext()).getAuthPreferenceObjectJson("auth_response");
        if(authResponse != null && authResponse.getUser() != null){
            progressBar.setVisibility(View.VISIBLE);
            purchaseHistoryPresenter.getPurchaseHistoryItems(authResponse.getUser().getId(), authResponse.getAccessToken());
        }
    }

    @Override
    public void onPurchaseHistoryLoaded(List<PurchaseHistory> purchaseHistoryList) {
        progressBar.setVisibility(View.GONE);
        if (purchaseHistoryList.size() > 0){
            RecyclerView recyclerView = findViewById(R.id.purchase_history_list);
            GridLayoutManager horizontalLayoutManager
                    = new GridLayoutManager(getApplicationContext(), 1);
            recyclerView.setLayoutManager(horizontalLayoutManager);
            PurchaseHistoryAdapter adapter = new PurchaseHistoryAdapter(getApplicationContext(), purchaseHistoryList, this);
            RecyclerViewMargin decoration = new RecyclerViewMargin(AppConfig.convertDpToPx(this,10), 1);
            recyclerView.addItemDecoration(decoration);
            recyclerView.setAdapter(adapter);
        }
        else {
            purchase_history_empty_text.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPurchaseHistoryItemClick(PurchaseHistory purchaseHistory) {
        Intent intent = new Intent(this, PurchaseHistoryDetailsActivity.class);
        intent.putExtra("purchase_history", purchaseHistory);
        startActivity(intent);
    }
}
