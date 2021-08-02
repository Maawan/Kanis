package rw.kanis.shop.Presentation.ui.activities.impl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.Product;
import rw.kanis.shop.Models.ProductDetails;
import rw.kanis.shop.Network.response.AddToCartResponse;
import rw.kanis.shop.Network.response.AddToWishlistResponse;
import rw.kanis.shop.Network.response.AppSettingsResponse;
import rw.kanis.shop.Network.response.AuthResponse;
import rw.kanis.shop.Network.response.CheckWishlistResponse;
import rw.kanis.shop.Network.response.RemoveWishlistResponse;
import rw.kanis.shop.Network.response.VariantResponse;
import rw.kanis.shop.Presentation.presenters.ProductDetailsPresenter;
import rw.kanis.shop.Presentation.ui.activities.ProductDetailsView;
import rw.kanis.shop.Presentation.ui.adapters.BestSellingofSellerAdapter;
import rw.kanis.shop.Presentation.ui.adapters.FeaturedProductAdapter;
import rw.kanis.shop.Presentation.ui.listeners.ProductClickListener;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.Utils.CustomToast;
import rw.kanis.shop.Utils.UserPrefs;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;
import com.bumptech.glide.Glide;
//import com.daimajia.slider.library.SliderLayout;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
//import com.denzcoskun.imageslider.constants.ScaleTypes;



import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;


import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends BaseActivity implements ProductDetailsView, ProductClickListener {
    private String product_name, link, top_selling_link;
   // private SliderLayout sliderLayout;
    private TextView name;
    private RatingBar ratingBar;
    private ImageSlider slider;
    private TextView rating_count, price_range, shortdesc,shortdesc1,shortdesc2,shortdesc3,shortdesc4;
    private ImageView shop_logo, heart_icon;
    private TextView shop_name;
    private RelativeLayout buying_option, specification, reviews, seller_policy, return_policy, support_policy;
    private RecyclerView related_products, top_selling;
    private NestedScrollView product_details;
    private LinearLayout product_buttons;
    private ProgressBar progress_bar;
    private TextView product_unit;
    private Button addTocart, buyNow;
    private ProductDetails productDetails = null;
    private ProgressDialog progressDialog;
    private AuthResponse authResponse;
    private ProductDetailsPresenter productDetailsPresenter;
    private CardView shop_info, image_card;
    private boolean isBuyNow = false;



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
        setContentView(R.layout.activity_product_details);

        product_name = getIntent().getStringExtra("product_name");
        link = getIntent().getStringExtra("link");
        top_selling_link = getIntent().getStringExtra("top_selling");

        initializeActionBar();
        setTitle(product_name);

        initviews();

        progress_bar.setVisibility(View.VISIBLE);
        product_details.setVisibility(View.INVISIBLE);
        product_buttons.setVisibility(View.INVISIBLE);
        image_card.setVisibility(View.GONE);

        productDetailsPresenter = new ProductDetailsPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this);

        productDetailsPresenter.getProductDetails(link);

        seller_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PolicyViewActivity.class);
                intent.putExtra("title", "Seller Policy");
                intent.putExtra("url", "policies/seller");
                startActivity(intent);
            }
        });

        specification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductDescriptionActivity.class);
                intent.putExtra("product_name", productDetails.getName());
                intent.putExtra("description", productDetails.getDescription());
                startActivity(intent);
            }
        });

