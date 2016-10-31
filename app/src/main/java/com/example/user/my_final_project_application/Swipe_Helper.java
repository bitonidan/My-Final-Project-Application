package com.example.user.my_final_project_application;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;

/**
 * Created by User on 10/10/2016.
 */
public class Swipe_Helper extends ItemTouchHelper.SimpleCallback {

    Favorite_Adapter adapter;

    Context context;



    public Swipe_Helper( Favorite_Adapter adapter,Context context) {
        super(ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        this.adapter = adapter;
        this.context=context;


    }

    public Swipe_Helper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int pos) {

        adapter.DismissPlace(viewHolder.getAdapterPosition());
    }
}
