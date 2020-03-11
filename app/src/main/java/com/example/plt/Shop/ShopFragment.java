package com.example.plt.Shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plt.Common.Common;
import com.example.plt.Interface.ItemClickListener;
import com.example.plt.Models.Category;
import com.example.plt.R;
import com.example.plt.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference category;
    private RecyclerView recyclerView;
    public FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;


    private static final String TAG = "MainActivity";

    private ArrayList<String> catagoryNames = new ArrayList<>();
    private ArrayList<String> catagoryImagesUrl = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shop, container, false);

        initFireBase();


        recyclerView = v.findViewById(R.id.menu_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);


        loadMenuFB();


        return v;
    }


    private void loadMenuFB() {

        FirebaseRecyclerOptions<Category> options;
        options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(category, Category.class).build();
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder menuViewHolder, int i, @NonNull final Category category) {
                Picasso.get().load(category.getImage())
                        .into(menuViewHolder.categoryImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                menuViewHolder.categoryName.setText(category.getName());
                Log.d("cat name; ", category.getName());
                final Category clickItem = category;
                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent itemList = new Intent(getActivity(), CategoryItemsActivity.class);
                        itemList.putExtra(Common.CATEGORY_ID, adapter.getRef(position).getKey());
                        itemList.putExtra("nameCategory",category.getName());
                        startActivity(itemList);
                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_catagory_item, parent, false);
                return new MenuViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private void initFireBase() {

        database = FirebaseDatabase.getInstance();
        category = database.getReference(Common.CATEGORY_REF);


    }


}
