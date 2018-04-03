package com.zuccessful.a2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

public class Activity_two extends AppCompatActivity {
//    private ArrayList<String> array;
    private ArrayAdapter<String> stringArrayAdapter;
    Fragment_Detail fragmentDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_layout);

        Intent getContact = getIntent();
        String contactId = getContact.getStringExtra("contact_id");

        fragmentDetail = (Fragment_Detail) getSupportFragmentManager().findFragmentById(R.id.fragment_detail);
        fragmentDetail.detailIntent(contactId);
    }
}
