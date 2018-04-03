package com.example.corvo.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

public class A1_MT17065_1 extends AppCompatActivity {
    static String language = "en";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale(language);
        setContentView(R.layout.activity_main0);
        setTitle(R.string.app_name);
        Log.d("mainActivity0", "here");

        final Spinner spinner = (Spinner)findViewById(R.id.spinner);
        Button button = (Button)findViewById(R.id.spinner_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(),"Select a language",Toast.LENGTH_SHORT).show();
                }
                else if(spinner.getSelectedItemPosition() == 1) {
                    language = "en";
                    setLocale("en");
                    recreate();
                    Intent refresh = new Intent(getApplicationContext(), A1_MT17065_2.class);
                    refresh.putExtra("language", "en");
                    startActivity(refresh);

                }
                else if(spinner.getSelectedItemPosition() == 2) {
                    language = "hi";
                    setLocale("hi");
                    recreate();
                    Intent refresh = new Intent(getApplicationContext(), A1_MT17065_2.class);
                    refresh.putExtra("language", "hi");
                    startActivity(refresh);
                }
            }
        });
    }

    public void setLocale(String lang) {
        Log.d("A1_MT17065_1", Locale.getDefault().toString());
        Locale myLocale = new Locale(lang);
        Configuration conf = getResources().getConfiguration();
        conf.locale = myLocale;
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
        Log.d("A1_MT17065_1", Locale.getDefault().toString());

    }
}
