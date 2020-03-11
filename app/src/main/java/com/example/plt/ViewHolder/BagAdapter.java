package com.example.plt.ViewHolder;


import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.plt.Bag.BagFragment;
import com.example.plt.BottomNivBarActivity;
import com.example.plt.Common.Common;
import com.example.plt.Database.Database;
import com.example.plt.Interface.ItemClickListener;
import com.example.plt.ItemShowPage.ItemActivity;
import com.example.plt.Models.Order;
import com.example.plt.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BagAdapter extends RecyclerView.Adapter<BagAdapter.BagViewHolder> {
    private BagFragment bagFragment;
    private List<Order> listData = new ArrayList<>();


    public BagAdapter(BagFragment bagFragment, List<Order> listData) {

        this.listData = listData;
        this.bagFragment = bagFragment;

    }

    @NonNull
    @Override
    public BagAdapter.BagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(bagFragment.getContext()).inflate(R.layout.bag_item_layout, parent, false);

        return new BagViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BagViewHolder holder, final int position) {
        Picasso.get()
                .load(listData.get(position).getImage())
                .into(holder.imgItem);
        holder.elegantNumberButton.setNumber(listData.get(position).getQuantity());

        holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                bagFragment.localDB.updateBag(order);
                bagFragment.loadBagList();

            }
        });
        //price with $
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        final int[] price = {(Integer.parseInt(listData.get(position).getPrice()) * Integer.parseInt(listData.get(position).getQuantity()))};
        Log.d("ptttt", "price: " + price[0]);
        holder.priceItem.setText(fmt.format(price[0]));

        holder.descriptionItem.setText(listData.get(position).getItemName());
        holder.sizeItem.setText(listData.get(position).getSize());


        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int i, boolean isLongClick) {
                if (view.getId() == R.id.delete_from_bag_img) {
                    bagFragment.localDB.removeFromBag(listData.get(position));
                    listData.remove(position);

                    notifyDataSetChanged();
                    notifyItemRemoved(position);

                    bagFragment.loadBagList();

                } else {
                    Intent itemPage = new Intent(bagFragment.getContext(), ItemActivity.class);
                    Log.d("idd", listData.get(position).getItemId());
                    itemPage.putExtra(Common.ITEM_ID, listData.get(position).getItemId());
                    //   itemPage.putExtra(Constans.CATEGORY_ID,item.getCategoryId());
                    bagFragment.getContext().startActivity(itemPage);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class BagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgItem, deleteFromBag_btn;
        TextView descriptionItem, priceItem, sizeItem;
        ElegantNumberButton elegantNumberButton;

        private ItemClickListener itemClickListener;


        public BagViewHolder(View itemView) {
            super(itemView);

            imgItem = itemView.findViewById(R.id.img_bag_item);
            descriptionItem = itemView.findViewById(R.id.bag_item_description);
            priceItem = itemView.findViewById(R.id.bag_item_price);
            sizeItem = itemView.findViewById(R.id.bag_item_size);
            elegantNumberButton = itemView.findViewById(R.id.item_quantity_inBag);
            deleteFromBag_btn = itemView.findViewById(R.id.delete_from_bag_img);


            itemView.setOnClickListener(this);
            deleteFromBag_btn.setOnClickListener(this);
            //  itemCardView.setOnClickListener(vie);

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