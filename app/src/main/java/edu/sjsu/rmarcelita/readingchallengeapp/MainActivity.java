package edu.sjsu.rmarcelita.readingchallengeapp;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class MainActivity extends ListActivity {

    private boolean signedIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Twitter.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, HomeActivity.class);
        if(signedIn) {
            startActivity(intent);
        }

//        final UserTimeline userTimeline = new UserTimeline.Builder()
//                .screenName("riri_goei")
//                .build();
//        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
//                .setTimeline(userTimeline)
//                .build();
//        setListAdapter(adapter);
    }
}
