package com.udacity.movietip.ui.activities;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.udacity.movietip.R;
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
    TODO Design details fragment.
    TODO On click display details activity.
    TODO Handle null pointer exceptions
    TODO Check for nulls
    TODO Polish the api
    TODO What is strings.xml supposed to hold? strings.xml translatable
    TODO Tablet layout
    TODO Debugging and linting

	use parcelable to create a parcel to use in saving state
	override onSaveInstanceState to save state for each fragment's parcel
Inside the MainActivity:
	check for a saved instance state, if it exists: unpack the parcel and restore the savedInstanceState objects
		else instantiate a MasterGridFragment for each: popular, top rated, and favorites
		instantiated fragment passes static final string to recycler view to obtain data

     */

    private static final String TAG = "MainActivity";

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreated started");
        /*
         Reference: https://developer.android.com/training/basics/fragments/fragment-ui
         Check that the activity is using the layout version with the movie_grid_fragment_container FrameLayout */

        /* Default initial fragment creation to the popular movies category
        *  TODO restore last bottomnavigation item user was viewing else init fragment with popular movies */

        // If I uncomment this then it crashes with "The specified child already has a parent. You must call removeView() on the child's parent first.
        initFragment(new MasterGridFragment().newInstance(getString(R.string.movies_popular_path)));

        Log.d(TAG, "onCreate init default fragment.");

        BottomNavigationView navigationBottom = (BottomNavigationView) findViewById(R.id.navigation);
        navigationBottom.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Log.d(TAG, "onCreate bottom navigation view set up finished.");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_popular:
                    // Provide argument to fragment constructor to build fragment and call correct api path
                    // Reference: https://developer.android.com/reference/android/app/Fragment
                    fragment = new MasterGridFragment().newInstance(getString(R.string.movies_popular_path));
                    Log.d(TAG, "Popular Fragment created.");
                    break;
                case R.id.navigation_top_rated:
                    fragment = new MasterGridFragment().newInstance(getString(R.string.movies_top_rated_path));
                    Log.d(TAG, "Top Rated Fragment created.");
                    break;
                case R.id.navigation_now_playing:
                    fragment = new MasterGridFragment().newInstance(getString(R.string.movies_now_playing_path));
                    Log.d(TAG, "Now Playing Fragment created.");
                    break;
                /*case R.id.navigation_favorites:
                    fragment = new MasterGridFragment();
                    break;*/
            }
            return initFragment(fragment);
        }
    };



    // Reference: https://www.simplifiedcoding.net/bottom-navigation-android-example/
    private boolean initFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onImageSelected(int position) {

    }
}
