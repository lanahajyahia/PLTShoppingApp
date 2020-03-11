package com.example.plt;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.plt.Bag.*;
import com.example.plt.Common.Common;
import com.example.plt.Database.Database;
import com.example.plt.Home.HomeFragment;
import com.example.plt.Profile.*;
import com.example.plt.Shop.ShopFragment;
import com.example.plt.wishlist.WishlistFragment;

public class BottomNivBarActivity extends AppCompatActivity {
    public AHBottomNavigation bottomNavigation;
    public AHBottomNavigationItem item2;

    public BottomNivBarActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_niv_bar);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new HomeFragment()).commit();

        bottomNavigation = findViewById(R.id.bottom_navigation);

        setMyToolBar();
        setMyAHBottomNavigation();


    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }


    private void setMyToolBar() {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("PRETTYLITTLETHING");
        setSupportActionBar(myToolbar);

    }

    private void setMyAHBottomNavigation() {

        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.img_navbar_home,
                R.color.colorBottomNavigationInactive);
        item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.img_navbar_shop,
                R.color.colorBottomNavigationInactive);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.img_navbar_bag,
                R.color.colorBottomNavigationInactive);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.tab_4, R.drawable.img_navbar_wishlist,
                R.color.colorBottomNavigationInactive);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.tab_5, R.drawable.img_navbar_profile,
                R.color.colorBottomNavigationInactive);

        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

        // Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FFFFFF"));

        // Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);


        // Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#131212"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

        // Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

        bottomNavigation.setTranslucentNavigationEnabled(true);

        // Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);


        // Set current item programmatically
        bottomNavigation.setCurrentItem(0);

        // Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#0C9C14"));
        bottomNavigation.setNotificationTextColor(Color.parseColor("#FFFFFF"));

        // Add or remove notification for each item


        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                Fragment selectedFragment = null;

                switch (position) {
                    case 0:
                        selectedFragment = new HomeFragment();
                        break;
                    case 1:
                        selectedFragment = new ShopFragment();
                        break;
                    case 2:
//                        if (Common.currentBagList != null) {
//                            if (Common.currentBagList.size() != 0) {
//                                navView.getOrCreateBadge(R.id.icon_menu_bag).setNumber(Common.currentBagList.size());
//                            }
//                        } else {
//                            navView.getOrCreateBadge(R.id.icon_menu_bag).setNumber(0);
//                            navView.getBadge(R.id.icon_menu_bag).setVisible(false);
//                        }
                        selectedFragment = new BagFragment();
                        break;
                    case 3:
                        selectedFragment = new WishlistFragment();
                        break;
                    case 4:
                        selectedFragment = new ProfileFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container
                        , selectedFragment).commit();

                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
      //  updateBagCount();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    public void updateBagCount() {
        if (new Database(this).getCountBag()>0)
            bottomNavigation.setNotification("" + new Database(this).getCountBag(), 2);
    }
}
