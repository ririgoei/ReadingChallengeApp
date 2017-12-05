package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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
        String bookTitle = intent.getStringExtra(ReadOnActivity.BOOK_TITLE_EXTRAMSG);
        currentBook = db.getBookByTitle(bookTitle);

        ImageView cover = (ImageView) findViewById(R.id.detailsCoverImageView);
        TextView title = (TextView) findViewById(R.id.detailsTitleTextView);
        TextView author = (TextView) findViewById(R.id.detailsAuthorTextView);
        TextView bookDetails = (TextView) findViewById(R.id.bookDetailsTextView);
        TextView synopsis = (TextView) findViewById(R.id.bookSynopsisTextView);

        StringBuffer synopsisSpaced = new StringBuffer();
        String currentSynopsis = currentBook.getSynopsis();

        title.setText(bookTitle);
        author.setText("by " + currentBook.getAuthor());
        bookDetails.setText("Genre: " + currentBook.getGenre() + " \n" + currentBook.getPages()
                + " pages, " + currentBook.getStars() + " stars");
        synopsis.setText(currentSynopsis);
        Picasso.with(getApplicationContext()).load(currentBook.getCover()).into(cover);
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
}
