package com.udacity.movietip.ui.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.udacity.movietip.R;
import com.udacity.movietip.data.model.Movie;
import com.udacity.movietip.ui.fragments.MasterGridFragment;

public class MainActivity extends AppCompatActivity implements MasterGridFragment.OnImageClickListener{

    /*
    https://www.simplifiedcoding.net/bottom-navigation-android-example
    https://materialdoc.com/components/bottom-navigation/
    DONE Generify the Interface methods. Test for OK response.
    DONE bottom navigation in main
    DONE listen for clicks and set navigation view accordingly
    DONE fragments for bottom navigation
    DONE Add a recycler view. Test for Ok response.
    DONE Display data in recyclerview gridview.
    DONE Respond to click with toast message.
    DONE Design details fragment: Add rating bar and other information with divider
    TODO vote average comes in as Vote Average is: 7.4. But my 5 stars shows as 5 stars. what's the math to get 5 stars to work?
    DONE popularity rating is wrapping vertically
    DONE What are the dimensions of the poster? Don't center crop, dynamically size the poster view.
    DONE put glide image loading into separate helper class
    DONE On click display details activity.
    DONE implement up navigation for detail activity
    DONE implement up navigation for fragments??? No, per material design it should just exit the app.
    DONE Where should I do the check if online? Am I loading the images in the right place? If I'm offline, Glide still tries to load the posters and the backdrop.
    TODO doesn't crash when not online but shows broken imageplaceholder in detailactivity for backdrop image only. use glide target?
    TODO doesn't crash when not online but shows broken imageplaceholder in viewholder's that were not visible when connection went down
    TODO crashes: load app; then put in plane mode; tap top rate and now playing appropriate not connected message pops; connect to internet; tap top rated and now playing NOTHING GETS CREATED.
    DONE Horizontal layout
    TODO Handle null pointer exceptions
    DONE Check for nulls
    TODO Polish the api
    TODO What is strings.xml supposed to hold? strings.xml translatable
    DONE LATER Tablet layout?? No
    TODO Debugging and linting
    TODO to get rid of topbar (actionbar) need to apply parent theme in style.xml to one that is noactionbar. then I can do a collapsing toolbar with the app_name in coordinator layout. https://www.youtube.com/watch?v=f4kpysbsIeI

	use parcelable to create a parcel to use in saving state
	override onSaveInstanceState to save state for each fragment's parcel
Inside the MainActivity:
	check for a saved instance state, if it exists: unpack the parcel and restore the savedInstanceState objects
		else instantiate a MasterGridFragment for each: popular, top rated, and favorites
		instantiated fragment passes static final string to recycler view to obtain data

     */

    private static final String TAG = "MainActivity";
    public static final String MOVIE_ITEM = "Movie Item";
    public static final String MOVIES_POPULAR_PATH = "popular";
    public static final String MOVIES_TOP_RATED_PATH = "top_rated";
    public static final String MOVIES_NOW_PLAYING_PATH = "now_playing";
    BottomNavigationView navigationBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Glide.get(this).clearMemory();

        /*
         Reference: https://developer.android.com/training/basics/fragments/fragment-ui
         Check that the activity is using the layout version with the movie_grid_fragment_container FrameLayout */
        navigationBottom = findViewById(R.id.navigation);
        navigationBottom.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /* Default initial fragment creation to the popular movies category*/
        if (savedInstanceState == null) {
            navigationBottom.setSelectedItemId(R.id.navigation_popular);
        }

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            String tag = "";

            switch (item.getItemId()) {
                case R.id.navigation_popular:
                    // Provide argument to fragment constructor to build fragment and call correct api path
                    // Reference: https://developer.android.com/reference/android/app/Fragment
                    tag = MOVIES_POPULAR_PATH;
                    break;
                case R.id.navigation_top_rated:
                    tag = MOVIES_TOP_RATED_PATH;
                    break;
                case R.id.navigation_now_playing:
                    tag = MOVIES_NOW_PLAYING_PATH;
                    break;
            }
            return initFragment(tag);
        }
    };



    /* Reference: https://www.simplifiedcoding.net/bottom-navigation-android-example/
       Per Material Design: "On Android, the Back button does not navigate between bottom navigation destinations."
       Hence, I will not add the fragments to back stack.
       Reference: https://material.io/design/components/bottom-navigation.html#behavior
    */
    private boolean initFragment(String tag){

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
        return true;
    }

    @Override
    public void onImageSelected(Movie movie) {

        final Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE_ITEM, movie);

        startActivity(intent);
    }
}
