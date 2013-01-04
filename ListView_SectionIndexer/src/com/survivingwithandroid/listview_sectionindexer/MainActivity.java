package com.survivingwithandroid.listview_sectionindexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.survivingwithandroid.listview_sectionindexer.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        List<String> countries = new ArrayList<String>();
        
        countries.add("Italy");
        countries.add("Spain");
        countries.add("France");
        countries.add("Germany");
        countries.add("United Kingdom");
        countries.add("Austria");
        countries.add("Ireland");
        countries.add("Portugal");
        countries.add("Belgium");
        countries.add("Denmark");
        countries.add("Finland");
        countries.add("Norway");
        countries.add("Sweden");
        countries.add("Netherlands");
        countries.add("Greece");
        countries.add("Luxembourg");
        countries.add("Malta");
        countries.add("Latvia");
        countries.add("Slovakia");
        countries.add("Slovenia");
        countries.add("Poland");
        countries.add("Hungary");
        countries.add("Romania");
        
        Collections.sort(countries);
        
        FastSearchListView listView = (FastSearchListView) findViewById(R.id.listview);
        
        SimpleAdapter sa = new SimpleAdapter(countries, this);
        listView.setAdapter(sa);
        
        //listView.setFastScrollEnabled(true);
        
    }

   
}
