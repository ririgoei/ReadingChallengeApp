package edu.sjsu.rmarcelita.readingchallengeapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.ArrayList;
import java.util.List;

public class DiscoverHorizontalActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_horizontal);

        ArrayList<String> listContents = new ArrayList<>();
        listContents.add("Events Near Me");
        listContents.add("Libraries Near Me");
        listContents.add("What's Tweetin'?");
        ListView discoverList = (ListView) findViewById(R.id.masterListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, listContents);
        discoverList.setAdapter(adapter);

        Configuration config = getResources().getConfiguration();
        final Intent intent_vertical = new Intent(this, DiscoverActivity.class);

        if(config.orientation == Configuration.ORIENTATION_PORTRAIT) {
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
                if(currentItem.equals("Events Near Me")) {
                    displayEvents();
                } else if(currentItem.equals("Libraries Near Me")) {
                    displayMap(switcher);
                } else {
                    displayTwitter(switcher);
                }
            }
        });

    }

    public void displayEvents() {

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
//        View frag = (View) findViewById(R.id.contentFragment);
//        fragmentLinear.setVisibility(frag.GONE);
//        fragmentLinear.setVisibility(((View) tweetRecycle).VISIBLE);
    }

    public void displayMap(ViewSwitcher switcher) {
        final LinearLayout fragmentLinear = (LinearLayout) findViewById(R.id.fragmentContentLinearLayout);
        View frag = (View) findViewById(R.id.contentFragment);
        final RecyclerView tweetRecycle = (RecyclerView) findViewById(R.id.twitterRecycleViewDiscover);
        //fragmentLinear.setVisibility(frag.VISIBLE);
        //fragmentLinear.setVisibility(((View) tweetRecycle).GONE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFragment);
        mapFragment.getMapAsync(this);
        switcher.showPrevious();
    }

    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in current location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
