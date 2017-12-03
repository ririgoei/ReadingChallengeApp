package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadOnActivity extends AppCompatActivity {

    private SQLiteHelper db;
    private InputStream inputStream;
    private BufferedReader br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_on);
        db = new SQLiteHelper(this);
        db.createBooksInfoTable();
        inputStream = getResources().openRawResource(R.raw.books);
        br = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = br.readLine()) != null) {
                String[] row = csvLine.split(",");
                int pages = Integer.parseInt(row[3]);
                double stars = Double.parseDouble(row[5]);
                db.insertBooksInfoTable(row[0], row[1], row[2], pages, row[4], stars);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor gyroscopeSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        final Button testButton = (Button) findViewById(R.id.button2);
        SensorEventListener gyroSensorListener = new SensorEventListener() {
            int count = 0;
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent.values[2] > 0.5f) { // anticlockwise
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                    testButton.setText("Button: " + count--);
                } else if(sensorEvent.values[2] < -0.5f) { // clockwise
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                    testButton.setText("Button: " + count++);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sm.registerListener(gyroSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
