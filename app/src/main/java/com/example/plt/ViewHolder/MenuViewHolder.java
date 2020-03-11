package com.example.plt.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plt.Interface.ItemClickListener;
import com.example.plt.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView categoryName;
    public ImageView categoryImage;

    private ItemClickListener itemClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        categoryName =itemView.findViewById(R.id.menu_category_name);
        categoryImage=itemView.findViewById(R.id.menu_category_img);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}