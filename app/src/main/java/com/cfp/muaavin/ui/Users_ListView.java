package com.cfp.muaavin.ui;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.BaseAdapter;
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


public class Users_ListView extends ActionBarActivity implements AsyncResponsePosts{

    ListView UserListView ;
    Context context;
    boolean isClipboardData, isTwitterData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users);
        context = this;
        UserListView = (ListView) findViewById(R.id.listView2);
        isTwitterData = getIntent().getBooleanExtra("isTwitterData",false);

        Users_CustomAdapter c = new Users_CustomAdapter( Users_ListView.this, FacebookUtil.Posts,FacebookUtil.users,isTwitterData);
        UserListView.setAdapter(c);

        /*if(isTwitterData)
        {
            Users_CustomAdapter c = new Users_CustomAdapter( Users_ListView.this, TwitterUtil.TwitterPosts,TwitterUtil.TwitterUsers,isTwitterData);
            UserListView.setAdapter(c);
        }
        else
        {
            isClipboardData = false;
            FacebookUtil.isUserPresent = false; // is Any user found in currently retrievd posts
            if (FacebookUtil.users.size() > 0) { getUserAndPostData(new ArrayList<Post>()); }
            else { new LoadPostsAyscncTask(context, Users_ListView.this, User_SignedIn_id, isClipboardData, "", User_posts, new ArrayList<User>()).execute(User_posts); }
        }*/


    }


    @Override // Get Facebook Posts and Users
    public void getUserAndPostData(ArrayList<Post> result) {

        //User_posts = result;
        //Unique_users = LoadPostsAyscncTask.users;
        //Unique_users = FacebookUtil.users;
        //FacebookUtil.getFriends(Unique_users);
        //Users_CustomAdapter c = new Users_CustomAdapter( Users_ListView.this, FacebookUtil.Posts,Unique_users,isTwitterData);
        //UserListView.setAdapter(c);
        //lv.setOnScrollListener(new EndlessScrollListener(Users_ListView.this, User_posts, Unique_users, lv));
        ((BaseAdapter) UserListView.getAdapter()).notifyDataSetChanged();
    }

    public void LoadUsers(View view)
    {
        isClipboardData= false; FacebookUtil.isUserPresent = false; // is Any user found in currently retrievd posts
        if (LoadPostsAyscncTask.nextResultsRequests != null)
        new LoadPostsAyscncTask(context, Users_ListView.this, User.getLoggedInUserInformation().id, isClipboardData, "", new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
    }


}
