package com.cfp.muaavin.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;


import com.cfp.muaavin.adapter.Tweets_CustomAdapter;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.loaders.TweetsLoadAsyncTask;
import com.cfp.muaavin.twitter.TweetsAsynchronousResponse;
import com.cfp.muaavin.twitter.TwitterUtil;

import java.util.ArrayList;

import static com.cfp.muaavin.ui.MenuActivity.LogOut;

public class Tweet_ListView extends Fragment implements TweetsAsynchronousResponse {

    ListView TweetListView;
    Context context;
    String option;
    Button LoadTweets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        //super.onCreate(savedInstanceState);
        ///////////////////
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //////////////////
        View view  = inflater.inflate(R.layout.tweets_layout, container, false);
        context = getActivity();
        //setContentView(R.layout.tweets_layout);
        TweetListView = (ListView)view.findViewById(R.id.TweetList);//LoadTweets
        LoadTweets = (Button)view.findViewById(R.id.LoadTweets);
        option = "LoadTweets";

        Tweets_CustomAdapter customAdapter = new Tweets_CustomAdapter(context,TwitterUtil.Tweets);
        TweetListView.setAdapter(customAdapter);

        LoadTweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TweetsLoadAsyncTask(context, Tweet_ListView.this,option).execute();
            }
        });
        return view;

    }

    public void loadTweets(View view)
    {
        new TweetsLoadAsyncTask(context, Tweet_ListView.this,option).execute();
    }


    @Override
    public void tweetsAsynchronousResponse(ArrayList<Post> tweet, String option)
    {
        ((BaseAdapter) TweetListView.getAdapter()).notifyDataSetChanged();
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_quote:
                // TODO put your code here to respond to the button tap
                LogOut();
                Intent intent = new Intent(Tweet_ListView.this,FacebookLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            default:  return super.onOptionsItemSelected(item);
        }
    }
    */
}
