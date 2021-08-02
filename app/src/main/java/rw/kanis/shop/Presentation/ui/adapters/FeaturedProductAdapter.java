package rw.kanis.shop.Presentation.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.Product;
import rw.kanis.shop.Presentation.ui.listeners.ProductClickListener;
import rw.kanis.shop.R;
import rw.kanis.shop.Utils.AppConfig;
import com.bumptech.glide.Glide;

import java.util.List;

public class FeaturedProductAdapter extends RecyclerView.Adapter<FeaturedProductAdapter.ViewHolder> {
    private List<Product> mProducts;
    private LayoutInflater mInflater;
    private Context context;
    private ProductClickListener productClickListener;

    // data is passed into the constructor
    public FeaturedProductAdapter(Context context, List<Product> mProducts, ProductClickListener productClickListener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mProducts = mProducts;
        this.productClickListener = productClickListener;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.featured_product_box, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview rw each row
    @Override
    public void onBindViewHolder(@NonNull FeaturedProductAdapter.ViewHolder holder, int position) {
        holder.bind(mProducts.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    // convenience method for getting data at click position
    public Product getItem(int id) {
        return mProducts.get(id);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView discounted_price, price;
        TextView name;
        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.product_image);
            discounted_price = itemView.findViewById(R.id.product_discounted_price);
            price = itemView.findViewById(R.id.product_price);
            name = itemView.findViewById(R.id.product_name);
            ratingBar = itemView.findViewById(R.id.product_rating);
        }

        public void bind(Product product) {
            Glide.with(context).load(AppConfig.ASSET_URL + product.getThumbnailImage()).into(image);
            discounted_price.setText(AppConfig.convertPrice(context, product.getBaseDiscountedPrice()));
            price.setText(AppConfig.convertPrice(context, product.getBasePrice()));
            if (product.getBaseDiscountedPrice().equals(product.getBasePrice())) {
                price.setVisibility(View.GONE);
            }
            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            name.setText(product.getName());
            ratingBar.setRating(product.getRating());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productClickListener.onProductItemClick(product);
                }
            });
        }
    }
}
