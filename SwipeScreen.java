package com.example.unitconverter.AppInterface;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.example.unitconverter.R;

public class SwipeScreen extends AppCompatActivity implements SwipePagerAdapter.OnPageActionListener {

    private ViewPager2 viewPager;
    private LinearLayout dotsLayout;
    private int[] layouts = {
            R.layout.swipe_screen1,
            R.layout.swipe_screen2,
            R.layout.swipe_screen3
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_screen);

        viewPager = findViewById(R.id.viewPager);
        dotsLayout = findViewById(R.id.dotsLayout);
        SwipePagerAdapter adapter = new SwipePagerAdapter(layouts, this);
        viewPager.setAdapter(adapter);

        setupDotIndicators();
        updateDotIndicator(0);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateDotIndicator(position);
            }
        });
    }

    private void setupDotIndicators() {
        for (int i = 0; i < layouts.length; i++) {
            View dot = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dot.setBackground(ContextCompat.getDrawable(this, R.drawable.inactive_dot));
            final int index = i;
            dot.setOnClickListener(v -> viewPager.setCurrentItem(index));
            dotsLayout.addView(dot);
        }
        updateDotIndicator(0);
    }

    private void updateDotIndicator(int position) {
        for (int i = 0; i < dotsLayout.getChildCount(); i++) {
            View dot = dotsLayout.getChildAt(i);
            dot.setBackground(ContextCompat.getDrawable(this, i == position ? R.drawable.active_dot : R.drawable.inactive_dot));
        }
    }

    @Override
    public void onSkip() {
        navigateToGetStarted();
    }

    @Override
    public void onNextPage(int nextPageIndex) {
        viewPager.setCurrentItem(nextPageIndex);
    }

    @Override
    public void onGetStarted() {
        navigateToGetStarted();
    }

    private void navigateToGetStarted() {
        Intent intent = new Intent(SwipeScreen.this, GetStarted.class);
        startActivity(intent);
        finish();
    }
}
