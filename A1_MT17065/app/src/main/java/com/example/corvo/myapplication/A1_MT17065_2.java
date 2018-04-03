package com.example.corvo.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Locale;

public class A1_MT17065_2 extends AppCompatActivity {
    String language = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        language = bundle.getString("language");
        Locale myLocale = new Locale(language);
        Configuration conf = getResources().getConfiguration();
        conf.locale = myLocale;
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);
    }

    public void callIIITD(View v)
    {
        Intent i = new Intent(this, A1_MT17065_3.class);
        String data = getResources().getString(R.string.iiitd_detail);
        i.putExtra("data", data);
        i.putExtra("head", getResources().getString(R.string.iiit));
        i.putExtra("language", language);
        startActivity(i);
    }

    public void programs(View v)
    {
        Intent i = new Intent(this, A1_MT17065_3.class);
        String data = getResources().getString(R.string.courses);
        i.putExtra("data", data);
        i.putExtra("head", getResources().getString(R.string.programs));
        i.putExtra("language", language);
        startActivity(i);
    }

    public void admission(View v)
    {
        Intent i = new Intent(this, A1_MT17065_3.class);
        String data = getResources().getString(R.string.ug);
        i.putExtra("data", data);
        i.putExtra("head", getResources().getString(R.string.admsn));
        i.putExtra("language", language);
        startActivity(i);
    }
}

