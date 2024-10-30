package com.example.unitconverter.AppInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.unitconverter.R;

public class SwipePagerAdapter extends RecyclerView.Adapter<SwipePagerAdapter.ViewHolder> {

    private int[] layouts;
    private OnPageActionListener pageActionListener;

    public SwipePagerAdapter(int[] layouts, OnPageActionListener listener) {
        this.layouts = layouts;
        this.pageActionListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return layouts[position];
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind skip and next button actions to the ViewHolder
        TextView skipButton = holder.itemView.findViewById(R.id.btn_skip);
        TextView nextButton = holder.itemView.findViewById(R.id.btn_next);

        if (skipButton != null) {
            skipButton.setOnClickListener(v -> pageActionListener.onSkip());
        }

        if (nextButton != null) {
            nextButton.setText(position == layouts.length - 1 ? "Get Started" : "Next");
            nextButton.setOnClickListener(v -> {
                if (position < layouts.length - 1) {
                    pageActionListener.onNextPage(position + 1);
                } else {
                    pageActionListener.onGetStarted();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return layouts.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    // Interface to handle actions from SwipeScreen
    public interface OnPageActionListener {
        void onSkip();
        void onNextPage(int nextPageIndex);
        void onGetStarted();
    }
}