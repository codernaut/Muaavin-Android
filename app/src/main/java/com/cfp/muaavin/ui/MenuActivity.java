package com.cfp.muaavin.ui;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cfp.muaavin.facebook.Friend;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.web.FriendManagement;
import com.cfp.muaavin.web.InfringingUser;

import java.util.ArrayList;


public class MenuActivity extends ActionBarActivity {

    FriendManagement  friend_management;
    ArrayList<Post> User_Posts;
    ArrayList<Friend> Friend_List;
    InfringingUser infringing_user;
    Context contex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
         contex = this;

        friend_management = new FriendManagement();

         User_Posts = (ArrayList<Post>) getIntent().getSerializableExtra("User_Posts");
         Friend_List = (ArrayList<Friend>) getIntent().getSerializableExtra("User_friends");






    }

    public void Reporting(View v)
    {


        friend_management.reportFriends(Friend_List,contex);


    }

    public void Highlights(View v)
    {



    }




}
