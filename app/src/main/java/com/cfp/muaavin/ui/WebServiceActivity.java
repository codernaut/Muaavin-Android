package com.cfp.muaavin.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.TextView;

import com.cfp.muaavin.facebook.UserInterface;
import com.cfp.muaavin.facebook.Friend;
import com.cfp.muaavin.facebook.Higlights_CustomAdapter;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.web.User;
import com.cfp.muaavin.web.WebHttpGetReq;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class WebServiceActivity extends ActionBarActivity implements UserInterface {

    public Context context;
    TextView jsonParsed;
    TextView uiUpdate;
    ArrayList<Friend> infringing_friends;


    ListView lv;
    int check ;
    TextView text_view;
    public static String response;
    String Group_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);
        context =     this;
        initializeUiElements();
        Intent mIntent = getIntent();
        check = mIntent.getIntExtra("check", 0);
        Group_name = mIntent.getStringExtra("Group_name");

        if (check == 0) {


            getInfringingUserAndPostData();

        } else {

            String serverURL = null;
            try {
                serverURL = "http://169.254.68.212:8080/Muaavin-Web/rest/Query/Highlights?name=" + AesEncryption.encrypt(Group_name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            new WebHttpGetReq(context,WebServiceActivity.this,  check, null, this).execute(serverURL);

        }


    }


    @Override
    public void getReportedFriends(ArrayList<Friend> Common_FriendsIds) {


        ArrayList<String>  friendIDs = new ArrayList<String>();
        infringing_friends = new ArrayList<Friend>();


        for (int i = 0; i < Common_FriendsIds.size(); i++) {


            String friend_id =  Common_FriendsIds.get(i).id;
            for (int j = 0; j < FacebookLoginActivity.friend_list.size(); j++) {



                if (friend_id.equals(FacebookLoginActivity.friend_list.get(j).id)&&(!friendIDs.contains(friend_id))) {

                    friendIDs.add(friend_id);
                    Friend infringingFriend = getInfringingFriend(j);
                    infringing_friends.add(infringingFriend);
                    break;
                }

            }

        }

        if(check == 1)
        {

            if(infringing_friends.size() == 0)
            {
                jsonParsed.setText("No record found");
            }
            else
            {
                uiUpdate.setText("Group :"+Group_name);
                Higlights_CustomAdapter c = new Higlights_CustomAdapter(WebServiceActivity.this, infringing_friends);
                lv.setAdapter(c);
            }
        }

    }

    @Override
    public void getBlockedUsers(ArrayList<String> UserIds) {

    }

    public void getInfringingUserAndPostData()
    {

        try {
            Intent mIntent = getIntent();
            String    Group_id = AesEncryption.encrypt(String.valueOf(mIntent.getIntExtra("Group_id", 0)));
            String infringing_user_id = AesEncryption.encrypt(mIntent.getStringExtra("user_id"));
            String post_id  = AesEncryption.encrypt(mIntent.getStringExtra("post_id"));
            String User_id   = AesEncryption.encrypt(mIntent.getStringExtra("User_Id"));
            String infringing_user_name = AesEncryption.encrypt(mIntent.getStringExtra("Infringing_User_name"));
            String infringing_Post_Detail = AesEncryption.encrypt(mIntent.getStringExtra("Post_detail"));
            String Comment = AesEncryption.encrypt(mIntent.getStringExtra("Comment"));
            String Comment_ID = AesEncryption.encrypt(mIntent.getStringExtra("Comment_ID"));
            String ParentComment_ID = AesEncryption.encrypt(mIntent.getStringExtra("ParentComment_ID"));
            String infringing_user_Profile_pic = AesEncryption.encrypt(mIntent.getStringExtra("Infringing_User_profilepic"));
            String PostImage = AesEncryption.encrypt(mIntent.getStringExtra("post_image"));
            User user = User.getUserInformation();
            user.profile_pic = AesEncryption.encrypt(getEncodedImage(user.profile_pic));
            user.name = AesEncryption.encrypt(user.name);
            user.id = AesEncryption.encrypt(user.id);
            Group_name = AesEncryption.encrypt(Group_name );


            String serverURL = "http://169.254.68.212:8080/Muaavin-Web/rest/posts/Insert_Post?user_name="+user.name+"&Post_id=" + post_id + "&Group_id=" + Group_id+"&Comment_id="+Comment_ID + "&PComment_id="+ParentComment_ID + "&Group_name=" + Group_name + "&Profile_name=" + User_id + "&user_id=" + infringing_user_id +  "&infringing user_name=" + infringing_user_name+"&Post_image=" + PostImage+"&userProfilePic="+user.profile_pic+"&infringingUser_ProfilePic=" + infringing_user_Profile_pic+"&Comment="+Comment +"&Post_Det=" + infringing_Post_Detail ;
            new WebHttpGetReq(context,WebServiceActivity.this,  check,null, this).execute(serverURL);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initializeUiElements()
    {

        uiUpdate =   (TextView) findViewById(R.id.output);
        jsonParsed = (TextView) findViewById(R.id.jsonParsed);
        lv =         (ListView) findViewById(R.id.listView1);
        text_view =  (TextView) findViewById(R.id.textView2);


    }

    public Friend  getInfringingFriend(int index)
    {
        Friend infringingFriend = new Friend();
        infringingFriend.name = FacebookLoginActivity.friend_list.get(index).name;
        infringingFriend.profile_pic = FacebookLoginActivity.friend_list.get(index).profile_pic;
        infringingFriend.profile_url = FacebookLoginActivity.friend_list.get(index).profile_url;
        return infringingFriend;

    }

    public String getEncodedImage(String image)
    {

        try {
            image = URLEncoder.encode(image, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return image;
    }
}

