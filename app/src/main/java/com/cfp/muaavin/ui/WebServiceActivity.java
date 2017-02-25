package com.cfp.muaavin.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.LoadPostsAyscncTask;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.UserInterface;
import com.cfp.muaavin.facebook.Higlights_CustomAdapter;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.twitter.TwitterUtil;
import com.cfp.muaavin.web.FriendManagement;
import com.cfp.muaavin.web.User;
import com.cfp.muaavin.web.WebHttpGetReq;
import com.twitter.sdk.android.Twitter;

import java.util.ArrayList;


public class WebServiceActivity extends ActionBarActivity implements UserInterface , AsyncResponsePosts {

    public Context context;
    TextView uiUpdate;
    ArrayList<User> infringing_friends;
    ListView lv;
    int check ;
    String Group_name;
    String serverURL = null;
    ArrayList<String> InfringingUserIds;
    Button loadUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);
        context =     this;
        initializeUiElements();
        Intent mIntent = getIntent();
        check = mIntent.getIntExtra("check", 0);
        Group_name = mIntent.getStringExtra("Group_name");
        loadUsers = (Button)findViewById(R.id.LoadButton);

        if (check == 0)
        {
            getInfringingUserAndPostData();
            loadUsers.setVisibility(View.GONE);
        }
        else if(check == 1)
        {
            try
            {
              User user = User.getLoggedInUserInformation();
              serverURL = "http://13.76.175.64:8080/Muaavin-Web/rest/Users/Highlights?name=" + AesEncryption.encrypt(Group_name)+"&user_id="+AesEncryption.encrypt(user.id)+"&specificUserFriends="+true;
            } catch (Exception e) { e.printStackTrace(); }
            new WebHttpGetReq(context,WebServiceActivity.this,  check, null, this).execute(serverURL);
        }

        else if(check == 5) // Twitter Data
        {
            try
            {
              loadUsers.setVisibility(View.GONE);
              User TwitterUser =  User.getTwitterUserLoggedInInformation();
              serverURL = "http://13.76.175.64:8080/Muaavin-Web/rest/TweetQuery/AddTweet?User_ID="+AesEncryption.encrypt(TwitterUser.id)+"&User_Name="+AesEncryption.encrypt(TwitterUser.name)+"&User_ImageUrl="+AesEncryption.encrypt("hoiu")+"&Tweet_ID="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.post_id)+"&Message="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.post_Detail)+"&ImageUrl="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.post_image)+"&Group_Name="+AesEncryption.encrypt(Group_name)+"&InfringingUserID="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.infringing_user_id)+"&InfringingUserName="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.infringing_user_name)+"&InfringingUserProfilePic="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.infringing_user_profile_pic);
            } catch (Exception e) { e.printStackTrace(); }
            new WebHttpGetReq(context,WebServiceActivity.this,check, null, this).execute(serverURL);
        }

        else if(check == 7) // Twitter Data
        {
            try
            {
              User TwitterUser =  User.getTwitterUserLoggedInInformation();
              serverURL = "http://13.76.175.64:8080/Muaavin-Web/rest/Users/Highlights?name=" + AesEncryption.encrypt(Group_name)+"&user_id="+AesEncryption.encrypt(TwitterUser.id)+"&specificUserFriends="+true+"&isTwitterData="+true;
            } catch (Exception e) { e.printStackTrace(); }
            new WebHttpGetReq(context,WebServiceActivity.this,check, null, this).execute(serverURL);
        }
    }

    public  void LoadUsers(View view)
    {
        // If Facebook Post
        new LoadPostsAyscncTask("HighlightUsers",context, WebServiceActivity.this, "", false, "", new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
    }

    @Override
    public void getUserAndPostData(ArrayList<Post> results, String option) {
        FacebookLoginActivity.friend_list = FacebookUtil.users;
        check = 1;
        getReportedFriends(InfringingUserIds);

    }
    @Override
    public void getReportedFriends(ArrayList<String> Common_FriendsIds) {

        InfringingUserIds = Common_FriendsIds;
        infringing_friends = new ArrayList<User>();
        ArrayList<User> friends = FacebookLoginActivity.friend_list;
        if(check == 7) { friends = TwitterUtil.Followers ;}

        for (int j = 0; j < friends.size(); j++)
        {
            String friend_id = friends.get(j).id;
            if (Common_FriendsIds.contains(friend_id)) {  infringing_friends.add(friends.get(j)); }
        }

        if((check == 1) ||(check == 7))
        {
            if(infringing_friends.size() == 0) { uiUpdate.setText("No record found"); }

            else
            {
                uiUpdate.setText("Group :"+Group_name);
                Higlights_CustomAdapter c = new Higlights_CustomAdapter(WebServiceActivity.this, infringing_friends);
                lv.setAdapter(c);
            }
        }
    }

    @Override
    public void getBlockedUsers(ArrayList<String> FacebookUserIds , ArrayList<String> TwitterUserIds) {

    }

    public void getInfringingUserAndPostData()
    {
        try
        {
            loadUsers.setVisibility(View.GONE);
            User user = User.getLoggedInUserInformation();
            String serverURL = "http://13.76.175.64:8080/Muaavin-Web/rest/posts/Insert_Post?user_name="+AesEncryption.encrypt(user.name)+"&UserState="+AesEncryption.encrypt(user.state)+"&ReportedUserState="+AesEncryption.encrypt(FacebookUtil.ReportPostDetail.user_state)+"&Post_id=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.post_id) + "&Group_id="+ AesEncryption.encrypt(String.valueOf(FacebookUtil.ReportPostDetail.group_id))+"&Comment_id="+AesEncryption.encrypt(FacebookUtil.ReportPostDetail.coment_id) + "&PComment_id="+AesEncryption.encrypt(FacebookUtil.ReportPostDetail.ParentComment_ID) + "&Group_name=" + AesEncryption.encrypt(Group_name) + "&Profile_name=" + AesEncryption.encrypt(user.id) + "&user_id=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.infringing_user_id) +  "&infringing_user_name="+ AesEncryption.encrypt(FacebookUtil.ReportPostDetail.infringing_user_name)+"&Post_image=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.post_image)+"&userProfilePic="+AesEncryption.encrypt(user.profile_pic)+"&infringingUser_ProfilePic=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.infringing_user_profile_pic)+"&Comment="+AesEncryption.encrypt(FacebookUtil.ReportPostDetail.comment) +"&Post_Det=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.post_Detail) ;
            new WebHttpGetReq(context,WebServiceActivity.this,  check,null, this).execute(serverURL);
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public void initializeUiElements()
    {
        uiUpdate =   (TextView) findViewById(R.id.output);
        lv =         (ListView) findViewById(R.id.listView1);
    }

    public User  getInfringingFriend(int index)
    {
        User infringingFriend = new User();
        infringingFriend.name = FacebookLoginActivity.friend_list.get(index).name;
        infringingFriend.profile_pic = FacebookLoginActivity.friend_list.get(index).profile_pic;
        infringingFriend.profile_url = FacebookLoginActivity.friend_list.get(index).profile_url;
        return infringingFriend;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(check == 7)
        {
            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


}

