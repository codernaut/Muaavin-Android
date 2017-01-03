package com.cfp.muaavin.ui;

import android.content.Intent;
import android.provider.Settings;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cfp.muaavin.facebook.Comment;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.twitter.CustomService;
import com.cfp.muaavin.twitter.Followers;
import com.cfp.muaavin.twitter.MyTwitterApiClient;
import com.cfp.muaavin.web.User;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import io.fabric.sdk.android.Fabric;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.*;

import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;
import com.twitter.sdk.android.tweetui.UserTimeline;
import com.twitter.sdk.android.*;
import com.twitter.sdk.android.core.models.*;
import com.twitter.sdk.android.tweetui.TweetUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public class TwitterLoginActivity extends ActionBarActivity {

    private TwitterLoginButton loginButton;
    HashMap<String,Post> dict = new HashMap<String, Post>() ;
    public ArrayList<User> TwitterUsers = new ArrayList<User>();
    ArrayList<User> users = new ArrayList<User>();
    TwitterSession session;



    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "qWbMCnZUcB9hOliWDG6IOtkNP";
    private static final String TWITTER_SECRET = "H4KIPod4y561OXJ7u8Cd4EuGCtIofAi0HhR2hW80Ng84JgQaQ3";
    private static final String Access_Token = "814034327302471680-Z3kdtaZsPDw29yNb5sPOa0zJ7vcMRqT";
    private static final String Access_Token_Secret = "e1yNyVmTIpOQQHLykdWEMuBTN7IvoOcToWoF377b0rAMx";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.twitter_login_screen);



        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                session = result.data;
                //session = Twitter.getSessionManager().getActiveSession();


                session = Twitter.getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;


                getFollowers();
                //loadTweets1(session.getUserId());


                ///////////////
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
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

    public void loadTweets(long userSessionId) {
        //final List<Long> tweetIds = Arrays.asList(userSessionId);
        final List<Long> tweetIds = Arrays.asList(userSessionId);
    }

    public void loadTweets1(long user_id)
    {

        ArrayList<Post> posts = new ArrayList<Post>();
         UserTimeline userTimeline = new UserTimeline.Builder().userId(user_id).includeReplies(true).build();

        userTimeline.next(null, new Callback<TimelineResult<Tweet>>() {
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {

                Toast.makeText(getApplicationContext(), "Tweets Loaded " +
                        "Successfully", Toast.LENGTH_LONG).show();
                for(int i = 0 ; i < result.data.items.size(); i++)
                {
                    User user = new User();

                    Post post = new Post();
                    post.id = result.data.items.get(i).idStr;
                    post.message = result.data.items.get(i).text;
                    if(!dict.containsKey(post.id)) { dict.put(post.id,post); }
                    else
                    {
                       post = dict.get(post.id);
                       post.id = result.data.items.get(i).idStr;
                       post.message =  result.data.items.get(i).text;
                       dict.put(post.id,post);
                    }

                    if(result.data.items.get(i).inReplyToStatusIdStr!=null)
                    {
                        post = new Post();
                        post.id = result.data.items.get(i).inReplyToStatusIdStr;
                        if(!dict.containsKey(post.id)) {dict.put(post.id,post);  }
                        post = dict.get(post.id);
                        Comment comment = new Comment();
                        comment.setComment(result.data.items.get(i).idStr,"",result.data.items.get(i).user.name,post.id,result.data.items.get(i).user.idStr,result.data.items.get(i).text,0);
                        post.Comments.add(comment);
                        dict.put(post.id, post);
                    }
                    user.setUserInformation(result.data.items.get(i).user.idStr,result.data.items.get(i).user.name,result.data.items.get(i).user.profileImageUrl,"","UnBlocked");
                    TwitterUsers.add(user);
                }
                Toast.makeText(getApplicationContext(), "Tweets Loaded " +
                        "Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });

    }

    public void loadTweets2()
    {
        final List<Long> tweetIds = Arrays.asList(510908133917487104L);
        TweetUtils.loadTweets(tweetIds, new Callback<List<Tweet>>() {

            @Override
            public void success(Result<List<Tweet>> result)
            {

                for (Tweet tweet : result.data)
                {
                    //myLayout.addView(new TweetView(EmbeddedTweetsActivity.this, tweet));
                }

            }

            @Override
            public void failure(TwitterException exception)
            {
                // Toast.makeText(...).show();
            }
        });
    }

    public void getFollowers()
    {

        ///////////
        MyTwitterApiClient customApiClient;
       /* if (session != null) {
            customApiClient = new TwitterApiClient(session, customClient);
            TwitterCore.getInstance().addApiClient(session, customApiClient);
        } else {
            customApiClient = new TwitterApiClient(customClient);
            TwitterCore.getInstance().addGuestApiClient(customApiClient);
        }*/
        customApiClient = new MyTwitterApiClient(session);
        TwitterCore.getInstance().addApiClient(session, customApiClient);
        CustomService customService = customApiClient.getCustomService();
        ///////////////
        //CustomService customService= new MyTwitterApiClient(session).getCustomService();
        Call<ResponseBody> users = customService.show(session.getUserId());
        users.enqueue(new Callback<ResponseBody>() {
            @Override
            public void success(Result<ResponseBody> result) {
                String message = result.response.message();
               /* for(int i = 0; i < result.data.users.size(); i++)
                {
                    User user = new User();
                    user.id =  result.data.users.get(i).idStr;
                    user.name = result.data.users.get(i).name;
                    user.profile_pic = result.data.users.get(i).profileImageUrl;
                    user.state = "unBlocked";

                }*/
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });

    }



}






