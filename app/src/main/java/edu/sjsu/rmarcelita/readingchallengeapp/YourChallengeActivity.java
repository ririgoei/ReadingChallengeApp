package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class YourChallengeActivity extends AppCompatActivity {

    SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_challenge);

        db = new SQLiteHelper(this);

        db.createUsersChallengeTable();
        db.createCurrentBooksTable();
        db.createChallengeTable();

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
        ImageView bookOne = (ImageView) findViewById(R.id.bookOneImageView);
        ImageView bookTwo = (ImageView)findViewById(R.id.bookTwoImageView);
        ImageView bookThree = (ImageView) findViewById(R.id.bookThreeImageView);
        ImageView bookFour = (ImageView) findViewById(R.id.bookFourImageView);
        ImageView bookFive = (ImageView) findViewById(R.id.bookFiveImageView);
        ImageView bookSix = (ImageView) findViewById(R.id.bookSixImageView);
        ImageView bookSeven = (ImageView) findViewById(R.id.bookSevenImageView);
        ImageView bookEight = (ImageView) findViewById(R.id.bookEightImageView);
        ImageView bookNine = (ImageView) findViewById(R.id.bookNineImageView);

        String existingImage = "https://images-na.ssl-images-amazon.com/images/I/71jn4zl8BUL.jpg";
        Uri myUri = Uri.parse(existingImage);

        Picasso.with(getApplicationContext()).setLoggingEnabled(true);

        Picasso.with(this)
                .load(existingImage)
                .memoryPolicy(MemoryPolicy.NO_CACHE )
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .noFade()
                .resize(150,200).into(currentBook);

        Picasso.with(getApplicationContext()).load(existingImage).resize(150,200).into(bookOne);
        Picasso.with(getApplicationContext()).load(existingImage).resize(150,200).into(bookTwo);
        Picasso.with(getApplicationContext()).load(existingImage).resize(150,200).into(bookThree);
        Picasso.with(getApplicationContext()).load(existingImage).resize(150,200).into(bookFour);
        Picasso.with(getApplicationContext()).load(existingImage).resize(150,200).into(bookFive);
        Picasso.with(getApplicationContext()).load(existingImage).resize(150,200).into(bookSix);
        Picasso.with(getApplicationContext()).load(existingImage).resize(150,200).into(bookSeven);
        Picasso.with(getApplicationContext()).load(existingImage).resize(150,200).into(bookEight);
        Picasso.with(getApplicationContext()).load(existingImage).resize(150,200).into(bookNine);


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
