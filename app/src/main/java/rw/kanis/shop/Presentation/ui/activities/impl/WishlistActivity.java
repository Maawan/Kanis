package rw.kanis.shop.Presentation.ui.activities.impl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.WishlistModel;
import rw.kanis.shop.Models.WishlistProduct;
import rw.kanis.shop.Network.response.AuthResponse;
import rw.kanis.shop.Presentation.presenters.WishlistPresenter;
import rw.kanis.shop.Presentation.ui.activities.WishlistView;
import rw.kanis.shop.Presentation.ui.adapters.WishlistAdapter;
import rw.kanis.shop.Presentation.ui.listeners.WishlistProductClickListener;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.Utils.RecyclerViewMargin;
import rw.kanis.shop.Utils.UserPrefs;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;

import java.util.List;

public class WishlistActivity extends BaseActivity implements WishlistView, WishlistProductClickListener {
    private AuthResponse authResponse;
    private WishlistPresenter wishlistPresenter;
    private ProgressBar progressBar;
    private TextView wishlist_empty_text;

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
        setContentView(R.layout.activity_wishlist);

        initializeActionBar();
        setTitle("My Wishlist");

        progressBar = findViewById(R.id.item_progress_bar);
        wishlist_empty_text = findViewById(R.id.wishlist_empty_text);

        wishlistPresenter = new WishlistPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this);

        authResponse = new UserPrefs(getApplicationContext()).getAuthPreferenceObjectJson("auth_response");
        if(authResponse != null && authResponse.getUser() != null){
            progressBar.setVisibility(View.VISIBLE);
            wishlistPresenter.getWishlistItems(authResponse.getUser().getId(), authResponse.getAccessToken());
        }
        else {
            //startActivityForResult(new Intent(getActivity(), LoginActivity.class), 100);
            //getActivity().finish();
        }
    }

    @Override
    public void setWishlistProducts(List<WishlistModel> wishlistModels) {
        progressBar.setVisibility(View.GONE);
        if (wishlistModels.size() > 0){
            WishlistAdapter adapter = new WishlistAdapter(getApplicationContext(), wishlistModels, this);
            RecyclerView recyclerView = findViewById(R.id.product_list);
            LinearLayoutManager horizontalLayoutManager
                    = new LinearLayoutManager(WishlistActivity.this, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManager);
            RecyclerViewMargin decoration = new RecyclerViewMargin(AppConfig.convertDpToPx(this,10), 1);
            recyclerView.addItemDecoration(decoration);
            recyclerView.setAdapter(adapter);
        }
        else {
            wishlist_empty_text.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onProductItemClick(WishlistProduct product) {
        Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
        intent.putExtra("product_name", product.getName());
        intent.putExtra("link", product.getLinks().getDetails());
        intent.putExtra("top_selling", product.getLinks().getRelated());
        startActivity(intent);
    }
}
