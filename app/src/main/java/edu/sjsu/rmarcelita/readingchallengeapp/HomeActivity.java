package edu.sjsu.rmarcelita.readingchallengeapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        challengeClick();
        readOnClick();
        discoverClick();
        accountClick();
    }

    public void challengeClick() {
        LinearLayout challengeLayout = (LinearLayout) findViewById(R.id.challengeInnerLinearLayout);
        final Intent intent = new Intent(this, YourChallengeActivity.class);
        challengeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    public void readOnClick() {
        LinearLayout readOnLayout = (LinearLayout) findViewById(R.id.readOnInnerLinearLayout);
        final Intent intent = new Intent(this, ReadOnActivity.class);
        readOnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    public void discoverClick() {
        LinearLayout discoverLayout = (LinearLayout) findViewById(R.id.discoverInnearLinearLayout);
        final Intent intent = new Intent(this, DiscoverActivity.class);
        discoverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

    }

    public void accountClick() {
        LinearLayout accountLayout = (LinearLayout) findViewById(R.id.accountInnerLinearLayout);
        final Intent intent = new Intent(this, AccountActivity.class);
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
}
