package com.cfp.muaavin.ui;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.EndlessScrollListener;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.LoadPostsAyscncTask;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.Users_CustomAdapter;
import com.cfp.muaavin.web.User;

import java.util.ArrayList;


public class Users_ListView extends ActionBarActivity implements AsyncResponsePosts {

    ListView lv ;
    String User_SignedIn_id;
    ArrayList<Post> User_posts = new ArrayList<Post>();
    Context context;
    boolean isClipboardData;

    ArrayList<User> Unique_users = new ArrayList<User>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users);
        context = this;


        User_SignedIn_id = getIntent().getStringExtra("user_id");


        isClipboardData = false;
        FacebookUtil.isUserPresent = false; // is Any user found in currently retrievd posts
        if(FacebookUtil.users.size() > 0) { getUserAndPostData(new ArrayList<Post>()); }
        else new LoadPostsAyscncTask(context, Users_ListView.this, User_SignedIn_id, isClipboardData,"",User_posts, new ArrayList<User>()).execute(User_posts);


    }


    @Override
    public void getUserAndPostData(ArrayList<Post> result) {

        User_posts = result;
        Unique_users = LoadPostsAyscncTask.users;
        ///////////
        Unique_users = FacebookUtil.users;
        ///////////
        //FacebookUtil.getFriends(Unique_users);
        ListView lv=(ListView) findViewById(R.id.listView2);
        Users_CustomAdapter c = new Users_CustomAdapter( Users_ListView.this, FacebookUtil.Posts, User_SignedIn_id,Unique_users);
        lv.setAdapter(c);
        //lv.setOnScrollListener(new EndlessScrollListener(Users_ListView.this, User_posts, Unique_users, lv));
    }

    public void LoadUsers(View view)
    {
        FacebookUtil.isUserPresent = false; // is Any user found in currently retrievd posts
        if (LoadPostsAyscncTask.nextResultsRequests != null)
        new LoadPostsAyscncTask(context, Users_ListView.this, User_SignedIn_id, isClipboardData,"",new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
    }
}
