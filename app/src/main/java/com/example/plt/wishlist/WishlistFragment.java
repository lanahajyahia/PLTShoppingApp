package com.example.plt.wishlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.plt.Common.Common;
import com.example.plt.Database.Database;
import com.example.plt.Models.WishlistItem;
import com.example.plt.R;
import com.example.plt.ViewHolder.WishlistAdapter;



import java.util.ArrayList;
import java.util.List;


public class WishlistFragment extends Fragment {

    private RecyclerView wishlistRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    private TextView txtTotalAmount, clearAllWishlist_btn;

    public Database localDB;


    private LinearLayout emptyScreen, wishlistLayout;
    private List<WishlistItem> wishlistItems = new ArrayList<>();
    private WishlistAdapter wishlistAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        localDB = new Database(getContext());

        emptyScreen = view.findViewById(R.id.empty_screen_wishlist);
        wishlistLayout = view.findViewById(R.id.linearLayout_wishlist);

        wishlistRecyclerView = view.findViewById(R.id.wishlist_recycleView);
        wishlistRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        wishlistRecyclerView.setLayoutManager(layoutManager);

        clearAllWishlist_btn = view.findViewById(R.id.btn_clearAll_wl);
        txtTotalAmount = view.findViewById(R.id.items_amount_WL);

        loadWishlist();


        clearAllWishlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanWishlist();
            }
        });

        return view;
    }


    private void cleanWishlist() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Are you sure you want to clear all the items from your wishlist");


        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                localDB.cleanWishlist();
                emptyScreen.setVisibility(View.VISIBLE);
                wishlistLayout.setVisibility(View.INVISIBLE);
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();


    }

    public void loadWishlist() {
        wishlistItems = localDB.getWishlist(Common.currentUser.getPhone());
        wishlistAdapter = new WishlistAdapter(this, wishlistItems);
        wishlistRecyclerView.setAdapter(wishlistAdapter);

        if (wishlistItems.size() == 1) {
            txtTotalAmount.setText("1 item");
        } else {
            txtTotalAmount.setText(wishlistItems.size() + " items");
        }

        if (wishlistItems.size() == 0) {
            emptyScreen.setVisibility(View.VISIBLE);
            wishlistLayout.setVisibility(View.INVISIBLE);
        }




    }


    @Override
    public void onResume() {
        super.onResume();

    }
}
