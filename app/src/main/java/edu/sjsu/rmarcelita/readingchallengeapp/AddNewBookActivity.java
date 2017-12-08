package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.reginald.editspinner.EditSpinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddNewBookActivity extends AppCompatActivity {

    private ArrayList<String> genres;
    private EditSpinner genreES;
    private SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);
        createHomeButton();
        db = new SQLiteHelper(this);
        genres = new ArrayList<>();
        populateGenres();
        genreES = findViewById(R.id.genreEditSpinner);

        ListAdapter arrAdapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                genres);

        genreES.setAdapter(arrAdapt);
        Button updateBtn = findViewById(R.id.addNewBookButton);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewBookInfo();
            }
        });
    }

    public void saveNewBookInfo() {
        EditText bookTitle = findViewById(R.id.newBookTitleEditText);
        EditText bookAuthor = findViewById(R.id.newBookAuthorEditText);
        EditText bookSynopsis = findViewById(R.id.newBookSynopsisEditText);
        EditText cover = findViewById(R.id.newBookCoverEditText);
        EditText bookPages = findViewById(R.id.newBookPagesEditText);
        EditText bookRating = findViewById(R.id.newBookRatingEditText);

        String title = bookTitle.getText().toString();
        String auth = bookTitle.getText().toString();

        if(title.equals("") || auth.equals("")) {
            Toast.makeText(getApplicationContext(), "Not enough information to insert new book!\n" +
                    "please provide both book title and author.", Toast.LENGTH_LONG).show();
        } else {
            String synop = bookSynopsis.getText().toString();
            String cov = cover.getText().toString();
            int pag = Integer.parseInt(bookPages.getText().toString());
            double rating = Double.parseDouble(bookRating.getText().toString());
            db.insertCurrentBooksTable(title, auth, genreES.getText().toString(), synop, pag,
                    cov, rating, db.getUserInfo("username").substring(1));
            Toast.makeText(getApplicationContext(), "New book added successfully to your " +
                "currently reading list!", Toast.LENGTH_SHORT).show();
        }

    }

    public void populateGenres() {
        genres.add("Fiction");
        genres.add("Historical Fiction");
        genres.add("Mystery");
        genres.add("Dystopian Fiction");
        genres.add("Science Fiction");
        genres.add("Fantasy");
        genres.add("Children's Fantasy");
        genres.add("Children's Fiction");
        genres.add("Horror");
        genres.add("Thriller");
        genres.add("Romance");
        genres.add("Memoir");
        genres.add("Anthology");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
