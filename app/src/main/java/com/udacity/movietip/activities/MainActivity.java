package com.udacity.movietip.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.udacity.movietip.R;
import com.udacity.movietip.data.model.MoviesModel;
import com.udacity.movietip.data.remote.ApiService;
import com.udacity.movietip.utils.ApiUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    /*
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
Inside the MovieGridFragment, I will
	inflate the fragment_movie_grid
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
3. Create a new fragment instance in a host activity
4. Add the fragment to it's host activity using the FragmentManager and a fragment transaction
     */

    private ApiService mService;
    private TextView mTextView;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_favorites:
                    mTextMessage.setText(R.string.title_favorites);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextView = (TextView) findViewById(R.id.test_json_return);
        mService = ApiUtils.getApiService(this);

        //loadMoviesData();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void loadMoviesData(){
        // abstract the method further so it's getMoviesObject(String objectType)

        String apiPath = this.getString(R.string.movies_popular_path);
        mService.getJSON(apiPath).enqueue(new Callback<MoviesModel>() {
                @Override
            public void onResponse(Call<MoviesModel> call, Response<MoviesModel> response) {
                if (response.body() != null) {
                    mTextView.setText(response.body().getTotalResults().toString());
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
}
