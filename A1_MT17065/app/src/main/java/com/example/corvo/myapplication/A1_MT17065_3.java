package com.example.corvo.myapplication;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.util.Locale;

public class A1_MT17065_3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String lang = bundle.getString("language");
        Locale myLocale = new Locale(lang);
        Configuration conf = getResources().getConfiguration();
        conf.locale = myLocale;
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_iiitd);

        String head = bundle.getString("head");
        String data = bundle.getString("data");

        TextView tv_head = (TextView)findViewById(R.id.tv_iiitd1);
        tv_head.setText(head);

        TextView tv_data = (TextView)findViewById(R.id.tv_iiitd2);
        tv_data.setText(data);
    }
}
