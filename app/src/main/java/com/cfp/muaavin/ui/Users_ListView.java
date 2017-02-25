package com.cfp.muaavin.ui;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.EndlessScrollListener;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.LoadPostsAyscncTask;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.Users_CustomAdapter;
import com.cfp.muaavin.twitter.TweetsAsynchronousLoad;
import com.cfp.muaavin.twitter.TweetsAsynchronousResponse;
import com.cfp.muaavin.twitter.TwitterUtil;
import com.cfp.muaavin.web.User;
import com.twitter.sdk.android.Twitter;

import java.util.ArrayList;

import static com.cfp.muaavin.facebook.FacebookUtil.clearFacebookData;


public class Users_ListView extends ActionBarActivity implements AsyncResponsePosts{

    ListView UserListView ;
    Context context;
    boolean isClipboardData, isTwitterData;
    Button LoadButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users);
        context = this;
        UserListView = (ListView) findViewById(R.id.listView2);
        LoadButton = (Button)  findViewById(R.id.LoadButton);
        isTwitterData = getIntent().getBooleanExtra("isTwitterData",false);

        Users_CustomAdapter c = new Users_CustomAdapter( Users_ListView.this, FacebookUtil.Posts,FacebookUtil.users,isTwitterData);
        UserListView.setAdapter(c);

    }


    @Override // Get Facebook Posts and Users
    public void getUserAndPostData(ArrayList<Post> result,String option) {

        //Users_CustomAdapter c = new Users_CustomAdapter( Users_ListView.this, FacebookUtil.Posts,Unique_users,isTwitterData);
        //UserListView.setAdapter(c);
        //lv.setOnScrollListener(new EndlessScrollListener(Users_ListView.this, User_posts, Unique_users, lv));
        ((BaseAdapter) UserListView.getAdapter()).notifyDataSetChanged();
    }

    public void LoadUsers(View view)
    {
        isClipboardData= false; FacebookUtil.isUserPresent = false; // is Any user found in currently retrievd posts
        if (LoadPostsAyscncTask.nextResultsRequests != null)
        new LoadPostsAyscncTask("ReportUsers",context, Users_ListView.this/*,UserListView*/, User.getLoggedInUserInformation().id, isClipboardData, "", new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
    }

}
