package com.example.plt.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plt.Interface.ItemClickListener;
import com.example.plt.ItemShowPage.ItemActivity;
import com.example.plt.Models.Item;
import com.example.plt.R;
import com.squareup.picasso.Picasso;


public class ItemsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView itemDescription, itemPrice;
    public ImageView itemImage,wishlistImg;


    private ItemClickListener itemClickListener;

    public ItemsListViewHolder(@NonNull View itemView) {
        super(itemView);

        itemDescription = itemView.findViewById(R.id.item_description);
        itemPrice = itemView.findViewById(R.id.item_price);
        itemImage = itemView.findViewById(R.id.item_image);
        wishlistImg = itemView.findViewById(R.id.img_add_to_wishlist);

        itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