//        description.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), PolicyViewActivity.class);
//                intent.putExtra("url", "http://shop2.9277955464.com/privacypolicy");
//                startActivity(intent);
//            }
//        });

        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, ProductReviewActivity.class);
                intent.putExtra("url", productDetails.getLinks().getReviews());
                startActivity(intent);
            }
        });

        return_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PolicyViewActivity.class);
                intent.putExtra("title", "Seller Policy");
                intent.putExtra("url", "policies/return");
                startActivity(intent);
            }
        });

        support_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PolicyViewActivity.class);
                intent.putExtra("title", "Seller Policy");
                intent.putExtra("url", "policies/support");
                startActivity(intent);
            }
        });


        buying_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBuyingOptionActivity();
            }
        });

        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAddToCart();
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBuyNow = true;
                processAddToCart();
            }
        });
    }

    private void processAddToCart(){
        if(productDetails != null && (productDetails.getChoiceOptions().size() > 0 || productDetails.getColors().size() > 0)){
            startBuyingOptionActivity();
        }
        else {
            AuthResponse authResponse = new UserPrefs(getApplicationContext()).getAuthPreferenceObjectJson("auth_response");
            if(authResponse != null && authResponse.getUser() != null){
                progressDialog.setMessage("Adding item to your shopping cart. Please wait.");
                progressDialog.show();
                productDetailsPresenter.addToCart(authResponse.getAccessToken(), authResponse.getUser().getId(), productDetails.getId(), null);
            }
            else {
                startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 100);
                finish();
            }
        }
    }

    private void startBuyingOptionActivity(){
        if(productDetails != null && (productDetails.getChoiceOptions().size() > 0 || productDetails.getColors().size() > 0)){
            Intent intent = new Intent(getApplicationContext(), BuyingOptionsActivity.class);
            intent.putExtra("product_details", productDetails);
            startActivity(intent);
        }
        else{
            CustomToast.showToast(this, "This product doesn't have any buying options.", R.color.colorWarning);
        }
    }
    String sd;
    private void initviews(){
        product_details = findViewById(R.id.product_details);
        product_buttons = findViewById(R.id.product_buttons);
        progress_bar = findViewById(R.id.item_progress_bar);
        product_unit = findViewById(R.id.product_unit);
       // sliderLayout = findViewById(R.id.imageSlider);

      slider = findViewById(R.id.image_Slider);



     //   sliderLayout.stopAutoCycle();
        name = findViewById(R.id.product_name);
        ratingBar = findViewById(R.id.product_rating);
        rating_count = findViewById(R.id.product_rating_count);
        price_range = findViewById(R.id.product_price_range);

        shortdesc = findViewById(R.id.shortdesc);
        shortdesc1 = findViewById(R.id.shortdesc1);
        shortdesc2 = findViewById(R.id.shortdesc2);
        shortdesc3 = findViewById(R.id.shortdesc3);
        shortdesc4 = findViewById(R.id.shortdesc4);



        shop_logo = findViewById(R.id.shop_logo);
        heart_icon = findViewById(R.id.heart_icon);
        shop_name = findViewById(R.id.shop_name);

        buying_option = findViewById(R.id.buying_option);

        specification = findViewById(R.id.specification);
        //description = findViewById(R.id.description);
        reviews = findViewById(R.id.reviews);
        seller_policy = findViewById(R.id.seller_policy);
        return_policy = findViewById(R.id.return_policy);
        support_policy = findViewById(R.id.support_policy);
        related_products = findViewById(R.id.related_products);
        top_selling = findViewById(R.id.top_selling);

        addTocart = findViewById(R.id.addToCart);
        buyNow = findViewById(R.id.buyNow);



        progressDialog = new ProgressDialog(this);
        shop_info = findViewById(R.id.shop_info);
        image_card = findViewById(R.id.image_card);
    }

    @Override
    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
        ArrayList<SlideModel> image = new ArrayList<>();
        for (String photo : productDetails.getPhotos()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description("")
                    .image(AppConfig.ASSET_URL + photo)
                    .setScaleType(BaseSliderView.ScaleType.CenterInside);
            //sliderLayout.addSlider(textSliderView);
           image.add(new SlideModel(AppConfig.ASSET_URL + photo, ScaleTypes.CENTER_INSIDE));
            //   image.add(new SlideModel(AppConfig.ASSET_URL.toString(),ScaleTypes.CENTER_INSIDE));
        }
