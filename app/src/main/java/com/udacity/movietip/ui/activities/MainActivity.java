package com.udacity.movietip.ui.activities;

import android.app.FragmentManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.udacity.movietip.R;
import com.udacity.movietip.data.model.MoviesModel;
import com.udacity.movietip.data.remote.ApiService;
import com.udacity.movietip.data.utils.ApiUtils;
import com.udacity.movietip.ui.fragments.MovieGridFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieGridFragment.OnFragmentInteractionListener{

    /*
    https://www.simplifiedcoding.net/bottom-navigation-android-example
    https://materialdoc.com/components/bottom-navigation/
    DONE Generify the Interface methods. Test for OK response.
    DONE bottom navigation in main
    DONE listen for clicks and set navigation view accordingly
    todo fragments for bottom navigation
    TODO Add a recycler view. Test for Ok response.
    TODO Display data in recyclerview gridview.
    TODO Respond to click with toast message.
    TODO Design details fragment.
    TODO On click display details activity.

    Inside the fragment layout, fragment_movie_grid, I will define the recycler view.
    Inside the MovieGridFragment, I will inflate the fragment_movie_grid
	get a reference to the recycler view inside the fragment layout
	add a click listener to handle clicks on the individual posters
	use parcelable to create a parcel to use in saving state
	override onSaveInstanceState to save state for each fragment's parcel
Inside the MainActivity:
	check for a saved instance state, if it exists: unpack the parcel and restore the savedInstanceState objects
		else instantiate a MovieGridFragment for each: popular, top rated, and favorites
		instantiated fragment passes static final string to recycler view to obtain data
Inside the interface
	if the passed in static final string matches then (have abstracted and provided methods for popular and toprated)
		query the api
Inside the Recycler View class:
	Abstract so each fragment can instantiate their own recycler view.
	Connect to the interface to query for the data to display by passing the static final string to the interface
Lesson 7 and 12 Background Tasks talks about loaders rewatch these and implement the loading here
	Exercise: T05b.02-Exercise-AddAsyncTaskLoader

1. Create a layout that defines the appearance of the fragment
2. Create a new fragment class that inflates a layout
main activity layout has to have a container for the fragment
3. Create a new fragment instance in a host activity (MainActivity)
4. Add the fragment to it's host activity using the FragmentManager and a fragment transaction
     */

    private ApiService mService;
    private TextView mTextMessage;
    private Response<MoviesModel> moviesResponse;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_popular:
                    return true;
                case R.id.navigation_top_rated:
                    return true;
                case R.id.navigation_now_playing:
                    return true;
                case R.id.navigation_favorites:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        final Fragment popularFragment = new MovieGridFragment();
        final Fragment topRatedFragment = new MovieGridFragment();
        final Fragment nowPlayingFragment = new MovieGridFragment();

        mService = ApiUtils.getApiService(this);

        loadMoviesData();

        /*
         Reference: https://developer.android.com/training/basics/fragments/fragment-ui
         Check that the activity is using the layout version with the movie_grid_fragment_container FrameLayout */

        mTextMessage = (TextView) findViewById(R.id.movie_grid_message);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()){
                    case R.id.navigation_popular:
                        fragmentTransaction.replace(R.id.movie_grid_fragment_container, popularFragment).commit();
                        return true;
                    case R.id.navigation_top_rated:
                        fragmentTransaction.replace(R.id.movie_grid_fragment_container, topRatedFragment).commit();
                        return true;
                    case R.id.navigation_now_playing:
                        fragmentTransaction.replace(R.id.movie_grid_fragment_container, nowPlayingFragment).commit();
                        return true;
                }
            }
        });
    }

    public void loadMoviesData(){
        // abstract the method further so it's getMoviesObject(String objectType)

        String apiPath = this.getString(R.string.movies_popular_path);
        mService.getJSON(apiPath).enqueue(new Callback<MoviesModel>() {
                @Override
            public void onResponse(Call<MoviesModel> call, Response<MoviesModel> response) {
                if (response.body() != null) {
                    moviesResponse = response;
                    Log.d("MainActivity", "data loaded from API");
                }else{
                    Log.d("MainActivity", "null returned from API");
                }
            }

            @Override
            public void onFailure(Call<MoviesModel> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}