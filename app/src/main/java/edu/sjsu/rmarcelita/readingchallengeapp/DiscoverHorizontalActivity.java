package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mopub.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static edu.sjsu.rmarcelita.readingchallengeapp.AppConfig.*;

import java.util.ArrayList;

public class DiscoverHorizontalActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private int MY_PERMISSION_READ_FINE_LOCATION = 1;
    private GoogleMap mMap;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_horizontal);

        ArrayList<String> listContents = new ArrayList<>();
        listContents.add("Libraries Near Me");
        listContents.add("What's Tweetin'?");
        ListView discoverList = (ListView) findViewById(R.id.masterListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, listContents);
        discoverList.setAdapter(adapter);

        Configuration config = getResources().getConfiguration();
        final Intent intent_vertical = new Intent(this, DiscoverActivity.class);

        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            startActivity(intent_vertical);
        }

        final Intent intent_home = new Intent(this, HomeActivity.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(intent_home);
                return true;
            }
        });

        final ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.discoverViewSwitcher);

        discoverList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String currentItem = ((TextView) view).getText().toString();
                if (currentItem.equals("Libraries Near Me")) {
                    displayMap(switcher);
                } else {
                    displayTwitter(switcher);
                }
            }
        });

    }

    public void displayTwitter(ViewSwitcher switcher) {
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("riri_goei")
                .build();

        final TweetTimelineRecyclerViewAdapter adapter =
                new TweetTimelineRecyclerViewAdapter.Builder(this)
                        .setTimeline(userTimeline)
                        .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                        .build();

        final LinearLayout fragmentLinear = (LinearLayout) findViewById(R.id.fragmentContentLinearLayout);
        final RecyclerView tweetRecycle = (RecyclerView) findViewById(R.id.twitterRecycleViewDiscover);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        tweetRecycle.setLayoutManager(mLayoutManager);
        tweetRecycle.setAdapter(adapter);
        switcher.showNext();
    }

    public void displayMap(ViewSwitcher switcher) {
        if (!isGooglePlayServicesAvailable()) {
            return;
        }
        final LinearLayout fragmentLinear = (LinearLayout) findViewById(R.id.fragmentContentLinearLayout);
        View frag = (View) findViewById(R.id.contentFragment);
        final RecyclerView tweetRecycle = (RecyclerView) findViewById(R.id.twitterRecycleViewDiscover);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFragment);
        mapFragment.getMapAsync(this);
        switcher.showPrevious();
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getCurrentLocation(mMap);
    }

    private void getCurrentLocation(GoogleMap googleMap) {
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_READ_FINE_LOCATION);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
        }
        LocationManager locManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Location l;
        if (LocationManager.NETWORK_PROVIDER != null) {
            l = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } else {
            l = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        LatLng currLocation = new LatLng(l.getLatitude(), l.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(currLocation)
                .title("Marker in current location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
        locManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 3000, 0, this);
    }

    private void loadNearByPlaces(double latitude, double longitude) {
        Log.v("Test", "Gets to loadNearbyPlaces method");
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

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void parseLocationResult(JSONObject result) {

        String id, place_id, placeName = null, reference, icon, vicinity = null;
        double latitude, longitude;

        try {
            JSONArray jsonArray = result.getJSONArray("results");

            if (result.getString(STATUS).equalsIgnoreCase(OK)) {

                mMap.clear();

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

                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(latitude, longitude);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);

                    mMap.addMarker(markerOptions);
                }

                Toast.makeText(getBaseContext(), jsonArray.length() + " libraries found!",
                        Toast.LENGTH_SHORT).show();
            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {
                Toast.makeText(getBaseContext(), "No libraries found in 5KM radius!!!",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {

            e.printStackTrace();
            Log.e(TAG, "parseLocationResult: Error=" + e.getMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        Log.v("Test", "Gets to onLocationChanged method");

        loadNearByPlaces(latitude, longitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
