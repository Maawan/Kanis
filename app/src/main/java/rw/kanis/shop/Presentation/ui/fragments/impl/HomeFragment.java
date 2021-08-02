package rw.kanis.shop.Presentation.ui.fragments.impl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import rw.kanis.shop.Models.AuctionProduct;
import rw.kanis.shop.Models.Banner;
import rw.kanis.shop.Models.BannerData;
import rw.kanis.shop.Models.BannerDataLinks;
import rw.kanis.shop.Models.Brand;
import rw.kanis.shop.Models.Category;
import rw.kanis.shop.Models.FlashDeal;
import rw.kanis.shop.Models.Product;
import rw.kanis.shop.Models.SliderImage;
import rw.kanis.shop.Network.response.AppSettingsResponse;
import rw.kanis.shop.Network.response.AuctionBidResponse;
import rw.kanis.shop.Network.response.AuthResponse;
import rw.kanis.shop.Presentation.presenters.HomePresenter;
import rw.kanis.shop.Presentation.ui.activities.impl.LoginActivity;
import rw.kanis.shop.Presentation.ui.activities.impl.ProductDetailsActivity;
import rw.kanis.shop.Presentation.ui.activities.impl.ProductListingActivity;
import rw.kanis.shop.Presentation.ui.adapters.AuctionProductAdapter;
import rw.kanis.shop.Presentation.ui.adapters.BannerCategoryAdapter;
import rw.kanis.shop.Presentation.ui.adapters.BestSellingAdapter;
import rw.kanis.shop.Presentation.ui.adapters.BrandAdapter;
import rw.kanis.shop.Presentation.ui.adapters.FeaturedProductAdapter;
import rw.kanis.shop.Presentation.ui.adapters.HomeCategoryAdapter;
import rw.kanis.shop.Presentation.ui.adapters.TodaysDealAdapter;
import rw.kanis.shop.Presentation.ui.adapters.TopCategoryAdapter;
import rw.kanis.shop.Presentation.ui.fragments.HomeView;
import rw.kanis.shop.Presentation.ui.fragments.impl.CartFragment;
import rw.kanis.shop.Presentation.ui.listeners.AuctionClickListener;
import rw.kanis.shop.Presentation.ui.listeners.BrandClickListener;
import rw.kanis.shop.Presentation.ui.listeners.CartItemListener;
import rw.kanis.shop.Presentation.ui.listeners.CategoryClickListener;
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
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.glide.slider.library.SliderLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.kingfisher.easyviewindicator.AnyViewIndicator;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.iwgang.countdownview.CountdownView;

public class HomeFragment extends Fragment implements HomeView, CategoryClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, ProductClickListener, BrandClickListener, AuctionClickListener {
    private View v;
    private ListView bannercategoryListView;
    private RecyclerView recyclerViewForBannerCategories;
    private BannerCategoryAdapter bannerCategoryAdapter;
    List<SliderImage> sliderImages;
   // private SliderLayout sliderLayout;
   // private SliderLayout sliderLayout;
    private HomePresenter homePresenter;
    private ImageSlider sliderLayout;
    private AnyViewIndicator gridIndicator;
    private RelativeLayout auction_product_section, todays_deal_section, flash_deal_section;
    private AuthResponse authResponse;
    private ProgressDialog progressDialog;
    private BottomSheetDialog dialog;
    private RecyclerView auction_recyclerView;
    private List<AuctionProduct> mAuctionProducts = new ArrayList<>();
    private  AuctionProductAdapter adapter;
    private TextView flash_deals_text;
    private CountdownView mCvCountdownView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, null);


        auction_product_section = v.findViewById(R.id.auction_product_section);
        todays_deal_section = v.findViewById(R.id.todays_deal_section);
        flash_deal_section = v.findViewById(R.id.flash_deal_section);
        flash_deals_text = v.findViewById(R.id.flash_deals_text);
        mCvCountdownView = (CountdownView) v.findViewById(R.id.countdown);

        auction_product_section.setVisibility(View.GONE);
        todays_deal_section.setVisibility(View.GONE);
        flash_deal_section.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(getContext());
        auction_recyclerView = v.findViewById(R.id.auction_products);
        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(getActivity(), 1, RecyclerView.VERTICAL, false);
        auction_recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new AuctionProductAdapter(getActivity(), mAuctionProducts, this);
        auction_recyclerView.addItemDecoration(new LayoutMarginDecoration( 1,  AppConfig.convertDpToPx(getContext(), 10)));
        auction_recyclerView.setAdapter(adapter);


        sliderLayout = v.findViewById(R.id.image_slider);
        sliderLayout.setVisibility(View.GONE);
