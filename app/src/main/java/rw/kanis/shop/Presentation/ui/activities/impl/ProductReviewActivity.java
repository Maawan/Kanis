package rw.kanis.shop.Presentation.ui.activities.impl;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.Review;
import rw.kanis.shop.Presentation.presenters.ProductReviewPresenter;
import rw.kanis.shop.Presentation.ui.activities.ProductReviewView;
import rw.kanis.shop.Presentation.ui.adapters.ProductReviewAdapter;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;

import java.util.List;

public class ProductReviewActivity extends BaseActivity implements ProductReviewView {
    private String url;
    private ProgressBar progressBar;
    private TextView product_reviews_empty_text;

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
        setContentView(R.layout.activity_product_review);

        url = getIntent().getStringExtra("url");

        initializeActionBar();
        setTitle("Product Reviews");

        progressBar = findViewById(R.id.item_progress_bar);
        product_reviews_empty_text = findViewById(R.id.product_reviews_empty_text);

        progressBar.setVisibility(View.VISIBLE);
        new ProductReviewPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).sendUpdateProfileRequest(url);
    }

    @Override
    public void onReviewsLoaded(List<Review> reviews) {
        progressBar.setVisibility(View.GONE);
        if (reviews.size() > 0){
            RecyclerView recyclerView = findViewById(R.id.product_reviews);
            LinearLayoutManager horizontalLayoutManager
                    = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManager);
            ProductReviewAdapter adapter = new ProductReviewAdapter(this, reviews);
            recyclerView.setAdapter(adapter);
        }
        else {
            product_reviews_empty_text.setVisibility(View.VISIBLE);
        }
    }
}
