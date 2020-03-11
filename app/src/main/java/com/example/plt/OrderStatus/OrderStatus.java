package com.example.plt.OrderStatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.plt.Common.Common;
import com.example.plt.Models.Request;
import com.example.plt.Models.Sortbyprice;
import com.example.plt.R;
import com.example.plt.ViewHolder.OrderStatusViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderStatusViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        //tool bar
        setToolBar();
        //firebase
        database = FirebaseDatabase.getInstance();
        request = database.getReference("Request");

        //recycle
        recyclerView = findViewById(R.id.order_status_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());
    }

    private void setToolBar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order History");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.finish();
        return true;
    }

    private void loadOrders(String phone) {

        FirebaseRecyclerOptions<Request> options;
        options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(request.orderByChild("phone").equalTo(phone), Request.class).build();
        adapter = new FirebaseRecyclerAdapter<Request, OrderStatusViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderStatusViewHolder orderStatusViewHolder, int i, @NonNull Request request) {
                orderStatusViewHolder.txtOrderId.setText(adapter.getRef(i).getKey());
                orderStatusViewHolder.txtOrderstatus.setText(convertCodeTostatus(request.getStatus()));
                orderStatusViewHolder.txtOrderPhone.setText(request.getPhone());
                orderStatusViewHolder.txtOrderAddress.setText(request.getAddress());
            }

            @NonNull
            @Override
            public OrderStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_order_layout, parent, false);
                return new OrderStatusViewHolder(view);
            }


        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }

    private String convertCodeTostatus(String status) {
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "On its way";
        else
            return "Deliverd";

    }
}
