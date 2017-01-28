package com.cfp.muaavin.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.LoadPostsAyscncTask;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.UserInterface;
import com.cfp.muaavin.helper.ClipBoardHelper;
import com.cfp.muaavin.twitter.Controller;
import com.cfp.muaavin.twitter.TwitterUtil;
import com.cfp.muaavin.web.DialogBox;
import com.cfp.muaavin.web.User;
import com.cfp.muaavin.web.FriendManagement;
import com.cfp.muaavin.web.WebHttpGetReq;
import java.util.ArrayList;
import static com.cfp.muaavin.facebook.FacebookUtil.clearFacebookData;
import static com.cfp.muaavin.twitter.TwitterUtil.clearTwitterData;
import static com.cfp.muaavin.ui.TwitterLoginActivity.session;


public  class MenuActivity extends ActionBarActivity implements  AsyncResponsePosts , UserInterface {

    FriendManagement  friend_management;
    ArrayList<Post> User_Posts = new ArrayList<Post>();
    ArrayList<Post> ClipBoard_Posts;
    Context contex;
    String user_id ;
    public static int check = 0;
    ArrayList<User> users_comments;
    public static ArrayList<User> users = new ArrayList<User>() ;
    public String[] group = {"A","B","C","All"};
    Controller controller;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        contex = this;
        clearFacebookData();
        clearTwitterData();
        controller = new Controller(contex);

        friend_management = new FriendManagement();
        user_id = getIntent().getStringExtra("User_signedID");
        //192.168.1.5  13.76.175.64
        String serverURL = null;
        serverURL = "http://192.168.1.9:8080/Muaavin-Web/rest/Users/getBlockedUsers?";


        //new WebHttpGetReq(contex,MenuActivity.this, 9,null, this).execute(serverURL);
        //new FriendsAsynchronousLoad(contex).execute();


    }

    public void Reporting(View v)
    {
        if(User.user_authentication == false) { return; }
        friend_management.reportFriends(users_comments, contex, User_Posts, user_id, users_comments);
    }

    public void reportTwitter(View view)
    {
        if(User.user_authentication == false) { return; }
        if(session == null)
        {
            Intent intent = new Intent(MenuActivity.this, TwitterLoginActivity.class);
            intent.putExtra("option", "LoadTweets"); startActivity(intent);
        }
        else {  controller.loadTwitterData( "LoadTweets"); }

    }

    public void Highlights(View v)
    {
        if(User.user_authentication == false) { return; }
        friend_management.Highlights(contex);
    }

    public void highlightTwitterUsers(View view)
    {
        if(User.user_authentication == false) { return; }
        if(session == null)
        {
            Intent intent = new Intent(MenuActivity.this, TwitterLoginActivity.class);
            intent.putExtra("option", "LoadFollowers"); startActivity(intent);
        }
        else { controller.loadTwitterData( "LoadFollowers"); }

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
            intent.putExtra("isTwitterData", false);
            contex.startActivity(intent);
         }

            else { DialogBox.showErrorDialog(contex); }

    }


    @Override
    public void getReportedFriends(ArrayList<String> Friends) {

    }

    @Override
    public void getBlockedUsers(ArrayList<String> FacebookBlockedUserIds, ArrayList<String> TwitterBlockedUserIds) {

        FacebookUtil.BlockedUsersIds = FacebookBlockedUserIds;
        TwitterUtil.BlockedUserIds = TwitterBlockedUserIds;
        if(FacebookBlockedUserIds.contains(user_id)) { User.user_authentication = false;  } else { User.user_authentication = true; }
        ClipBoardHelper.getPostFromClipBoard(contex , this, user_id );

    }
}
