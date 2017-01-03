package com.cfp.muaavin.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.FacebookUtil;

import com.cfp.muaavin.facebook.FriendsAsynchronousLoad;
import com.cfp.muaavin.facebook.LoadPostsAyscncTask;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.UserInterface;
import com.cfp.muaavin.helper.ClipBoardHelper;
import com.cfp.muaavin.web.DialogBox;
import com.cfp.muaavin.web.User;
import com.cfp.muaavin.web.FriendManagement;
import com.cfp.muaavin.web.WebHttpGetReq;

import java.util.ArrayList;



public  class MenuActivity extends ActionBarActivity implements  AsyncResponsePosts , UserInterface {

    FriendManagement  friend_management;
    ArrayList<Post> User_Posts = new ArrayList<Post>();
    ArrayList<Post> ClipBoard_Posts;
    Context contex;
    String user_id ;
    public static int check = 0;
    ArrayList<User> users_comments;
    public static ArrayList<User> users = new ArrayList<User>() ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        contex = this;

        friend_management = new FriendManagement();
        user_id = getIntent().getStringExtra("User_signedID");

        String serverURL = "http://169.254.68.212:8080/Muaavin-Web/rest/Users/getBlockedUsers?";
        //new WebHttpGetReq(contex,MenuActivity.this, 9,null, this).execute(serverURL);
        new FriendsAsynchronousLoad(contex).execute();


    }

    public void Reporting(View v)
    {
        if(User.user_authentication == false) { return; }
        friend_management.reportFriends(users_comments, contex, User_Posts, user_id, users_comments);
    }

    public void reportTwitter(View view)
    {
        Intent intent = new Intent(MenuActivity.this,TwitterLoginActivity.class);
        startActivity(intent);
    }

    public void Highlights(View v)
    {
        if(User.user_authentication == false) { return; }
        friend_management.Highlights(contex);
    }

    public void Browse(View v)
    {
        if(User.user_authentication == false) { return; }
        friend_management.Browse(contex);
    }

    public void BrowsePost(View v)
    {
        if(User.user_authentication == false) { return; }
        friend_management.BrowsePost(contex, user_id);
    }





    @Override
    public void getUserAndPostData(ArrayList<Post> result) {

         if(LoadPostsAyscncTask.getPostResponse() == true)
         {
            ClipBoard_Posts = result; // Get Asynchronous Posts Result
            users = LoadPostsAyscncTask.users; // Get users from Clipboard post
            Intent intent = new Intent(contex, Post_ListView.class);
                intent.putExtra("user_posts", ClipBoard_Posts);  //User_id
                intent.putExtra("User_id", user_id);
                intent.putExtra("ClipBoardOption", true);
                contex.startActivity(intent);
         }

            else { DialogBox.showErrorDialog(contex); }

    }


    @Override
    public void getReportedFriends(ArrayList<String> Friends) {

    }

    @Override
    public void getBlockedUsers(ArrayList<String> BlockedUserIds) {

        FacebookUtil.BlockedUsersIds = BlockedUserIds;
        if(BlockedUserIds.contains(user_id)) { User.user_authentication = false;  } else { User.user_authentication = true; }
        ClipBoardHelper.getPostFromClipBoard(contex , this, user_id );

    }
}
