package edu.sjsu.rmarcelita.readingchallengeapp;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class DiscoverActivity extends AppCompatActivity {

    private int MY_PERMISSION_READ_FINE_LOCATION = 1;
    private int MY_PERMISSION_READ_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        TextView latLng = (TextView) findViewById(R.id.latLongTextView);

        final Intent intent_home = new Intent(this, HomeActivity.class);

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_READ_COARSE_LOCATION);

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_READ_FINE_LOCATION);

        LocationManager locManager;

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            Location l;

            if (LocationManager.NETWORK_PROVIDER != null) {
                l = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else {
                l = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            //latLng.setText("Latitude and longitude: " + l.getLatitude() + " " + l.getLongitude());
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
// lat,lng, your current location
            try {
                List<Address> addresses = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
                latLng.setText("Postal code: " + addresses.get(0).getPostalCode());
            } catch(Exception e) {
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
