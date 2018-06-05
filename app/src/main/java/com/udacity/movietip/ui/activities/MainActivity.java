package com.udacity.movietip.ui.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
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

    TODO Handle null pointer exceptions
    TODO What is strings.xml supposed to hold? strings.xml translatable
    TODO Debugging and linting

     */

    private static final String MOVIE_ITEM = "Movie Item";
    private static final String MOVIES_POPULAR_PATH = "popular";
    private static final String MOVIES_TOP_RATED_PATH = "top_rated";
    private static final String MOVIES_NOW_PLAYING_PATH = "now_playing";
    private BottomNavigationView navigationBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For testing glide when network is down
        Glide.get(this).clearMemory();

        /*
         Reference: https://developer.android.com/training/basics/fragments/fragment-ui
         Check that the activity is using the layout version with the movie_grid_fragment_container FrameLayout */
        navigationBottom = findViewById(R.id.navigation);
        navigationBottom.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


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
        Boolean flag = true;

        if (taggedFragment == null) {
            taggedFragment = new MasterGridFragment().newInstance(tag);
            flag = false;
        }

        fragmentTransaction
                .replace(R.id.fragment_container, taggedFragment, tag)
                .commit();

        return flag;
    }

    @Override
    public void onImageSelected(Movie movie) {

        final Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE_ITEM, movie);
        startActivity(intent);
    }
}
