package com.udacity.movietip.ui.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

/* Provide margins all around our recyclerview poster items using RecyclerView.ItemDecoration
 * Reference: https://www.youtube.com/watch?v=Z_DooPH6Aio&t=384s
 */
public class RecyclerViewItemMargins extends RecyclerView.ItemDecoration {

    private int margin;

    public RecyclerViewItemMargins(int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        int position = parent.getChildLayoutPosition(view);
        outRect.top = margin;
        outRect.left = margin;
        outRect.right = margin;
    }
}
