package com.cfp.muaavin.ui;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.EndlessScrollListener;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.Friend;
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
        FacebookUtil.users = new ArrayList<User>();
        FacebookUtil.friendsIds = new ArrayList<String>();
        LoadPostsAyscncTask.count = 0; // if Paging Completed then set count = 0;
        new LoadPostsAyscncTask(context, Users_ListView.this, User_SignedIn_id, isClipboardData,"",User_posts, new ArrayList<User>()).execute(User_posts);/*.execute(User_Posts)*/;


    }


    @Override
    public void getUserAndPostData(ArrayList<Post> result) {

        User_posts = result;
        Unique_users = FacebookUtil.users;
        ListView lv=(ListView) findViewById(R.id.listView2);
        Users_CustomAdapter c = new Users_CustomAdapter( Users_ListView.this, /*User_posts*/FacebookUtil.Posts, User_SignedIn_id,/*Unique_users*/FacebookUtil.users);
        lv.setAdapter(c);
        lv.setOnScrollListener(new EndlessScrollListener(Users_ListView.this,User_posts/*FacebookUtil.Posts*/,/*Unique_users*/FacebookUtil.users,lv));
    }
}