//        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
//        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
      //  slider.setImageList(image, ScaleTypes.CENTER_INSIDE);
        slider.setImageList(image);
        
        product_unit.setText("/ " + productDetails.getUnit());
        image_card.setVisibility(View.VISIBLE);

        name.setText(productDetails.getName());
        shortdesc.setText(productDetails.getShortdesc());
        String sd = shortdesc.getText().toString();
        if(sd.equals("0")){
            shortdesc.setVisibility(View.GONE);
        }

        shortdesc1.setText(productDetails.getShortdesc1());
        String sd1 = shortdesc1.getText().toString();
        if(sd1.equals("0")){
            shortdesc1.setVisibility(View.GONE);
        }

        shortdesc2.setText(productDetails.getShortdesc2());
        String sd2 = shortdesc2.getText().toString();
        if(sd2.equals("0")){
            shortdesc2.setVisibility(View.GONE);
        }

        shortdesc3.setText(productDetails.getShortdesc3());
        String sd3 = shortdesc3.getText().toString();
        if(sd3.equals("0")){
            shortdesc3.setVisibility(View.GONE);
        }

        shortdesc4.setText(productDetails.getShortdesc4());
        String sd4 = shortdesc4.getText().toString();
        if(sd4.equals("0")){
            shortdesc4.setVisibility(View.GONE);
        }
        int bo =  productDetails.getChoiceOptions().size();
        int ab = productDetails.getColors().size();
        if(bo==0&& ab==0)
        {
            try{

                    buying_option.setVisibility(View.GONE);


            }
            catch (Exception e){
                Log.e("failed","d");
            }
        }
        int i = productDetails.getCurrentStock();
        if(i<=0){

            addTocart.setText("OUT OF STOCK");
            addTocart.setEnabled(false);
            addTocart.setBackgroundColor(Color.RED);
            buyNow.setVisibility(View.GONE);

        }
        else
        {
            addTocart.setText("ADD TO CART");

        }

        ratingBar.setRating(productDetails.getRating());
        rating_count.setText("("+productDetails.getRatingCount()+")");



        authResponse = new UserPrefs(getApplicationContext()).getAuthPreferenceObjectJson("auth_response");
        if(authResponse != null && authResponse.getUser() != null){
            productDetailsPresenter.checkOnWishlist(authResponse.getAccessToken(), authResponse.getUser().getId(), productDetails.getId());
        }
        else {
            heart_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 100);
                    finish();
                }
            });
        }

        if (!productDetails.getAddedBy().equals("admin")){
            Glide.with(this).load(AppConfig.ASSET_URL + productDetails.getUser().getShopLogo()).into(shop_logo);
            shop_name.setText(productDetails.getUser().getShopName());
            shop_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProductDetailsActivity.this, SellerShopActivity.class);
                    intent.putExtra("shop_name", productDetails.getUser().getShopName());
                    intent.putExtra("shop_link", productDetails.getUser().getShopLink());
                    startActivity(intent);
                }
            });
        }
        else {
            AppSettingsResponse appSettingsResponse = new UserPrefs(this).getAppSettingsPreferenceObjectJson("app_settings_response");
            if (appSettingsResponse != null){
                Glide.with(this).load(AppConfig.ASSET_URL + appSettingsResponse.getData().get(0).getLogo()).into(shop_logo);
            }
        }

        if(productDetails.getPriceLower().equals(productDetails.getPriceHigher())){
            price_range.setText(AppConfig.convertPrice(this, productDetails.getPriceLower()));
        }
        else {
            price_range.setText(AppConfig.convertPrice(this, productDetails.getPriceLower())+"-"+AppConfig.convertPrice(this, productDetails.getPriceHigher()));
        }

        progress_bar.setVisibility(View.GONE);
        product_details.setVisibility(View.VISIBLE);
        product_buttons.setVisibility(View.VISIBLE);




        new ProductDetailsPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).getRelatedProducts(productDetails.getLinks().getRelated());

        new ProductDetailsPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).getTopSellingProducts(top_selling_link);
    }

    @Override
    public void setRelatedProducts(List<Product> relatedProducts) {
        RecyclerView recyclerView = findViewById(R.id.related_products);
        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        FeaturedProductAdapter adapter = new FeaturedProductAdapter(this, relatedProducts, this);
        recyclerView.addItemDecoration( new LayoutMarginDecoration( 1,  AppConfig.convertDpToPx(getApplicationContext(), 10)) );
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setTopSellingProducts(List<Product> topSellingProducts) {
        RecyclerView recyclerView = findViewById(R.id.top_selling);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        BestSellingofSellerAdapter adapter = new BestSellingofSellerAdapter(this, topSellingProducts, this);
        recyclerView.addItemDecoration( new LayoutMarginDecoration( 1,  AppConfig.convertDpToPx(getApplicationContext(), 10)) );
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setAddToCartMessage(AddToCartResponse addToCartResponse) {
        progressDialog.dismiss();
        if (isBuyNow){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("message", addToCartResponse.getMessage());
            intent.putExtra("position", "cart");
            startActivity(intent);
            finish();
        }
        else {
            CustomToast.showToast(this, addToCartResponse.getMessage(), 1);
        }
    }

    @Override
    public void setAddToWishlistMessage(AddToWishlistResponse addToWishlistMessage) {
        CustomToast.showToast(this, addToWishlistMessage.getMessage(), R.color.colorSuccess);
        heart_icon.setImageResource(R.drawable.ic_heart_filled);
        productDetailsPresenter.checkOnWishlist(authResponse.getAccessToken(), authResponse.getUser().getId(), productDetails.getId());
    }

    @Override
    public void onCheckWishlist(CheckWishlistResponse checkWishlistResponse) {
        if(checkWishlistResponse.getIsInWishlist()){
            heart_icon.setImageResource(R.drawable.ic_heart_filled);
            heart_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productDetailsPresenter.removeFromWishlist(authResponse.getAccessToken(), checkWishlistResponse.getWishlistId());
                }
            });
        }
        else{
            heart_icon.setImageResource(R.drawable.ic_heart);
            heart_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productDetailsPresenter.addToWishlist(authResponse.getAccessToken(), authResponse.getUser().getId(), productDetails.getId());
                }
            });
        }
    }

    @Override
    public void onRemoveFromWishlist(RemoveWishlistResponse removeWishlistResponse) {
        heart_icon.setImageResource(R.drawable.ic_heart);
        CustomToast.showToast(this, removeWishlistResponse.getMessage(), R.color.colorSuccess);
        productDetailsPresenter.checkOnWishlist(authResponse.getAccessToken(), authResponse.getUser().getId(), productDetails.getId());
    }

    @Override
    public void onProductItemClick(Product product) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("product_name", product.getName());
        intent.putExtra("link", product.getLinks().getDetails());
        intent.putExtra("top_selling", product.getLinks().getRelated());
        startActivity(intent);
    }
}
