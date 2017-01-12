package com.cfp.muaavin.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;


import com.cfp.muaavin.adapter.Tweets_CustomAdapter;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.twitter.TweetsAsynchronousLoad;
import com.cfp.muaavin.twitter.TweetsAsynchronousResponse;
import com.cfp.muaavin.twitter.TwitterUtil;

import java.util.ArrayList;

public class Tweet_ListView extends Activity implements TweetsAsynchronousResponse {

    ListView TweetListView;
    Context context;
    String option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweets_layout);
        TweetListView = (ListView)findViewById(R.id.TweetList);
        context = this; option = "LoadTweets";

        Tweets_CustomAdapter customAdapter = new Tweets_CustomAdapter(context,TwitterUtil.Tweets);
        TweetListView.setAdapter(customAdapter);

    }

    public void loadTweets(View view)
    {
        new TweetsAsynchronousLoad(context, Tweet_ListView.this,option).execute();
    }


    @Override
    public void tweetsAsynchronousResponse(ArrayList<Post> tweet)
    {
        ((BaseAdapter) TweetListView.getAdapter()).notifyDataSetChanged();
    }
}
