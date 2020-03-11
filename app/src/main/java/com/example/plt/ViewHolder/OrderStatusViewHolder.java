package com.example.plt.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plt.Interface.ItemClickListener;
import com.example.plt.R;

public class OrderStatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtOrderId,txtOrderstatus,txtOrderPhone,txtOrderAddress;

    private ItemClickListener itemClickListener;
    public OrderStatusViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);
        txtOrderstatus = itemView.findViewById(R.id.order_status);

        itemView.setOnClickListener(this);


    }

    public void OrderStatusViewHolder(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
