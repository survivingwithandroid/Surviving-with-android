package com.survivingwithandroid.androidsnackbar;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View v = (View) findViewById(R.id.main);

        final TextView tv = (TextView) findViewById(R.id.data);

        Button b = (Button) findViewById(R.id.btn);

        //Snackbar.make(v, "Welcome to SwA", Snackbar.).show();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Activate snack bar


                Snackbar bar = Snackbar.make(v, "Weclome to SwA", Snackbar.LENGTH_LONG)
                         .setAction("Dismiss", new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                tv.setText("You pressed Dismiss!!");
                             }
                         });

                bar.setActionTextColor(Color.RED);

                TextView tv = (TextView) bar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.CYAN);
                bar.show();
            }
        });
    }
}
