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
import java.util.List;
import java.util.Locale;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;


public class DiscoverActivity extends AppCompatActivity {

    private int MY_PERMISSION_READ_FINE_LOCATION = 1;
    private int MY_PERMISSION_READ_COARSE_LOCATION = 1;
    private boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Twitter.initialize(this);

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
}
