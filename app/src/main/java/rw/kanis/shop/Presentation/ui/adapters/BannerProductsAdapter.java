package rw.kanis.shop.Presentation.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import rw.kanis.shop.Models.BannerProducts;
import rw.kanis.shop.Presentation.ui.activities.impl.ProductDetailsActivity;
import rw.kanis.shop.R;
import rw.kanis.shop.Utils.AppConfig;


public class BannerProductsAdapter extends RecyclerView.Adapter<BannerProductsAdapter.ViewHolder> {
    private List<BannerProducts> productsList;
    private Context ctx;

    public BannerProductsAdapter(Context ctx , List<BannerProducts>  products){
        this.productsList = products;
        this.ctx = ctx;
    }
    @NonNull
    @Override
    public BannerProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.banner_products_layout, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BannerProductsAdapter.ViewHolder holder, int position) {
        BannerProducts data = productsList.get(position);
        holder.price_discounted_price.setText(AppConfig.convertPrice(ctx ,  data.getBase_discounted_price()));
        holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        Glide.with(ctx).load(data.getDataSource()).into(holder.imageView);
        holder.title.setText(data.getName());
        holder.price.setText(data.getPrice());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, ProductDetailsActivity.class);
                intent.putExtra("product_name", data.getName());
                intent.putExtra("link", data.getSpecificAPIurl());
                intent.putExtra("top_selling", "");
                ctx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title;
        private CardView cardView;
        private TextView price_discounted_price, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            imageView = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.product_name);
            price_discounted_price = itemView.findViewById(R.id.product_discounted_price);
            price = itemView.findViewById(R.id.product_price);

        }
    }
}
