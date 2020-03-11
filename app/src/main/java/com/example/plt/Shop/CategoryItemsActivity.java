package com.example.plt.Shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.plt.Common.Common;
import com.example.plt.Common.Consts;
import com.example.plt.Database.Database;
import com.example.plt.Interface.ItemClickListener;
import com.example.plt.ItemShowPage.ItemActivity;
import com.example.plt.Models.Item;
import com.example.plt.Models.WishlistItem;
import com.example.plt.R;
import com.example.plt.ViewHolder.ItemsListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryItemsActivity extends AppCompatActivity {

    private RecyclerView categoryItemsRecycleV;
    private FirebaseDatabase database;
    private DatabaseReference itemList;
    private String categoryID = "";
    private String categoryName = "";


    // FirebaseRecyclerAdapter<Item, ItemsListViewHolder> adapter;
    private List<Item> itemsArrayList = new ArrayList<>();


   // private ItemsListAdapter adapter1;
    private FirebaseRecyclerAdapter<Item, ItemsListViewHolder> adapter;
    public Database localDB;

    private int sortType = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory_items);

        //firebase
        database = FirebaseDatabase.getInstance();
        itemList = database.getReference(Common.ITEM_REF);

       // setCategoryId();
        //local db
        localDB = new Database(this);

        categoryItemsRecycleV = findViewById(R.id.catagory_items_recycleV);
        categoryItemsRecycleV.setHasFixedSize(true);
        // gridlayout
        categoryItemsRecycleV.setLayoutManager(new GridLayoutManager(this, 2));

    }

    @Override
    protected void onResume() {
        super.onResume();
        setCategoryId();
        setToolBar();


    }

    private void setCategoryId() {
        if (getIntent() != null) {
            categoryID = getIntent().getStringExtra(Common.CATEGORY_ID);
            categoryName = getIntent().getStringExtra("nameCategory");
        }
        if (!categoryID.isEmpty() && categoryID != null) {
            Log.d("pttttc ", categoryID + "id");
              loadListItem(categoryID);
//            loadItemsFromFireBase(categoryID);
//            adapter1 = new ItemsListAdapter(this, itemsArrayList);
//            categoryItemsRecycleV.setAdapter(adapter);
        }
    }

    private void setToolBar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(categoryName);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sizes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.sortBy_item:
                Toast.makeText(this, "sort by", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.highToLow_item:
                Toast.makeText(this, "h t l", Toast.LENGTH_SHORT).show();
                Collections.sort(itemsArrayList, Item.HIGH_LOW_TO);
            //    adapter1 = new ItemsListAdapter(this, itemsArrayList);
               // categoryItemsRecycleV.setAdapter(adapter1);
                sortType = 1;//high to low
                loadItemsAfterSort();
                return true;
            case R.id.lowToHigh_item:
                Toast.makeText(this, "l t h", Toast.LENGTH_SHORT).show();
                sortType = 2;//low to high
                Collections.sort(itemsArrayList, Item.LOW_TO_HIGH);
//                adapter1 = new ItemsListAdapter(this, itemsArrayList);
//                categoryItemsRecycleV.setAdapter(adapter1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void loadItemsAfterSort() {
    }

//    private void loadItemsFromFireBase(String categoryID) {
//        itemList.orderByChild("categoryId").equalTo(categoryID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    itemsArrayList.add(postSnapshot.getValue(Item.class));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });
//    }

    private void loadListItem(String categoryID) {
        FirebaseRecyclerOptions<Item> options;
        options = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(itemList.orderByChild("categoryId").equalTo(categoryID), Item.class).build();
        adapter = new FirebaseRecyclerAdapter<Item, ItemsListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ItemsListViewHolder itemsListViewHolder, final int i, @NonNull final Item item) {
                Picasso.get().load(item.getImage1())
                        .into(itemsListViewHolder.itemImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                final WishlistItem wishlistItem = new WishlistItem(adapter.getRef(i).getKey(),
                        item.getDescription(), item.getPrice(), item.getImage1(),Common.currentUser.getPhone());

                //add to wishlist
                if (localDB.isFavorite(adapter.getRef(i).getKey(),Common.currentUser.getPhone())) {
                    itemsListViewHolder.wishlistImg.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
                itemsListViewHolder.wishlistImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!localDB.isFavorite(adapter.getRef(i).getKey(),Common.currentUser.getPhone())) {
                            localDB.addToWishlist(wishlistItem);
                            itemsListViewHolder.wishlistImg.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(CategoryItemsActivity.this, "added to wishlist", Toast.LENGTH_SHORT).show();
                        } else {
                            localDB.removeFromWishlist(wishlistItem);
                            itemsListViewHolder.wishlistImg.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(CategoryItemsActivity.this, "remove to wishlist", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                itemsListViewHolder.itemDescription.setText(item.getDescription());
                itemsListViewHolder.itemPrice.setText(item.getPrice() + Consts.DOLLAR);

                itemsArrayList.add(item);
                itemsListViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent itemPage = new Intent(CategoryItemsActivity.this, ItemActivity.class);
                        itemPage.putExtra(Common.ITEM_ID, adapter.getRef(position).getKey());
                        startActivity(itemPage);
                    }
                });
            }

            @NonNull
            @Override
            public ItemsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_item, parent, false);
                return new ItemsListViewHolder(view);
            }
        };
        adapter.startListening();
        categoryItemsRecycleV.setAdapter(adapter);
    }


}
