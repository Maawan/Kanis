package rw.kanis.shop.Presentation.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.SearchProduct;
import rw.kanis.shop.Presentation.ui.listeners.SearchProductClickListener;
import rw.kanis.shop.R;
import rw.kanis.shop.Utils.AppConfig;
import com.bumptech.glide.Glide;

import java.util.List;

import static rw.kanis.shop.Presentation.ui.fragments.impl.ProductSearchFragment.priceRangeFilter;

public class ProductSearchAdapter extends RecyclerView.Adapter<ProductSearchAdapter.ViewHolder> {

    private List<SearchProduct> mProducts;
    private LayoutInflater mInflater;
    private Context context;
    private SearchProductClickListener productClickListener;
    private int initalPrice = 0;
    private int finalPrice = 99999;





    // data is passed into the constructor
    public ProductSearchAdapter(Context context, List<SearchProduct> mProducts, SearchProductClickListener productClickListener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);

        this.mProducts = mProducts;
        this.productClickListener = productClickListener;
        if(priceRangeFilter.get("Initial") != null){
            initalPrice = priceRangeFilter.get("Initial");
            finalPrice = priceRangeFilter.get("Final");
        }

    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.search_product_box, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview rw each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mProducts.get(position));

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    // convenience method for getting data at click position
 //   public SearchProduct getItem(int id) {
   //     return mProducts.get(id);
    //}

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView discounted_price, price;
        TextView name;
        TextView sales;
        RatingBar ratingBar;
        CardView productSearchCard;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.product_image);
            discounted_price = itemView.findViewById(R.id.product_discounted_price);
            price = itemView.findViewById(R.id.product_price);
            name = itemView.findViewById(R.id.product_name);
            productSearchCard = itemView.findViewById(R.id.productSearchCard);
            ratingBar = itemView.findViewById(R.id.product_rating);
            sales = itemView.findViewById(R.id.product_rating_count);
        }

        public void bind(SearchProduct product) {
            if(product.getBaseDiscountedPrice() < initalPrice || product.getBaseDiscountedPrice() > finalPrice){
                //productSearchCard.setVisibility(View.GONE);
//                mProducts.remove(getAdapterPosition());
//                notifyItemRemoved(getAdapterPosition());
//                notifyItemRangeChanged(getAdapterPosition(),mProducts.size());
            }
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

