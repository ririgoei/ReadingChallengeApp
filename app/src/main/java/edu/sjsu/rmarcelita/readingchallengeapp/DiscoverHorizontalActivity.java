package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class DiscoverHorizontalActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;

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

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.contentsFragment);
//
//        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in current location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
