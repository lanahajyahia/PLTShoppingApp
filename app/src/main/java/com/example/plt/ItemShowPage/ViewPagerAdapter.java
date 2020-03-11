package com.example.plt.ItemShowPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.plt.R;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private String image1, image2;

    public ViewPagerAdapter() {
    }

    public ViewPagerAdapter(ItemActivity itemActivity, String img1, String img2) {
        context = itemActivity;
        image1 = img1;
        image2 = img2;
    }


    private int[] images = {0, 1};

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_image_layout, null);
        ImageView itemImageView = view.findViewById(R.id.item_image_viewpager);
        if (position == 0) {
            Picasso.get().load(image1).into(itemImageView);
        } else {
            Picasso.get().load(image2).into(itemImageView);
        }

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);

    }
}
