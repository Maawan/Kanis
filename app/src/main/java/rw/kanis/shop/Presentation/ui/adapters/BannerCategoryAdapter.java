package rw.kanis.shop.Presentation.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.daimajia.slider.library.SliderLayout;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import carbon.recycler.ListAdapter;
import rw.kanis.shop.Models.BannerData;
import rw.kanis.shop.Models.BannerDataLinks;
import rw.kanis.shop.Models.BannerProducts;
import rw.kanis.shop.R;
import rw.kanis.shop.Utils.AppConfig;

import static rw.kanis.shop.Presentation.ui.activities.impl.SplashActivity.SCREEN_HEIGHT;
import static rw.kanis.shop.Presentation.ui.activities.impl.SplashActivity.SCREEN_WIDTH;

public class BannerCategoryAdapter extends RecyclerView.Adapter<BannerCategoryAdapter.ViewHolder> {
    private Context ctx;
    private List<BannerData> list;


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageSlider sliderLayout;
        private RecyclerView recyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText);
            sliderLayout = itemView.findViewById(R.id.image_slider);
            recyclerView = itemView.findViewById(R.id.recyclerForProducts);
        }
    }

    public BannerCategoryAdapter(Context ctx, List<BannerData> list) {
        this.ctx = ctx;
        this.list = list;
    }


    @NonNull
    @Override
    public BannerCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View contactView = inflater.inflate(R.layout.banner_category_layout, parent, false);


        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BannerData data = list.get(position);
        holder.title.setText(data.getName());
        if(data.getName().length() > 20){
            holder.title.setTextSize(15);
        }
        final int[] height = new int[1];

        List<BannerDataLinks> tempLinks = data.getSlider_imgs();
        try {
            URL url = new URL(tempLinks.get(0).getDataSource());
            Glide.with(ctx).asBitmap().load(tempLinks.get(0).getDataSource()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                    ViewGroup.LayoutParams layoutParams = holder.sliderLayout.getLayoutParams();




//                    layoutParams.width = resource.getWidth();
                    layoutParams.height = (int) (SCREEN_WIDTH * resource.getHeight()) / resource.getWidth() ;
                    //Toast.makeText(ctx, SCREEN_HEIGHT + " ", Toast.LENGTH_SHORT).show();
                    holder.sliderLayout.setLayoutParams(layoutParams);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<SlideModel> images = new ArrayList<>();
        for (int i = 0; i < tempLinks.size(); i++) {

            images.add(new SlideModel(tempLinks.get(i).getDataSource(), ScaleTypes.CENTER_INSIDE));
        }
        if (images.size() > 0) {
            holder.sliderLayout.setImageList(images);
           // holder.sliderLayout.startSliding(20);
            holder.sliderLayout.startSliding(2000);

        }else{
            holder.sliderLayout.setVisibility(View.GONE);
        }
        List<BannerProducts> list  = data.getProducts();
        BannerProductsAdapter adapter = new BannerProductsAdapter(ctx , list);
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(ctx , LinearLayoutManager.HORIZONTAL,false));



    }

    @Override
    public int getItemCount() {

        return list.size();
    }

}
