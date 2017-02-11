package com.cfp.muaavin.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.twitter.Controller;
import com.cfp.muaavin.twitter.TweetsAsynchronousLoad;
import com.cfp.muaavin.twitter.TweetsAsynchronousResponse;
import com.cfp.muaavin.twitter.TwitterUtil;
import com.cfp.muaavin.web.DialogBox;
import com.cfp.muaavin.web.User;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import com.twitter.sdk.android.core.*;
import java.util.ArrayList;
import okhttp3.logging.HttpLoggingInterceptor;

public class TwitterLoginActivity extends ActionBarActivity implements TweetsAsynchronousResponse {

    private TwitterLoginButton loginButton;
    public static TwitterSession session = null;
    Context context ;
    String option;
    public String[] group = {"A","B","C","All"};
    public Controller controller;


    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "qWbMCnZUcB9hOliWDG6IOtkNP";
    private static final String TWITTER_SECRET = "H4KIPod4y561OXJ7u8Cd4EuGCtIofAi0HhR2hW80Ng84JgQaQ3";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.twitter_login_screen);
        context = this;
        option = getIntent().getStringExtra("option");
        controller = new Controller(context);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                //option = "LoadFollowers";
                //new TweetsAsynchronousLoad(context, TwitterLoginActivity.this,option).execute();


                session = Twitter.getSessionManager().getActiveSession();
                //Controller controller = new Controller(context,option);

                final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
                final OkHttpClient customClient = new OkHttpClient.Builder() .addInterceptor(loggingInterceptor).build();


                // pass custom OkHttpClient into TwitterApiClient and add to TwitterCore
                final TwitterApiClient customApiClient;
                if (session != null) {
                    customApiClient = new TwitterApiClient(session, customClient);
                    TwitterCore.getInstance().addApiClient(session, customApiClient);
                } else {
                    customApiClient = new TwitterApiClient(customClient);
                    TwitterCore.getInstance().addGuestApiClient(customApiClient);
                }

                controller.loadTwitterData("LoadUser");
                controller.loadTwitterData(option);

                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();



            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void tweetsAsynchronousResponse(ArrayList<Post> tweets, String option)
    {
        Toast.makeText(getApplicationContext(), "Tweets Successfully loaded", Toast.LENGTH_LONG).show();
        if(option.equals("LoadTweets"))
        {
            Intent intent = new Intent(this, Tweet_ListView.class);
            startActivity(intent);
        }
        else if(option.equals("LoadFollowers"))
        {
            DialogBox.ShowDialogBOx3(context, "Select Group ", group, 7, String.valueOf(session.getUserId()),true);
        }

    }

    public void InsideLoginButtonCode()
    {


    }

}






