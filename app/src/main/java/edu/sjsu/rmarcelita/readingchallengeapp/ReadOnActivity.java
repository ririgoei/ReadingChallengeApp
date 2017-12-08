package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class ReadOnActivity extends AppCompatActivity {

    public static final String BOOK_TITLE_EXTRAMSG = "edu.sjsu.rmarcelita.readingchallengeapp.MESSAGE";
    private SQLiteHelper db;
    private InputStream inputStream;
    private InputStream inputDetailStream;
    private BufferedReader br;
    private BufferedReader brDetail;
    private final int MAX_BOOKS = 31;
    private final int MIN_BOOKS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_on);
        db = new SQLiteHelper(this);
        db.createBooksInfoTable();
        inputStream = getResources().openRawResource(R.raw.books);
        inputDetailStream = getResources().openRawResource(R.raw.booksynopsis);
        brDetail = new BufferedReader(new InputStreamReader(inputDetailStream));
        br = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine, csvLineDetail;
            String row[] = new String[6];
            String rowDetails[] = new String[2];
            String synopsis = "";
            int pages = 0;
            double stars = 0.00;
            while ((csvLine = br.readLine()) != null) {
                row = csvLine.split(",");
                pages = Integer.parseInt(row[3]);
                stars = Double.parseDouble(row[5]);
                db.insertBooksInfoTable(row[0], row[1], row[2], "", pages, row[4], stars);
            }
            while((csvLineDetail = brDetail.readLine()) != null) {
                rowDetails = csvLineDetail.split("_");
                synopsis = rowDetails[1];
                db.updateSynopsis(rowDetails[0], synopsis);
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }

        getRandomBook();
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor gyroscopeSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        SensorEventListener gyroSensorListener = new SensorEventListener() {
            int count = 0;

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[2] > 1f) { // anticlockwise
                    getRandomBook();
                } else if (sensorEvent.values[2] < -1f) { // clockwise
                    getRandomBook();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sm.registerListener(gyroSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        LinearLayout linear = (LinearLayout) findViewById(R.id.bookLinearLayout);
        final TextView title = (TextView) findViewById(R.id.readOnTitleTextView);
        linear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent_details = new Intent(getApplicationContext(), BookDetailsActivity.class);
                intent_details.putExtra(BOOK_TITLE_EXTRAMSG, title.getText());
                startActivityForResult(intent_details, 2);
                return true;
            }
        });

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

    public void getRandomBook() {
        Random rand = new Random();
        int index = rand.nextInt((MAX_BOOKS - MIN_BOOKS) + 1) + MIN_BOOKS;

        Books curBook = db.getRandomBook(index);

        TextView readOnTitle = (TextView) findViewById(R.id.readOnTitleTextView);
        TextView readOnAuthor = (TextView) findViewById(R.id.readOnAuthorTextView);
        TextView readOnGenre = (TextView) findViewById(R.id.readOnGenreTextView);
        TextView readOnPages = (TextView) findViewById(R.id.readOnPagesTextView);
        RatingBar readOnStars = findViewById(R.id.readOnStarsRatingBar);

        ImageView readOnCover = (ImageView) findViewById(R.id.readOnCover);

        readOnTitle.setText(curBook.getTitle());
        readOnAuthor.setText("by " + curBook.getAuthor());
        readOnGenre.setText("Genre: " + curBook.getGenre());
        readOnPages.setText(curBook.getPages() + " pages");
        readOnStars.setRating((float) curBook.getStars());

        String cover = curBook.getCover();
        Picasso.with(getApplicationContext()).load(cover).into(readOnCover);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
