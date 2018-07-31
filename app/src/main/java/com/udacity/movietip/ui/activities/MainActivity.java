package com.udacity.movietip.ui.activities;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.udacity.movietip.R;
import com.udacity.movietip.ui.fragments.MovieGridFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    /*
    REFERENCES:
    https://developer.android.com/reference/com/google/android/material/bottomnavigation/BottomNavigationView
    https://developer.android.com/guide/components/fragments
    https://developer.android.com/guide/components/fragments#Lifecycle
    https://developer.android.com/topic/performance/graphics/cache-bitmap#config-changes
    https://developer.android.com/topic/libraries/architecture/saving-states
    https://developer.android.com/training/data-storage/sqlite
    https://developer.android.com/guide/components/intents-common#PlayMedia
    https://developer.android.com/training/data-storage/sqlite
     */


    /*
      Reference: https://www.simplifiedcoding.net/bottom-navigation-android-example
      Reference: https://materialdoc.com/components/bottom-navigation/
     */
    private static final String MOVIES_POPULAR = "popular";
    private static final String MOVIES_TOP_RATED = "top_rated";
    private static final String MOVIES_NOW_PLAYING = "now_playing";
    private static final String MOVIES_FAVORITES = "favorites";
    private String fragmentTag = "";
    private FragmentManager fragmentManager;
    private BottomNavigationView navigationBottom;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolBar = findViewById(R.id.main_activity_toolbar);

        // For testing glide when network is down.
        // IMPORTANT!!! If this is enabled and there are no override options in the Glide load,
        // then scroll state does not get restored automatically for the poster images recyclerview.
        //Glide.get(this).clearMemory();

        /*
         Reference: https://developer.android.com/training/basics/fragments/fragment-ui
        */
        navigationBottom = findViewById(R.id.main_activity_navigation);
        navigationBottom.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.navigation_popular:
                    toolBar.setTitle(R.string.title_popular);
                    // Provide argument to fragment constructor to build fragment and call correct api path
                    // Reference: https://developer.android.com/reference/android/app/Fragment
                    fragmentTag = MOVIES_POPULAR;
                    break;
                case R.id.navigation_top_rated:
                    toolBar.setTitle(R.string.title_top_rated);
                    fragmentTag = MOVIES_TOP_RATED;
                    break;
                case R.id.navigation_now_playing:
                    toolBar.setTitle(R.string.title_now_playing);
                    fragmentTag = MOVIES_NOW_PLAYING;
                    break;
                case R.id.navigation_favorites:
                    toolBar.setTitle(R.string.title_favorites);
                    fragmentTag = MOVIES_FAVORITES;
                    break;
            }

            initFragment();

            return true;
        });

        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getFragments().isEmpty()){
            navigationBottom.setSelectedItemId(R.id.navigation_popular);
        }

        /* Reference: https://stackoverflow.com/a/44849095
         * Show/hide BottomNavigationView - this "seems" a more elegant solution than overriding coordinatorlayout behavior. Maybe that's premature or naive at this point?
         * Reference: https://developer.android.com/reference/android/support/design/widget/AppBarLayout.OnOffsetChangedListener
         */
        ((AppBarLayout)findViewById(R.id.main_activity_app_bar_layout))
                .addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
                    // move Bottom navigation view in the opposite direction of the appbar on the y axis.
                    navigationBottom.setTranslationY(verticalOffset*-1);
                });
    }


    /* Reference: https://www.simplifiedcoding.net/bottom-navigation-android-example/
       Per Material Design: "On Android, the Back button does not navigate between bottom navigation destinations."
       Hence, I will not add the fragments to back stack.
       Reference: https://material.io/design/components/bottom-navigation.html#behavior
    */
    private void initFragment() {
        // Per Material Design transition between bottom navigation views using cross-fade animation
        // Reference: https://material.io/design/components/bottom-navigation.html#behavior
        int fadeIn = android.R.anim.fade_in;
        int fadeOut = android.R.anim.fade_out;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(fadeIn, fadeOut, fadeIn, fadeOut );

        Fragment taggedFragment = fragmentManager.findFragmentByTag(fragmentTag);

        if ( taggedFragment != null){
            fragmentTransaction.show(taggedFragment);
        } else {
            taggedFragment = new MovieGridFragment().newInstance(fragmentTag);
            fragmentTransaction.add(R.id.main_activity_fragment_container, taggedFragment, fragmentTag);
        }

        // get a list of fragments currently added to the FragmentManager
        List<Fragment> fragmentList = fragmentManager.getFragments();

        for (Fragment fragment : fragmentList){
            if (!fragment.equals(taggedFragment)){
                fragmentTransaction.hide(fragment);
            }
        }

        fragmentTransaction.commitNow();
    }
}
