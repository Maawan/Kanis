package rw.kanis.shop.Presentation.ui.activities.impl;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import rw.kanis.shop.Models.CartModel;
import rw.kanis.shop.Network.response.AppSettingsResponse;
import rw.kanis.shop.Presentation.ui.activities.OnSearchButtonClick;
import rw.kanis.shop.Presentation.ui.fragments.impl.AccountFragment;
import rw.kanis.shop.Presentation.ui.fragments.impl.CartFragment;
import rw.kanis.shop.Presentation.ui.fragments.impl.CategoriesFragment;
import rw.kanis.shop.Presentation.ui.fragments.impl.HomeFragment;
import rw.kanis.shop.Presentation.ui.fragments.impl.ProductSearchFragment;
import rw.kanis.shop.Presentation.ui.fragments.impl.groceryFragment;
import rw.kanis.shop.Presentation.ui.listeners.CartItemListener;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.Utils.CustomToast;
import rw.kanis.shop.Utils.UserPrefs;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;
import rw.kanis.shop.domain.interactors.AppSettingsInteractor;
import rw.kanis.shop.domain.interactors.impl.AppSettingsInteractorImpl;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends AppCompatActivity implements AppSettingsInteractor.CallBack , OnSearchButtonClick {

    final Fragment homeFragment = new HomeFragment();
    int searchValue = 0;
    final Fragment categoriesFragment = new CategoriesFragment();
    private Fragment cartFragment = new CartFragment();
    private Fragment accountFragment = new AccountFragment();
    private Fragment searchFragment = new ProductSearchFragment();
    private Fragment groceryFragment = new groceryFragment();
    final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = homeFragment;
    public static BottomNavigationView navView;
    private ImageButton cart, search;
    private TextView title;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    title.setText(R.string.app_name);
                    loadFragment(homeFragment);
                    break;
                case R.id.navigation_categories:
                    title.setText("Categories");
                    loadFragment(categoriesFragment);
                    break;
                case R.id.groceryTab:
                    title.setText("Grocery");
                    loadFragment(groceryFragment);
                    break;

//                case R.id.navigation_search:
//                    title.setText("Search");
//                    loadFragment(searchFragment);
//                    break;
                case R.id.navigation_cart:
                    title.setText("Shopping Cart");
                    loadFragment(cartFragment);
                    break;
//                case R.id.grocery:
//                    title.setText("Groceries");
//                    break;

                case R.id.navigation_account:
                    title.setText("My Account");
                    loadFragment(accountFragment);
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        setContentView(R.layout.activity_main);
//        this.getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        getSupportActionBar().setElevation(0);

        View view =getSupportActionBar().getCustomView();

        Intent intent = getIntent();
        searchValue = intent.getIntExtra("Search",0);

        cart = view.findViewById(R.id.action_bar_cart);
        search = view.findViewById(R.id.action_bar_search);
        title = view.findViewById(R.id.nav_title);

        title.setText(R.string.app_name);
        if(searchValue == 1){
            loadFragment(searchFragment);
        }

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navView.setSelectedItemId(R.id.navigation_cart);
                loadFragment(cartFragment);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navView.setSelectedItemId(R.id.navigation_search);
                loadFragment(searchFragment);
            }
        });

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3); // number of menu from left
        new QBadgeView(this).bindTarget(v).setBadgeText(String.valueOf(0)).setShowShadow(false);

        fm.beginTransaction().add(R.id.fragment_container, categoriesFragment, "categories").hide(categoriesFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, searchFragment, "search").hide(searchFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, cartFragment, "cart").hide(cartFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, accountFragment, "account").hide(accountFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container , groceryFragment , "Grocery").hide(groceryFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, homeFragment, "home").commit();


        loadFragment(homeFragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            search.setVisibility(View.VISIBLE);
            cart.setVisibility(View.VISIBLE);
            if(fragment == searchFragment){
                search.setVisibility(View.GONE);
            }
            else if(fragment == cartFragment){
                cart.setVisibility(View.GONE);
            }else {
                search.setVisibility(View.VISIBLE);
                cart.setVisibility(View.VISIBLE);
            }


            if(fragment == cartFragment){
                cartFragment = new CartFragment();
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
                fm.beginTransaction().add(R.id.fragment_container, cartFragment, "cart").hide(cartFragment).commitAllowingStateLoss();
                fm.beginTransaction().hide(active).show(cartFragment).commitAllowingStateLoss();
                active = cartFragment;
            }
            else if (fragment == accountFragment){
                accountFragment = new AccountFragment();
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
                fm.beginTransaction().add(R.id.fragment_container, accountFragment, "account").hide(accountFragment).commitAllowingStateLoss();
                fm.beginTransaction().hide(active).show(accountFragment).commitAllowingStateLoss();
                active = accountFragment;
            }
            else{
                fm.beginTransaction().hide(active).show(fragment).commit();
                active = fragment;
            }
        }

        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().getExtras() != null){
            String message = getIntent().getStringExtra("message");
            String position = getIntent().getStringExtra("position");
            searchValue = getIntent().getIntExtra("Search",0);

           // CustomToast.showToast(this, message, R.color.colorSuccess);
            getIntent().removeExtra("message");
            getIntent().removeExtra("position");
            if(searchValue == 1){
                loadFragment(searchFragment);
            }

            if(position.equals("cart")){
                loadFragment(cartFragment);
                navView.setSelectedItemId(R.id.navigation_cart);
            }
            else if (position.equals("account")){
                loadFragment(accountFragment);
                navView.setSelectedItemId(R.id.navigation_account);
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (active == homeFragment){
            super.onBackPressed();
        }
        else {
            loadFragment(homeFragment);
            navView.setSelectedItemId(R.id.navigation_home);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            new AppSettingsInteractorImpl(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).execute();
        }
        if (resultCode == Activity.RESULT_CANCELED) {

        }

    }

    @Override
    public void onAppSettingsLoaded(AppSettingsResponse appSettingsResponse) {
        UserPrefs userPrefs = new UserPrefs(this);
        userPrefs.setAppSettingsPreferenceObject(appSettingsResponse, "app_settings_response");

        accountFragment = new AccountFragment();
        fm.beginTransaction().remove(accountFragment).commitAllowingStateLoss();
        fm.beginTransaction().add(R.id.fragment_container, accountFragment, "account").hide(accountFragment).commitAllowingStateLoss();
        fm.beginTransaction().hide(active).show(accountFragment).commitAllowingStateLoss();
        active = accountFragment;
    }

    @Override
    public void onAppSettingsLoadedError() {

    }


    @Override
    public void displaySearchScreen() {
//        Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show();
    }


}