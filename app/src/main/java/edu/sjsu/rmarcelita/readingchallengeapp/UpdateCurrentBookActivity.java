package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UpdateCurrentBookActivity extends AppCompatActivity {

    private SQLiteHelper db;
    public static final String CURBOOK_EXTRA_MSG = "edu.sjsu.rmarcelita.readingchallengeapp.MESSAGE_CUR";
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_current_book);
        createHomeButton();
        db = new SQLiteHelper(this);
        currentUser = db.getUserInfo("username").substring(1);
        loadBooks();
        Button newBook = findViewById(R.id.newBookButton);
        final Intent newBookIntent = new Intent(this, AddNewBookActivity.class);
        newBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(newBookIntent);
            }
        });
    }

    public void loadBooks() {
        ArrayList<Books> allCurrent = db.getAllCurrentBooks(currentUser);
        RecyclerView rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        CurrentBooksAdapter adapter = new CurrentBooksAdapter(allCurrent, getApplicationContext());
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
