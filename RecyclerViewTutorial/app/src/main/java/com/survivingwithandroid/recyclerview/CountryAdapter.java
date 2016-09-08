package com.survivingwithandroid.recyclerview;

/*
 * Copyright (C) 2016, francesco Azzola 
 *
 *(http://www.survivingwithandroid.com)
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
 *
 * 14/08/16
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {

    private List<Country> countryList;

    /**
     * View holder class
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView countryText;
        public TextView popText;

       public MyViewHolder(View view) {
           super(view);
           countryText = (TextView) view.findViewById(R.id.countryName);
           popText = (TextView) view.findViewById(R.id.pop);
       }
    }

    public CountryAdapter(List<Country> countryList) {
        this.countryList = countryList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        System.out.println("Bind ["+holder+"] - Pos ["+position+"]");
        Country c = countryList.get(position);
        holder.countryText.setText(c.name);
        holder.popText.setText(String.valueOf(c.population));
    }

    @Override
    public int getItemCount() {
        Log.d("RV", "Item size ["+countryList.size()+"]");
       return countryList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent, false);
        return new MyViewHolder(v);
    }
}
