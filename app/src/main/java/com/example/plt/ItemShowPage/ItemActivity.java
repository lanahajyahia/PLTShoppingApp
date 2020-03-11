package com.example.plt.ItemShowPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plt.Common.Common;
import com.example.plt.Common.Consts;
import com.example.plt.Database.Database;
import com.example.plt.Models.Item;
import com.example.plt.Models.Order;
import com.example.plt.Models.WishlistItem;
import com.example.plt.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private TextView imageDescription, imagePrice;
    private ImageButton goBackBtn;
    private ImageView addToWishlist, btnList;
    private Button addItemTobagBtn;
    private BottomSheetDialog bottomSheetDialog;
    private Database localDB;


    private String itemId = "", categoryID = "";
    FirebaseDatabase database;
    DatabaseReference item;

    Item currentItem;
    String itemSize = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        init();
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    public void init() {
        viewPager = findViewById(R.id.view_pager);
        imageDescription = findViewById(R.id.item_description);
        imagePrice = findViewById(R.id.item_price);
        goBackBtn = findViewById(R.id.button_go_back);
        addItemTobagBtn = findViewById(R.id.button_addToBag);
        addToWishlist = findViewById(R.id.img_add_wl_itemActivity);
        // btnList= findViewById(R.id.img_add_to_wishlist);

        localDB = new Database(this);


        database = FirebaseDatabase.getInstance();
        item = database.getReference(Common.ITEM_REF);

        if (getIntent() != null)
            itemId = getIntent().getStringExtra(Common.ITEM_ID);
        if (!itemId.isEmpty()) {
            getItemDetails(itemId);
        }

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addItemTobagBtn.setOnClickListener((View.OnClickListener) this);

    }

    private void getItemDetails(final String itemId) {
        item.child(itemId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentItem = dataSnapshot.getValue(Item.class);

                TabLayout tabLayout = findViewById(R.id.tabDots);
                tabLayout.setupWithViewPager(viewPager, true);
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(ItemActivity.this, currentItem.getImage1(), currentItem.getImage2());
                viewPager.setAdapter(viewPagerAdapter);

                imageDescription.setText(currentItem.getDescription());
                imagePrice.setText(currentItem.getPrice() + Consts.DOLLAR);
                categoryID = currentItem.getCategoryId();

                final WishlistItem wishlistItem = new WishlistItem(itemId, currentItem.getDescription(), currentItem.getPrice(), currentItem.getImage1(), Common.currentUser.getPhone());


                //add to wishlist
                if (localDB.isFavorite(itemId, Common.currentUser.getPhone()))
                    addToWishlist.setImageResource(R.drawable.ic_favorite_black_24dp);

                addToWishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!localDB.isFavorite(itemId, Common.currentUser.getPhone())) {
                            localDB.addToWishlist(wishlistItem);
                            addToWishlist.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(ItemActivity.this, "added to wishlist", Toast.LENGTH_SHORT).show();
                        } else {
                            localDB.removeFromWishlist(wishlistItem);
                            addToWishlist.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            //btnList.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(ItemActivity.this, "remove to wishlist", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


                Log.d("catid", categoryID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setBottomSheetDialog() {
        if (categoryID.equals("05")) {
            BottomSheetDialog bottomSheetDialogShoes = new BottomSheetDialog(ItemActivity.this);
            View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.btm_sheet_size_shoes, null);
            //  bottomSheetDialog.setTitle(R.string.select_size);
            bottomSheetDialogShoes.setContentView(bottomSheetDialogView);

            TextView s36 = bottomSheetDialogView.findViewById(R.id.shoe36);
            TextView s37 = bottomSheetDialogView.findViewById(R.id.shoe37);
            TextView s38 = bottomSheetDialogView.findViewById(R.id.shoe38);
            TextView s39 = bottomSheetDialogView.findViewById(R.id.shoe39);
            TextView s40 = bottomSheetDialogView.findViewById(R.id.shoe40);
            TextView s41 = bottomSheetDialogView.findViewById(R.id.shoe41);

            s36.setOnClickListener(this);
            s37.setOnClickListener(this);
            s38.setOnClickListener(this);
            s39.setOnClickListener(this);
            s40.setOnClickListener(this);
            s41.setOnClickListener(this);

            bottomSheetDialog = bottomSheetDialogShoes;
            bottomSheetDialog.show();
        } else if (categoryID.equals("06")) {
            Toast.makeText(ItemActivity.this, "added to bag : one size", Toast.LENGTH_SHORT).show();
            itemSize = "one size";
            addToBag();
        } else {
            BottomSheetDialog bottomSheetDialogClothes = new BottomSheetDialog(ItemActivity.this);
            View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.btm_sheet_size_clothes, null);
            //  bottomSheetDialog.setTitle(R.string.select_size);
            bottomSheetDialogClothes.setContentView(bottomSheetDialogView);

            TextView xsmall = bottomSheetDialogView.findViewById(R.id.xsmall);
            TextView small = bottomSheetDialogView.findViewById(R.id.small);
            TextView medium = bottomSheetDialogView.findViewById(R.id.medium);
            TextView large = bottomSheetDialogView.findViewById(R.id.large);
            TextView xlarge = bottomSheetDialogView.findViewById(R.id.xlarge);

            xsmall.setOnClickListener(this);
            small.setOnClickListener(this);
            medium.setOnClickListener(this);
            large.setOnClickListener(this);
            xlarge.setOnClickListener(this);
            bottomSheetDialog = bottomSheetDialogClothes;
            bottomSheetDialog.show();
        }
    }

    private void addToBag() {
        boolean isExist = new Database(this).checkItemExist(itemId, Common.currentUser.getPhone(),itemSize);
        Log.d("pttt", "addToBag: " + isExist);

        if (!isExist) {
            new Database(this).addToBag(new Order(
                    Common.currentUser.getPhone(),
                    itemId,
                    currentItem.getDescription(),
                    "1",
                    currentItem.getPrice(),
                    currentItem.getDiscount(),
                    itemSize,
                    currentItem.getImage1()));
        } else {
            Log.d("pttt", "addToBag: alreadyexist");
            new Database(this).increaseBag(Common.currentUser.getPhone(), itemId,itemSize);

        }
        Toast.makeText(ItemActivity.this, "added to bag " + itemSize, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.button_addToBag) {
            setBottomSheetDialog();
        } else {
            TextView tv = (TextView) v;
            itemSize = tv.getText().toString();
            addToBag();

            bottomSheetDialog.hide();
        }

    }

}
