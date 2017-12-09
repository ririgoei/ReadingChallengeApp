package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {

    private SQLiteHelper db;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        db = new SQLiteHelper(this);

        String name = db.getUserInfo("name");
        username = db.getUserInfo("username");
        String email = db.getUserInfo("email");

        TextView nameText = (TextView) findViewById(R.id.usernameTextView);
        nameText.setText(name);

        TextView usernameText = (TextView) findViewById(R.id.userNameValTextView);
        usernameText.setText(username);

        TextView emailText = (TextView) findViewById(R.id.emailValTextView);
        emailText.setText(email);

        Button logoutBtn = (Button) findViewById(R.id.logoutButton);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        getPages();
        createHomeButton();
    }

    public void getPages() {
        ArrayList<Books> books = db.getAllReadBooks(username.substring(1));
        int totalPages = 0;
        for(int i = 0; i < books.size(); i++) {
            totalPages += books.get(i).getPages();
        }
        TextView totalPagesText = findViewById(R.id.pagesReadValTextView);
        totalPagesText.setText(totalPages + " pages");
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

        public void signOut() {
            GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                    GoogleSignInOptions.DEFAULT_SIGN_IN);
            final Intent intent = new Intent(this, MainActivity.class);
            signInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "You have successfully logged out!",
                                    Toast.LENGTH_LONG).show();
                            db.deleteUser();
                            startActivity(intent);
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
