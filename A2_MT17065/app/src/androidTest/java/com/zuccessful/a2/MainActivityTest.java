package com.zuccessful.a2;

import android.support.test.espresso.ViewInteractionModule_ProvideViewFinderFactory;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> testMain = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mainActivity;

    @Before
    public void setUp() throws Exception {
        mainActivity = testMain.getActivity();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void checkActivity() {
        View view = mainActivity.findViewById(R.id.fragment_list);
        assertNotNull(view);
    }

    public void testTabletFrag()
    {
        if(MainActivity.test == true)
        {
            View view = mainActivity.findViewById(R.id.fragment_detail);
            assertNotNull(view);
        }
    }
    @Test
    public void intentCall() {
    }
}