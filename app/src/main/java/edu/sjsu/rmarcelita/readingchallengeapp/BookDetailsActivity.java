package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.nearby.messages.internal.Update;
import com.squareup.picasso.Picasso;

public class BookDetailsActivity extends AppCompatActivity {

    private SQLiteHelper db;
    private Books currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        db = new SQLiteHelper(this);
        Intent intent = getIntent();

        String intent1 = intent.getStringExtra(ReadOnActivity.BOOK_TITLE_EXTRAMSG);
        String intent2 = intent.getStringExtra(YourChallengeActivity.BOOK_EXTRA_MSG);
        String intent3 = intent.getStringExtra(UpdateCurrentBookActivity.CURBOOK_EXTRA_MSG);

        if(intent1 != null || intent2 != null || intent3 != null) {
            if(intent1 != null) {
                readBookDetails(intent1, "booksInfo");
            } else if(intent2 != null){
                readBookDetails(intent2, "readBooks");
            } else {
                readBookDetails(intent3, "currentBooks");
            }
        }

        setHomeToolbar();
    }

    public void setHomeToolbar() {
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

    public void readBookDetails(String bookTitle, String tableName) {
        currentBook = db.getBookByTitle(tableName, bookTitle);

        if(currentBook == null && tableName.equals("readBooks")) {
            tableName = "booksInfo";
            currentBook = db.getBookByTitle(tableName, bookTitle);
        } else if(currentBook == null && tableName.equals("booksInfo")) {
            tableName = "readBooks";
            currentBook = db.getBookByTitle(tableName, bookTitle);
        }

        ImageView cover = (ImageView) findViewById(R.id.detailsCoverImageView);
        TextView title = (TextView) findViewById(R.id.detailsTitleTextView);
        TextView author = (TextView) findViewById(R.id.detailsAuthorTextView);
        TextView bookDetails = (TextView) findViewById(R.id.bookDetailsTextView);
        TextView synopsis = (TextView) findViewById(R.id.bookSynopsisTextView);

        StringBuffer synopsisSpaced = new StringBuffer();
        String currentSynopsis = currentBook.getSynopsis();

        title.setText(bookTitle);
        author.setText("by " + currentBook.getAuthor());
        bookDetails.setText("Genre: " + currentBook.getGenre() + ", " + currentBook.getPages()
                + " pages");
        RatingBar stars = findViewById(R.id.bookRatingBar);
        stars.setRating((float) currentBook.getStars());
        synopsis.setText(currentSynopsis);
        Picasso.with(getApplicationContext()).load(currentBook.getCover()).into(cover);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
