package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateChallengeActivity extends AppCompatActivity {

    private SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_challenge);
        db = new SQLiteHelper(this);
        createHomeButton();
        getUpdatedChallenges();
    }

    public void getUpdatedChallenges() {
        EditText bookOne = findViewById(R.id.bookOneChallengeEditText);
        EditText bookTwo = findViewById(R.id.bookTwoChallengeEditText);
        EditText bookThree = findViewById(R.id.bookThreeChallengeEditText);

        EditText moneyOne = findViewById(R.id.bookOneMoneyEditText);
        EditText moneyTwo = findViewById(R.id.bookTwoMoneyEditText);
        EditText moneyThree = findViewById(R.id.bookThreeMoneyEditText);

        String bookOneStr = bookOne.getText().toString();
        String bookTwoStr = bookTwo.getText().toString();
        String bookThreeStr = bookThree.getText().toString();

        String moneyOneStr = moneyOne.getText().toString();
        String moneyTwoStr = moneyTwo.getText().toString();
        String moneyThreeStr = moneyThree.getText().toString();

        if(!bookOneStr.equals("") && !bookTwoStr.equals("") && !bookThreeStr.equals("")) {
            int bookOneVal = Integer.parseInt(bookOneStr);
            int bookTwoVal = Integer.parseInt(bookTwoStr);
            int bookThreeVal = Integer.parseInt(bookThreeStr);

            double moneyOneVal = Double.parseDouble(moneyOneStr);
            double moneyTwoVal = Double.parseDouble(moneyTwoStr);
            double moneyThreeVal = Double.parseDouble(moneyThreeStr);

            if(db.checkEmptyDatabase("challenges")) {
                db.insertChallengeTable(bookOneVal, bookTwoVal, bookThreeVal, moneyOneVal,
                        moneyTwoVal, moneyThreeVal);
            } else {
                db.updateChallengeInfo(bookOneVal, bookTwoVal, bookThreeVal, moneyOneVal, moneyTwoVal, moneyThreeVal);
            }
            Toast.makeText(getApplicationContext(), "Challenge is successfully updated!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "One or more fields is empty!", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
