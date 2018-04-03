package com.zuccessful.a2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.BaseAdapter;

public class MainActivity extends AppCompatActivity implements Fragment_List.OnFragmentInteractionListener{
    static Boolean test=false;
    android.support.v4.app.FragmentManager fragmentManager;
    Fragment_Detail frag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void intentCall(String id) {
        frag2 = (Fragment_Detail) fragmentManager.findFragmentById(R.id.fragment_detail);

        if(frag2 == null) {
            Intent intent = new Intent(MainActivity.this, Activity_two.class);
            intent.putExtra("contact_id", id);
            startActivity(intent);
        }
        else {
            test = true;
            frag2.detailIntent(id);
        }
    }

}
