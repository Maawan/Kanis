package rw.kanis.shop.Presentation.ui.activities.impl;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import rw.kanis.shop.Presentation.ui.activities.OnSearchButtonClick;
import rw.kanis.shop.R;

public class BaseActivity extends AppCompatActivity {
    protected ImageButton cart, search;
    protected TextView title;
    private OnSearchButtonClick listener;

    public void initializeActionBar(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        getSupportActionBar().setElevation(0);

        View view =getSupportActionBar().getCustomView();

        cart = view.findViewById(R.id.action_bar_cart);
        search = view.findViewById(R.id.action_bar_search);
        title = view.findViewById(R.id.nav_title);

        cart.setVisibility(View.GONE);
        search.setVisibility(View.VISIBLE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BaseActivity.this,MainActivity.class).putExtra("Search",1).putExtra("message","").putExtra("position",""));

            }
        });
    }

    public void setTitle(String s){
        title.setText(s);
    }
}
