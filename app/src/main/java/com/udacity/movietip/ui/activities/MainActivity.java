package com.udacity.movietip.ui.activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.udacity.movietip.R;
import com.udacity.movietip.data.model.Movie;
import com.udacity.movietip.data.utils.NetworkService;
import com.udacity.movietip.ui.fragments.MasterGridFragment;

public class MainActivity extends AppCompatActivity implements MasterGridFragment.OnImageClickListener{

    /*
    Best one fits my scenario. But need to refactor fragment first and separate api type fragment from db type fragment.
    api type: has a: pagedlist recyclerview and adapter, tmdb datasource
    db type: has a: regular recyclerview and adapter, db datasource

    http://androidkt.com/rest-api-pagination-paging-library/
    https://proandroiddev.com/8-steps-to-implement-paging-library-in-android-d02500f7fffe
    https://github.com/codepath/android_guides/wiki/Paging-Library-Guide
    http://codinginfinite.com/android-paging-library-example/



        DONE implement livedata for configuration changes and saving state
        TODO Trailer recyclerview and Reviews recyclerview do NOT push up toolbar imageview
        TODO Implement onsaveinstancestate for bottomnavigation
            https://developer.android.com/reference/com/google/android/material/bottomnavigation/BottomNavigationView
        TODO Implement onsaveinstancestate for scrolled position
            https://developer.android.com/guide/components/fragments
            https://developer.android.com/guide/components/fragments#Lifecycle
        TODO Implement onsaveinstancestate for detail activity
            https://developer.android.com/topic/performance/graphics/cache-bitmap#config-changes
            https://developer.android.com/topic/libraries/architecture/saving-states

        DONE Create Trailers models
        DONE Create Reviews models
        DONE modify apiservice to add reviews and trailers calls
        DONE add method to DetailActivity to call for trailers
        DONE add method to DetailActivity to call for reviews
        DONE Display trailers in detail activity
            if video then make api call and pass the movie id
        DONE Launch trailers with intent to youtube or other player
            https://developer.android.com/guide/components/intents-common#PlayMedia
            use glide to load it into imageview with thumbnail and intent to launch player
        DONE Display reviews in detail activity
            make api call and pass movie id to retrieve reviews

        DONE Implement database for favorites using room
            https://developer.android.com/training/data-storage/sqlite
        DONE Add favorites fragment -
        DONE pull favorites from database or else nothing
        DONE display favorites from database
            Need to add check if tag is favorites, then do different calls to database
        DONE Detail activity add button or other to add/deleteMovie favorites from database
            https://developer.android.com/training/data-storage/sqlite

        DONE implement broadcast receiver as jobservice to check internet
        TODO implement local broadcast receiver (in NetworkService.java) for checking internet so fragments can do something with this info

        DONE Bottomnavigation can't tell which item is selected as the text and icon don't stand out.
            this is because you HAVE to click twice for it to recognize you selected a bottomnavigation item
        DONE Change Detail activity to be a cardview
        TODO Change rating bar from 10 stars to 5
        TODO Adhere to material design for margins, etc.
        DONE Make trailers and reviews clickable
        todo document references
        todo two bugs make app not adhere to rubric. 1. on rotation needs to use livedata and not onsaveinstancestate 2. favorites tab does not refresh when I toggle a favorite from the favorites tab. see steps to recreate in notepadqq or git commit

     */

    /*
      Reference: https://www.simplifiedcoding.net/bottom-navigation-android-example
      Reference: https://materialdoc.com/components/bottom-navigation/
     */

    private static final String MOVIE_ITEM = "Movie Item";
    private static final String MOVIES_POPULAR_PATH = "popular";
    private static final String MOVIES_TOP_RATED_PATH = "top_rated";
    private static final String MOVIES_NOW_PLAYING_PATH = "now_playing";
    private static final String MOVIES_FAVORITES = "favorites";
    private static Context mContext;
    private BottomNavigationView navigationBottom;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // scheduleNetworkCheckJob();

        mContext = this;
        setContentView(R.layout.activity_main);

        toolBar = findViewById(R.id.toolbar);

        // For testing glide when network is down
        Glide.get(this).clearMemory();

        /*
         Reference: https://developer.android.com/training/basics/fragments/fragment-ui
         Check that the activity is using the layout version with the movie_grid_fragment_container FrameLayout */
        navigationBottom = findViewById(R.id.navigation);
        navigationBottom.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /* Default initial fragment creation to the popular movies category*/
        if (savedInstanceState == null) {
            navigationBottom.setSelectedItemId(R.id.navigation_popular);
            toolBar.setTitle(R.string.title_popular);
        }


        /* Reference: https://stackoverflow.com/a/44849095
         * Show/hide BottomNavigationView - this "seems" a more elegant solution than overriding coordinatorlayout behavior. Maybe that's premature or naive at this point?
         * Reference: https://developer.android.com/reference/android/support/design/widget/AppBarLayout.OnOffsetChangedListener
         */
        ((AppBarLayout)findViewById(R.id.app_bar_layout))
                .addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // move Bottom navigation view in the opposite direction of the appbar on the y axis.
                navigationBottom.setTranslationY(verticalOffset*-1);
            }
        });
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            String tag = "";

            switch (item.getItemId()) {
                case R.id.navigation_popular:
                    toolBar.setTitle(R.string.title_popular);
                    // Provide argument to fragment constructor to build fragment and call correct api path
                    // Reference: https://developer.android.com/reference/android/app/Fragment
                    tag = MOVIES_POPULAR_PATH;
                    break;
                case R.id.navigation_top_rated:
                    toolBar.setTitle(R.string.title_top_rated);
                    tag = MOVIES_TOP_RATED_PATH;
                    break;
                case R.id.navigation_now_playing:
                    toolBar.setTitle(R.string.title_now_playing);
                    tag = MOVIES_NOW_PLAYING_PATH;
                    break;
                case R.id.navigation_favorites:
                    toolBar.setTitle(R.string.title_favorites);
                    tag = MOVIES_FAVORITES;
                    break;
            }

            initFragment(tag);

            return true;
        }
    };


    /* Reference: https://www.simplifiedcoding.net/bottom-navigation-android-example/
       Per Material Design: "On Android, the Back button does not navigate between bottom navigation destinations."
       Hence, I will not add the fragments to back stack.
       Reference: https://material.io/design/components/bottom-navigation.html#behavior
    */
    private void initFragment(String tag){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment taggedFragment;
        taggedFragment = fragmentManager.findFragmentByTag(tag);

        if (taggedFragment == null) {
            taggedFragment = new MasterGridFragment().newInstance(tag);
        }

        fragmentTransaction
                .replace(R.id.fragment_container, taggedFragment, tag)
                .commit();
        }

    @Override
    public void onImageSelected(Movie movie) {
        final Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE_ITEM, movie);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Intent startNetworkServiceIntent = new Intent(this, NetworkService.class);
        //startService(startNetworkServiceIntent);
    }

    @Override
    protected void onStop() {
        //stopService(new Intent(this, NetworkService.class));
        super.onStop();
    }

    private void scheduleNetworkCheckJob(){
        JobInfo jobInfo = new JobInfo.Builder(0, new ComponentName(this, NetworkService.class))
                .setRequiresCharging(false)
                .setMinimumLatency(1000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(false)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(jobInfo);
        }
    }
}
