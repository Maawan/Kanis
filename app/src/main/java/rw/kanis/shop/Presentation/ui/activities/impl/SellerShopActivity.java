package rw.kanis.shop.Presentation.ui.activities.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.Product;
import rw.kanis.shop.Models.Shop;
import rw.kanis.shop.Presentation.presenters.ShopPresenter;
import rw.kanis.shop.Presentation.ui.activities.SellerShopView;
import rw.kanis.shop.Presentation.ui.adapters.BestSellingAdapter;
import rw.kanis.shop.Presentation.ui.adapters.FeaturedProductAdapter;
import rw.kanis.shop.Presentation.ui.adapters.ProductListingAdapter;
import rw.kanis.shop.Presentation.ui.listeners.ProductClickListener;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.Utils.RecyclerViewMargin;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class SellerShopActivity extends BaseActivity implements SellerShopView, ProductClickListener {
    private String shop_name, shop_link;
    //private SliderLayout sliderLayout;
    private ShopPresenter shopPresenter;
    private ProgressBar progress_bar;
    private ImageSlider slider;
    private TextView featured,top_selling, new_arrival;
    private NestedScrollView shop_details;
    private Button btn_seller_products;

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
        setContentView(R.layout.activity_seller_shop);

        shop_name = getIntent().getStringExtra("shop_name");
        shop_link = getIntent().getStringExtra("shop_link");

        initializeActionBar();
        setTitle(shop_name);
        initviews();

        shop_details.setVisibility(View.GONE);

        progress_bar.setVisibility(View.VISIBLE);
        featured.setVisibility(View.GONE);
        top_selling.setVisibility(View.GONE);
        new_arrival.setVisibility(View.GONE);

        shopPresenter = new ShopPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this);
        shopPresenter.getShopDetails(shop_link);
    }

    private void initviews(){
        //sliderLayout = findViewById(R.id.imageSlider);
        slider = findViewById(R.id.image_slider);
        //sliderLayout.stopAutoCycle();
        progress_bar = findViewById(R.id.item_progress_bar);
        featured = findViewById(R.id.featured_products_text);
        top_selling = findViewById(R.id.top_selling_products_text);
        new_arrival = findViewById(R.id.new_products_text);
        shop_details = findViewById(R.id.shop_details);
        btn_seller_products = findViewById(R.id.btnSellerProducts);
    }

    @Override
    public void onShopDetailsLoaded(Shop shop) {
        ArrayList<SlideModel> image = new ArrayList<>();
        for (String photo : shop.getSliders()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description("")
                    .image(AppConfig.ASSET_URL + photo)
                    .setScaleType(BaseSliderView.ScaleType.CenterInside);
            //sliderLayout.addSlider(textSliderView);
            image.add(new SlideModel(AppConfig.ASSET_URL + photo, ScaleTypes.CENTER_INSIDE));
        }
//        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
//        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setImageList(image);

        btn_seller_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerShopActivity.this, ProductListingActivity.class);
                intent.putExtra("title", shop.getName());
                intent.putExtra("url", shop.getLinks().getAll());
                startActivity(intent);
            }
        });

        new ShopPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).getFeaturedProducts(shop.getLinks().getFeatured());

        new ShopPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).getTopSellingProducts(shop.getLinks().getTop());

        new ShopPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).getNewProducts(shop.getLinks().getNew());
    }

    @Override
    public void setFeaturedProducts(List<Product> products) {
        featured.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = findViewById(R.id.featured_products);
        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        FeaturedProductAdapter adapter = new FeaturedProductAdapter(this, products, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setTopSellingProducts(List<Product> products) {
        top_selling.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = findViewById(R.id.top_selling);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        BestSellingAdapter adapter = new BestSellingAdapter(this, products, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setNewProducts(List<Product> products) {
        new_arrival.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = findViewById(R.id.new_products);
        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        //adapter.setClickListener(this);
        RecyclerViewMargin decoration = new RecyclerViewMargin(convertDpToPx(this,10), 2);
        recyclerView.addItemDecoration(decoration);
        ProductListingAdapter adapter = new ProductListingAdapter(getApplicationContext(), products, this);
        recyclerView.setAdapter(adapter);

        progress_bar.setVisibility(View.GONE);
        shop_details.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProductItemClick(Product product) {

    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value rw dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public int convertDpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
