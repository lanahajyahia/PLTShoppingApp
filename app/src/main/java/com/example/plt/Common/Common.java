package com.example.plt.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plt.BottomNivBarActivity;
import com.example.plt.Database.Database;
import com.example.plt.ItemShowPage.ItemActivity;
import com.example.plt.Models.Order;
import com.example.plt.Models.User;
import com.example.plt.R;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class Common {

    public static User currentUser;



    public static List<Order> currentBagList;
    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null){
            NetworkInfo[] info =connectivityManager.getAllNetworkInfo();
            if (info!=null){
                for (int i=0;i<info.length;i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public static final String USER_KEY="user";
    public static final String PSWD_KEY="password";

    public final static String ITEM_ID = "itemId";
    public final static String CATEGORY_ID = "categoryId";

    public final static String CATEGORY_REF = "Category";
    public final static String ITEM_REF ="Item";





}