//        sliderLayout.stopAutoCycle();

        homePresenter = new HomePresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this);
        homePresenter.getAppSettings();
        homePresenter.getSliderImages();
        homePresenter.getTopCategories();
        homePresenter.getBanners();




        return v;
    }

    @Override
    public void onAppSettingsLoaded(AppSettingsResponse appSettingsResponse) {
        UserPrefs userPrefs = new UserPrefs(getContext());
        userPrefs.setAppSettingsPreferenceObject(appSettingsResponse, "app_settings_response");

        homePresenter.getTodaysDeal();
        homePresenter.getFlashDeal();
        homePresenter.getBestSelling();
        homePresenter.getFeaturedProducts();
        homePresenter.getPopularbrands();
        homePresenter.getAuctionProducts();
        homePresenter.getBannerCategories();
    }

    @Override
    public void setSliderImages(List<SliderImage> sliderImages) {
        this.sliderImages = sliderImages;
        ArrayList<SlideModel> images = new ArrayList<SlideModel>();
        for (SliderImage sliderImage : sliderImages) {

//            TextSliderView textSliderView = new TextSliderView(getContext());
//            textSliderView
//                    .description("")
//                    .image(AppConfig.ASSET_URL + sliderImage.getPhoto())
//                    .setScaleType(BaseSliderView.ScaleType.Fit);
            //Toast.makeText(getContext(), AppConfig.ASSET_URL + sliderImage.getPhoto(), Toast.LENGTH_SHORT).show();
            images.add(new SlideModel(AppConfig.ASSET_URL + sliderImage.getPhoto(), ScaleTypes.FIT));
            //sliderLayout.addSlider();
            //Log.d("Sliders", AppConfig.ASSET_URL + sliderImage.getPhoto());
        }
        if(images != null) {
            sliderLayout.setImageList(images);
        }

        sliderLayout.setVisibility(View.VISIBLE);
        //Glide.with(getContext()).load(AppConfig.ASSET_URL + sliderImages.get(0).getPhoto()).transform(new BlurTransformation(75, 1)).into(imageSliderShadow);
    }

    @Override
    public void setHomeCategories(List<Category> categories) {
//        RecyclerView recyclerView = v.findViewById(R.id.home_categories);
//        GridLayoutManager horizontalLayoutManager
//                = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(horizontalLayoutManager);
//        HomeCategoryAdapter adapter = new HomeCategoryAdapter(getActivity(), categories, HomeFragment.this);
//        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setTodaysDeal(List<Product> products) {
        if (products.size() > 0){
            RecyclerView recyclerView = v.findViewById(R.id.todays_deals);
            GridLayoutManager horizontalLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManager);
            FeaturedProductAdapter adapter = new FeaturedProductAdapter(getActivity(), products, this);
            recyclerView.addItemDecoration( new LayoutMarginDecoration( 2,  AppConfig.convertDpToPx(getContext(), 10)) );
            recyclerView.setAdapter(adapter);

            todays_deal_section.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setFlashDeal(FlashDeal flashDeal) {
        if (flashDeal.getProducts().getData().size() > 0){
            flash_deals_text.setText(flashDeal.getTitle());

            mCvCountdownView.start((flashDeal.getEndDate()*1000) - System.currentTimeMillis());

            RecyclerView recyclerView = v.findViewById(R.id.flash_deals);
            GridLayoutManager horizontalLayoutManager
                    = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManager);
            TodaysDealAdapter adapter = new TodaysDealAdapter(getActivity(), flashDeal.getProducts().getData(), this);
            recyclerView.addItemDecoration( new LayoutMarginDecoration( 2,  AppConfig.convertDpToPx(getContext(), 10)) );
            recyclerView.setAdapter(adapter);

            flash_deal_section.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setBanners(List<Banner> banners) {
        ImageView banner_1 = v.findViewById(R.id.banner_1);
        ImageView banner_2 = v.findViewById(R.id.banner_2);
        ImageView banner_3 = v.findViewById(R.id.banner_3);

        Glide.with(getContext()).load(AppConfig.ASSET_URL + banners.get(0).getPhoto()).into(banner_1);
        Glide.with(getContext()).load(AppConfig.ASSET_URL + banners.get(1).getPhoto()).into(banner_2);
        Glide.with(getContext()).load(AppConfig.ASSET_URL + banners.get(2).getPhoto()).into(banner_3);
    }

    @Override
    public void setBestSelling(final List<Product> products) {
        RecyclerView recyclerView = v.findViewById(R.id.best_selling);
        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(getActivity(), 1, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        //recyclerView.addItemDecoration(new CirclePagerIndicatorDecoration());
        BestSellingAdapter adapter = new BestSellingAdapter(getActivity(), products, this);
        recyclerView.addItemDecoration( new LayoutMarginDecoration( 1,  AppConfig.convertDpToPx(getContext(), 10)) );
        recyclerView.setAdapter(adapter);

        //gridIndicator.setItemCount((int) Math.ceil((float)products.size() / (float)3));
        //gridIndicator.setCurrentPosition(0);
        //LinearSnapHelper gridLayoutSnapHelper = new LinearSnapHelper();
        //gridLayoutSnapHelper.attachToRecyclerView(recyclerView);

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                switch (newState) {
//                    case RecyclerView.SCROLL_STATE_IDLE:
//                        int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
//                        gridIndicator.setCurrentPosition((int) Math.ceil((position) / 3));
//                        break;
//                }
//            }
//        });
    }

    @Override
    public void setFeaturedProducts(List<Product> products) {
        RecyclerView recyclerView = v.findViewById(R.id.featured_products);
        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        FeaturedProductAdapter adapter = new FeaturedProductAdapter(getActivity(), products, this);
        recyclerView.addItemDecoration( new LayoutMarginDecoration( 2,  AppConfig.convertDpToPx(getContext(), 10)) );
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setTopCategories(List<Category> categories) {
        RecyclerView recyclerView = v.findViewById(R.id.top_categories);
        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        TopCategoryAdapter adapter = new TopCategoryAdapter(getActivity(), categories, HomeFragment.this);
        recyclerView.addItemDecoration( new LayoutMarginDecoration( 1,  AppConfig.convertDpToPx(getContext(), 10)) );


        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPopularBrands(List<Brand> brands) {
        RecyclerView recyclerView = v.findViewById(R.id.popular_brads);
        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        BrandAdapter adapter = new BrandAdapter(getActivity(), brands, this);
        recyclerView.addItemDecoration( new LayoutMarginDecoration( 2,  AppConfig.convertDpToPx(getContext(), 10)) );
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setAuctionProducts(List<AuctionProduct> auctionProducts) {
        mAuctionProducts.clear();
        mAuctionProducts.addAll(auctionProducts);
        if (auctionProducts.size() > 0){
            auction_product_section.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
        else {
            auction_product_section.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Log.d("SliderPosition", String.valueOf(position));
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("SliderPosition", String.valueOf(position));
        //Glide.with(getContext()).load(AppConfig.ASSET_URL + sliderImages.get(position).getPhoto()).transform(new BlurTransformation(75, 1)).into(imageSliderShadow);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Log.d("SliderPosition", String.valueOf(state));
    }

    @Override
    public void onCategoryItemClick(Category category) {
        Intent intent = new Intent(getContext(), ProductListingActivity.class);
        intent.putExtra("title", category.getName());
        intent.putExtra("url", category.getLinks().getProducts());
        startActivity(intent);
    }

    @Override
    public void onProductItemClick(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
        intent.putExtra("product_name", product.getName());
        intent.putExtra("link", product.getLinks().getDetails());
        intent.putExtra("top_selling", product.getLinks().getRelated());
        startActivity(intent);
       // Toast.makeText(getContext(), "Link ---> " +  product.getLinks().getDetails(), Toast.LENGTH_LONG).show();
        Log.e("Link ", product.getLinks().getDetails());

    }

    @Override
    public void onBrandItemClick(Brand brand) {
        Intent intent = new Intent(getContext(), ProductListingActivity.class);
        intent.putExtra("title", brand.getName());
        intent.putExtra("url", brand.getLinks().getProducts());
        startActivity(intent);
    }

    @Override
    public void onAuctionItemClick(AuctionProduct auctionProduct) {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog_auction, null);
        ImageView image = view.findViewById(R.id.product_image);
        TextView current_bid_amount = view.findViewById(R.id.current_bid_amount);
        TextView total_bids = view.findViewById(R.id.total_bids);
        TextView name = view.findViewById(R.id.product_name);
        EditText bid_amount = view.findViewById(R.id.bid_amount);
        Button place_bid = view.findViewById(R.id.place_bid);

        Glide.with(getContext()).load(AppConfig.ASSET_URL + auctionProduct.getImage()).into(image);
        current_bid_amount.setText(AppConfig.convertPrice(getContext(), auctionProduct.getCurrentPrice()));
        total_bids.setText(auctionProduct.getBidsCount()+" Bids");
        name.setText(auctionProduct.getName());


        dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(view);
        dialog.show();

        place_bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double amount = (bid_amount.getText().length() > 0) ? Double.valueOf(bid_amount.getText().toString()) : 0.0;
                if (amount > auctionProduct.getCurrentPrice()){
                    authResponse = new UserPrefs(getActivity()).getAuthPreferenceObjectJson("auth_response");
                    if(authResponse != null && authResponse.getUser() != null){
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("auction_product_id", auctionProduct.getId());
                        jsonObject.addProperty("user_id", authResponse.getUser().getId());
                        jsonObject.addProperty("amount", amount);

                        dialog.hide();
                        progressDialog.setMessage("Your bid is being submitted. Please wait.");
                        progressDialog.show();
                        homePresenter.submitBid(jsonObject, authResponse.getAccessToken());
                    }
                    else {
                        startActivityForResult(new Intent(getActivity(), LoginActivity.class), 100);
                    }
                }
                else {
                    CustomToast.showToast(getActivity(), "Your bidding amount must be greater than the current bid", R.color.colorWarning);
                    bid_amount.requestFocus();
                }
            }
        });
    }

    @Override
    public void onAuctionBidSubmitted(AuctionBidResponse auctionBidResponse) {
        progressDialog.dismiss();
        CustomToast.showToast(getActivity(), auctionBidResponse.getMessage(), R.color.colorSuccess);
        homePresenter.getAuctionProducts();
    }

    @Override
    public void setBannerCategories(List<BannerData> list) {
        if(list == null){
           // Toast.makeText(getContext(), "List is NULL", Toast.LENGTH_SHORT).show();
        }else {
         //   Toast.makeText(getContext(), "Size --->>>> " + list.size(), Toast.LENGTH_SHORT).show();
            for(int i = 0 ; i < list.size(); i++){
           //     Toast.makeText(getContext(), "Size of links --> " + list.get(i).getSlider_imgs().size(), Toast.LENGTH_SHORT).show();
            }


            recyclerViewForBannerCategories = v.findViewById(R.id.recyclerForBannerCategories);
            bannerCategoryAdapter = new BannerCategoryAdapter(getContext() , list);
            recyclerViewForBannerCategories.setAdapter(bannerCategoryAdapter);
            recyclerViewForBannerCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        }
    }

    @Override
    public void setBannerCategoriesError(Throwable t) {
//        Toast.makeText(getContext(), "Error " + t + " Message " + t.getMessage(), Toast.LENGTH_SHORT).show();
        Log.e("Error",t.getMessage());
    }
}