package com.example.plt.Home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.plt.R;
import com.example.plt.ViewHolder.HomeSliderAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    ArrayList<Integer> slider_image_list = new ArrayList<>();
    int page_position = 0;
    Timer timer;
    private ViewPager images_slider;
    private HomeSliderAdapter sliderPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);

        images_slider = view.findViewById(R.id.viewpager_home_image);
       // pages_dots = rootview.findViewById(R.id.image_page_dots);

        timer = new Timer();
        initSlider();
        scheduleSlider();



        return view;
    }

    public void initSlider() {

        slider_image_list = new ArrayList<>();

        //Add few items to slider_image_list ,this should contain url of images which should be displayed in slider
        // here i am adding few sample image links from drawable, we will replace it later

        slider_image_list.add(R.drawable.image_home_4);
        slider_image_list.add(R.drawable.img_home_1);
        slider_image_list.add(R.drawable.img_home_2);
        slider_image_list.add(R.drawable.image_home_3);

        sliderPagerAdapter = new HomeSliderAdapter(getActivity(), slider_image_list);
        images_slider.setAdapter(sliderPagerAdapter);
        images_slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position){
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void scheduleSlider() {

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_image_list.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                images_slider.setCurrentItem(page_position, true);
            }
        };

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 500, 4000);
    }


    @Override
    public void onPause() {
        timer.cancel();
        super.onPause();
    }
}
