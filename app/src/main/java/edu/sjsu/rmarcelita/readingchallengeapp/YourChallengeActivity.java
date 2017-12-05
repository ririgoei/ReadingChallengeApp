package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class YourChallengeActivity extends AppCompatActivity {

    public static final String BOOK_EXTRA_MSG = "edu.sjsu.rmarcelita.readingchallengeapp.MESSAGE";
    public static final String BOOK_TABLE_MSG = "edu.sjsu.rmarcelita.readingchallengeapp.MESSAGE";
    private SQLiteHelper db;
    private ArrayList<Books> readBooks;
    private InputStream inputStream;
    private InputStream inputDetailStream;
    private BufferedReader br;
    private BufferedReader brDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_challenge);

        db = new SQLiteHelper(this);

        db.createUsersChallengeTable();
        db.createCurrentBooksTable();
        db.createChallengeTable();
        db.createReadBooksTable();

        Button updateBtn = (Button) findViewById(R.id.updateChallengeButton);
        Button updateCurBtn = (Button) findViewById(R.id.updateCurrentButton);

        final Intent intent = new Intent(this, UpdateChallengeActivity.class);
        final Intent intent_cur = new Intent(this, UpdateCurrentBookActivity.class);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        updateCurBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent_cur);
            }
        });

        ImageView currentBook = (ImageView) findViewById(R.id.currentBookImageView);

        String existingImage = "https://images-na.ssl-images-amazon.com/images/I/71jn4zl8BUL.jpg";

        Picasso.with(this)
                .load(existingImage)
                .memoryPolicy(MemoryPolicy.NO_CACHE )
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .noFade()
                .resize(150,200).into(currentBook);

        loadFromCSV();
        loadCurrentBooks();

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

    public void loadFromCSV() {
        inputStream = getResources().openRawResource(R.raw.readbooks);
        inputDetailStream = getResources().openRawResource(R.raw.readbooksynopsis);
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
                db.insertReadBooksTable(row[0], row[1], row[2], "", pages, row[4], stars);
            }
            while ((csvLineDetail = brDetail.readLine()) != null) {
                rowDetails = csvLineDetail.split("_");
                synopsis = rowDetails[1];
                db.updateReadBooksSynopsis(rowDetails[0], synopsis);
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
    }

    public void loadCurrentBooks() {
        GridLayout firstGrid = (GridLayout) findViewById(R.id.readBooksFirstGrid);
        GridLayout secondGrid = (GridLayout) findViewById(R.id.readBooksSecondGrid);
        ImageView readBook;
        String currentCover;
        readBooks = db.getAllReadBooks();
        int sizeInDp = 10;
        int sizeInDpLeft = 30;
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
        int dpAsPixelsLeft = (int) (sizeInDpLeft*scale + 0.5f);

        for(int i = 0; i < readBooks.size(); i++) {
            readBook = new ImageView(this);
            if(i < 0) {
                readBook.setPadding(dpAsPixelsLeft,0,0,0);
            } else {
                readBook.setPadding(dpAsPixels, 0, 0, 0);
            }
            currentCover = readBooks.get(i).getCover();
            Log.v("Cover", "Cover: " + currentCover);
            if(i < 5) {
                firstGrid.addView(readBook);
            } else {
                secondGrid.addView(readBook);
            }
            Picasso.with(getApplicationContext())
                    .load(currentCover)
                    .resize(200,300)
                    .into(readBook);
            final Intent detail_intent = new Intent(this, BookDetailsActivity.class);
            final String curTitle = readBooks.get(i).getTitle();
            readBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detail_intent.putExtra(BOOK_EXTRA_MSG, curTitle);
                    startActivity(detail_intent);
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
