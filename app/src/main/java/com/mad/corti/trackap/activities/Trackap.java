package com.mad.corti.trackap.activities;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mad.corti.trackap.R;
import com.mad.corti.trackap.activities.Data.FakesDataSets;
import com.mad.corti.trackap.activities.fragments.Friends;
import com.mad.corti.trackap.activities.fragments.Map;
import com.mad.corti.trackap.activities.fragments.Meetings;
import com.mad.corti.trackap.models.Friend;
import com.mad.corti.trackap.models.Meeting;

import java.util.ArrayList;

public class Trackap extends AppCompatActivity {

    private ArrayList<Friend>   _friendList;
    private ArrayList<Meeting>  _meetingList;

    private static final int PAGE_FRIENDS   = 0;
    private static final int PAGE_MAP       = 1;
    private static final int PAGE_MEETINGS  = 2;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    /**
     * public floating button so each fragment can change its behaviour.
     * TODO: Find a better solution.
     */
    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackap_main);

        // Init data set
        _friendList = FakesDataSets.fakeFriends();
        _meetingList = FakesDataSets.FakesMeetings();

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Setup the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Setup the tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                Log.d("DEBUG", "POS : " + position);
                switch (position) {
                    case PAGE_FRIENDS:
                        fab.setVisibility(View.VISIBLE);
                        break;
                    case PAGE_MAP:
                        fab.setVisibility(View.GONE);
                        break;
                    case PAGE_MEETINGS:
                        fab.setVisibility(View.VISIBLE);
                        break;
                    default:
                        fab.setVisibility(View.GONE);
                }
            }
        });

        // Initialize floating action button
        fab = (FloatingActionButton) findViewById(R.id.fab);

    }

    public ArrayList<Friend> getFriendList() {
        return _friendList;
    }

    public ArrayList<Meeting> getMeetingList() {
        return _meetingList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trackap_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case PAGE_FRIENDS:
                    return new Friends();
                case PAGE_MAP:
                    return new Map();
                case PAGE_MEETINGS:
                    return new Meetings();
                default:
                    return new Friends();
            }
        }

        /**
         * Total pages
         * @return number of pages
         */
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case PAGE_FRIENDS:
                    return "FRIENDS";
                case PAGE_MAP:
                    return "MAP";
                case PAGE_MEETINGS:
                    return "MEETINGS";
            }
            return null;
        }
    }
}
