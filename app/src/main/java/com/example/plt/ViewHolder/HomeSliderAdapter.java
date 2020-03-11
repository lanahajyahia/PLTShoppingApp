package com.example.plt.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.plt.R;

import java.util.ArrayList;

public class HomeSliderAdapter extends PagerAdapter {
    Context context;
    ArrayList<Integer> image_arraylist;
    private LayoutInflater layoutInflater;

    public HomeSliderAdapter(Context context, ArrayList<Integer> image_arraylist) {
        this.context = context;
        this.image_arraylist = image_arraylist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.home_viewpager_layout, container, false);

        ImageView im_slider = view.findViewById(R.id.im_slider);
        im_slider.setImageResource(image_arraylist.get(position));
        im_slider.setScaleType(ImageView.ScaleType.FIT_XY);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
