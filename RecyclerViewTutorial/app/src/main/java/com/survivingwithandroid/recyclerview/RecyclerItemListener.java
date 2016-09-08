package com.survivingwithandroid.recyclerview;

/*
* Copyright (C) 2016 Francesco Azzola - Surviving with Android (http://www.survivingwithandroid.com)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Francesco on 05/09/2016.
 */
public class RecyclerItemListener implements RecyclerView.OnItemTouchListener  {

    private RecyclerTouchListener listener;
    private GestureDetector gd;

    public interface RecyclerTouchListener {
        public void onClickItem(View v, int position) ;
        public void onLongClickItem(View v, int position);
    }

    public RecyclerItemListener(Context ctx, final RecyclerView rv, final RecyclerTouchListener listener) {
        this.listener = listener;
        gd = new GestureDetector(ctx, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                // We find the view
                View v = rv.findChildViewUnder(e.getX(), e.getY());
                // Notify the even
                listener.onLongClickItem(v, rv.getChildAdapterPosition(v));
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View v = rv.findChildViewUnder(e.getX(), e.getY());
                // Notify the even
                listener.onClickItem(v, rv.getChildAdapterPosition(v));
                return true;
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        return ( child != null && gd.onTouchEvent(e));
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
