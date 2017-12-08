package edu.sjsu.rmarcelita.readingchallengeapp;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import android.app.ListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mopub.volley.Request;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.GEOMETRY;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.GOOGLE_BROWSER_API_KEY;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.ICON;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.LATITUDE;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.LIBRARY_ID;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.LOCATION;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.LONGITUDE;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.NAME;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.OK;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.PLACE_ID;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.PROXIMITY_RADIUS;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.REFERENCE;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.STATUS;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.TAG;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.VICINITY;
import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.ZERO_RESULTS;


public class DiscoverActivity extends AppCompatActivity {

    private int MY_PERMISSION_READ_FINE_LOCATION = 1;
    private int MY_PERMISSION_READ_COARSE_LOCATION = 1;
    private boolean checked = false;
    public ArrayList<String> libs = new ArrayList<>();
    private SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Twitter.initialize(this);
        db = new SQLiteHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("riri_goei")
                .build();
        final TweetTimelineRecyclerViewAdapter adapter =
                new TweetTimelineRecyclerViewAdapter.Builder(this)
                        .setTimeline(userTimeline)
                        .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                        .build();

        final RecyclerView tweetRecycle = (RecyclerView) findViewById(R.id.twitterRecycleView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        tweetRecycle.setLayoutManager(mLayoutManager);
        tweetRecycle.getLayoutParams().height = 1000;
        tweetRecycle.getLayoutParams().width = 1100;
        tweetRecycle.setAdapter(adapter);

        Configuration config = getResources().getConfiguration();
        final Intent intent_horizontal = new Intent(this, DiscoverHorizontalActivity.class);

        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            startActivity(intent_horizontal);
        }

        TextView libraries = (TextView) findViewById(R.id.librariesNearTextView);

        final Intent intent_home = new Intent(this, HomeActivity.class);

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_READ_COARSE_LOCATION);

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_READ_FINE_LOCATION);

        LocationManager locManager;
        Location l;

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            checked = true;
        }

        if(checked) {
            locManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            if (LocationManager.NETWORK_PROVIDER != null) {
                l = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else {
                l = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
                libraries.setText("Libraries Near: " + addresses.get(0).getLocality() + ", " +
                        addresses.get(0).getPostalCode());
                loadNearByPlaces(l.getLatitude(), l.getLongitude());
                Log.v("Test", "Size is now: " + libs.size());
                for(int i = 0; i < libs.size(); i++) {
                    Log.v("Libs", "Libs: " + libs.get(i));
                }
            } catch (Exception e) {
                Log.v("Error", "Address not found.");
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(intent_home);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void loadNearByPlaces(double latitude, double longitude) {
        String type = "library";
        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlacesUrl.append("&radius=").append(PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=").append(type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + GOOGLE_BROWSER_API_KEY);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, googlePlacesUrl.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        Log.i(TAG, "onResponse: Result= " + result.toString());
                        parseLocationResult(result);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: Error= " + error);
                Log.e(TAG, "onErrorResponse: Error= " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    public void parseLocationResult(JSONObject result) {

        String id, place_id, placeName = null, reference, icon, vicinity = null;
        double latitude, longitude;

        try {
            JSONArray jsonArray = result.getJSONArray("results");

            if (result.getString(STATUS).equalsIgnoreCase(OK)) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject place = jsonArray.getJSONObject(i);

                    id = place.getString(LIBRARY_ID);
                    place_id = place.getString(PLACE_ID);
                    if (!place.isNull(NAME)) {
                        placeName = place.getString(NAME);
                    }
                    if (!place.isNull(VICINITY)) {
                        vicinity = place.getString(VICINITY);
                    }
                    latitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                            .getDouble(LATITUDE);
                    longitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                            .getDouble(LONGITUDE);
                    reference = place.getString(REFERENCE);
                    icon = place.getString(ICON);
                    libs.add(placeName);
                }
                Log.v("Test", "First one is: " + libs.get(0));
                Log.v("Test", "Libraries shown: " + libs.size());
//                Toast.makeText(getBaseContext(), jsonArray.length() + " libraries found!",
//                        Toast.LENGTH_SHORT).show();
            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {
//                Toast.makeText(getBaseContext(), "No libraries found in 5KM radius!!!",
//                        Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {

            e.printStackTrace();
            Log.e(TAG, "parseLocationResult: Error=" + e.getMessage());
        }
    }
}
