package com.example.plt.ViewHolder;


import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plt.Common.Common;
import com.example.plt.Database.Database;
import com.example.plt.Interface.ItemClickListener;
import com.example.plt.ItemShowPage.ItemActivity;
import com.example.plt.Models.Order;
import com.example.plt.Models.WishlistItem;
import com.example.plt.R;
import com.example.plt.wishlist.WishlistFragment;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {
    public WishlistFragment wishlistFragment;
    private List<WishlistItem> listData = new ArrayList<>();


    public WishlistAdapter(WishlistFragment wishlistFragment, List<WishlistItem> listData) {
        this.wishlistFragment = wishlistFragment;
        this.listData = listData;
    }

    @NonNull
    @Override
    public WishlistAdapter.WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(wishlistFragment.getContext()).inflate(R.layout.wishlist_item, parent, false);

        return new WishlistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.plt.ViewHolder.WishlistAdapter.WishlistViewHolder holder, final int position) {
        Picasso.get()
                .load(listData.get(position).getImage())
                .into(holder.imgItem);
        //price with $
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice()));
        holder.priceItem.setText(fmt.format(price));
        holder.descriptionItem.setText(listData.get(position).getItemName());

        //  holder.itemCardView.setOnClickListener(holder);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (view.getId() == R.id.remove_from_wishlisht_btn) {
                    wishlistFragment.localDB.removeFromWishlist(listData.get(position));
                    listData.remove(position);
                    notifyDataSetChanged();
                    notifyItemRemoved(position);

                    wishlistFragment.loadWishlist();

                } else {
                    Intent itemPage = new Intent(wishlistFragment.getContext(), ItemActivity.class);
                    Log.d("idd", listData.get(position).getItemId());
                    itemPage.putExtra(Common.ITEM_ID, listData.get(position).getItemId());
                    wishlistFragment.getContext().startActivity(itemPage);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class WishlistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgItem, rm_item_wl;
        TextView descriptionItem, priceItem;
        Button addToBagFromWL;

        private ItemClickListener itemClickListener;


        public WishlistViewHolder(View itemView) {
            super(itemView);

            imgItem = itemView.findViewById(R.id.item_image_wl);
            descriptionItem = itemView.findViewById(R.id.item_description_wl);
            priceItem = itemView.findViewById(R.id.item_price_wl);
            rm_item_wl = itemView.findViewById(R.id.remove_from_wishlisht_btn);

            itemView.setOnClickListener(this);
            rm_item_wl.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

    }
}

