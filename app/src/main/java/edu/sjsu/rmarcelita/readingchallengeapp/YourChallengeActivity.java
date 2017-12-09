package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class YourChallengeActivity extends AppCompatActivity {

    public static final String BOOK_EXTRA_MSG = "edu.sjsu.rmarcelita.readingchallengeapp.MESSAGE_READON";
    private SQLiteHelper db;
    private ArrayList<Books> readBooks;
    private InputStream inputStream;
    private InputStream inputDetailStream;
    private BufferedReader br;
    private BufferedReader brDetail;
    private int readBooksNum;
    private String curUser;
    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_challenge);
        readBooksNum = 0;

        db = new SQLiteHelper(this);
        db.createUsersChallengeTable();
        db.createCurrentBooksTable();
        db.createChallengeTable();
        db.createReadBooksTable();
        curUser = db.getUserInfo("username").substring(1);

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

        if(db.checkEmptyDatabase("currentBooks")) {
            loadCurrentsFromCSV();
        }
        loadFromCSV();
        loadCurrentBooks();
        loadReadBooks();
        setChallengesTextView();
        createHomeButton();
    }

    public void setChallengesTextView() {

        TextView firstLine = findViewById(R.id.oneDollarDistance);
        TextView secondLine = findViewById(R.id.threeDollarDistance);
        TextView thirdLine = findViewById(R.id.fiveDollarDistance);
        ArrayList<Integer> bookChallenges = new ArrayList<>();
        ArrayList<Double> moneyChallenges = new ArrayList<>();

        if(!db.checkEmptyDatabase("challenges")) {
            bookChallenges = db.getChallengesBooksInfo();
            moneyChallenges = db.getChallengesMoneyInfo();
            firstLine.setText((bookChallenges.get(0) - readBooksNum) + " away from $"
                    + moneyChallenges.get(0));
            secondLine.setText((bookChallenges.get(1) - readBooksNum) + " away from $"
                    + moneyChallenges.get(1));
            thirdLine.setText((bookChallenges.get(2) - readBooksNum) + " away from $"
                    + moneyChallenges.get(2));
        }
    }

    public void loadCurrentBooks() {
        LinearLayout curBooks = findViewById(R.id.curImageViewsLinearLayout);
        ArrayList<Books> currents = db.getAllCurrentBooks(curUser);
        ImageView curBook;
        String curCover = "";
        int sizeInDp = 10;
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
        for(int i = 0; i < currents.size(); i++) {
            curCover = currents.get(i).getCover();
            curBook = new ImageView(this);
            curBook.setPadding(dpAsPixels,0,0,0);
            curBooks.addView(curBook);
            Picasso.with(this)
                    .load(curCover)
                    .resize(150,200).into(curBook);
        }
    }

    public void createHomeButton() {
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

    public void loadCurrentsFromCSV() {
        inputStream = getResources().openRawResource(R.raw.currentlyreading);
        inputDetailStream = getResources().openRawResource(R.raw.currentsynopsis);
        brDetail = new BufferedReader(new InputStreamReader(inputDetailStream));
        br = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine, csvLineDetail;
            String row[] = new String[7];
            String rowDetails[] = new String[2];
            String synopsis = "";
            int pages = 0;
            double stars = 0.00;
            while ((csvLine = br.readLine()) != null) {
                row = csvLine.split(",");
                pages = Integer.parseInt(row[3]);
                stars = Double.parseDouble(row[5]);
                db.insertCurrentBooksTable(row[0], row[1], row[2], "", pages, row[4], stars,
                        row[6]);
            }
            while ((csvLineDetail = brDetail.readLine()) != null) {
                rowDetails = csvLineDetail.split("_");
                synopsis = rowDetails[1];
                db.updateReadBooksSynopsis(rowDetails[0], synopsis, "currentBooks");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
                inputDetailStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
    }

    public void loadFromCSV() {
        inputStream = getResources().openRawResource(R.raw.readbooks);
        inputDetailStream = getResources().openRawResource(R.raw.readbooksynopsis);
        brDetail = new BufferedReader(new InputStreamReader(inputDetailStream));
        br = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine, csvLineDetail;
            String row[] = new String[7];
            String rowDetails[] = new String[2];
            String synopsis = "";
            int pages = 0;
            double stars = 0.00;
            while ((csvLine = br.readLine()) != null) {
                row = csvLine.split(",");
                pages = Integer.parseInt(row[3]);
                stars = Double.parseDouble(row[5]);
                db.insertReadBooksTable(row[0], row[1], row[2], "", pages, row[4], stars,
                        row[6]);
            }
            while ((csvLineDetail = brDetail.readLine()) != null) {
                rowDetails = csvLineDetail.split("_");
                synopsis = rowDetails[1];
                db.updateReadBooksSynopsis(rowDetails[0], synopsis, "readBooks");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
                inputDetailStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
    }

    public void loadReadBooks() {
        GridLayout firstGrid = (GridLayout) findViewById(R.id.readBooksFirstGrid);
        GridLayout secondGrid = (GridLayout) findViewById(R.id.readBooksSecondGrid);
        ImageView readBook;
        String currentCover;
        readBooks = db.getAllReadBooks(curUser);

        TextView numRead = findViewById(R.id.booksReadTextView);
        String size = readBooks.size() + "";
        numRead.setText(size);
        readBooksNum = readBooks.size();

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
                    startActivityForResult(detail_intent, 1);
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
