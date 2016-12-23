package com.cfp.muaavin.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.UserInterface;
import com.cfp.muaavin.facebook.Friend;
import com.cfp.muaavin.facebook.Higlights_CustomAdapter;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.web.User;
import com.cfp.muaavin.web.WebHttpGetReq;
import java.util.ArrayList;


public class WebServiceActivity extends ActionBarActivity implements UserInterface {

    public Context context;
    TextView jsonParsed;
    TextView uiUpdate;
    ArrayList<Friend> infringing_friends;
    User user = User.getUserInformation();


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

        if (check == 0)
        {

            getInfringingUserAndPostData();
        }
        else
        {
            String serverURL = null;
            try
            {
                serverURL = "http://169.254.68.212:8080/Muaavin-Web/rest/Users/Highlights?name=" + AesEncryption.encrypt(Group_name)             +"&user_id="+AesEncryption.encrypt(user.id)+"&specificUserFriends="+true;
            } catch (Exception e) { e.printStackTrace(); }

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
        try
        {

            String serverURL = "http://169.254.68.212:8080/Muaavin-Web/rest/posts/Insert_Post?user_name="+AesEncryption.encrypt(user           .name)+"&UserState="+AesEncryption.encrypt(user.state)+"&ReportedUserState="+AesEncryption.encrypt(FacebookUtil                    .ReportPostDetail.user_state)+"&Post_id=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.post_id) + "&Group_id="            + AesEncryption.encrypt(String.valueOf(FacebookUtil.ReportPostDetail.group_id))+"&Comment_id="+AesEncryption.encrypt               (FacebookUtil.ReportPostDetail.coment_id) + "&PComment_id="+AesEncryption.encrypt(FacebookUtil.ReportPostDetail                   .ParentComment_ID) + "&Group_name=" + AesEncryption.encrypt(Group_name) + "&Profile_name=" + AesEncryption.encrypt                  (user.id) + "&user_id=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.infringing_user_id) +  "&infringing user_name="+ AesEncryption.encrypt(FacebookUtil.ReportPostDetail.infringing_user_name)+"&Post_image=" + AesEncryption              .encrypt(FacebookUtil.ReportPostDetail.post_image)+"&userProfilePic="+AesEncryption.encrypt(user.profile_pic)                       +"&infringingUser_ProfilePic=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.infringing_user_profile_pic)                  +"&Comment="+AesEncryption.encrypt(FacebookUtil.ReportPostDetail.comment) +"&Post_Det=" + AesEncryption.encrypt                     (FacebookUtil.ReportPostDetail.post_Detail) ;
            new WebHttpGetReq(context,WebServiceActivity.this,  check,null, this).execute(serverURL);
        }
        catch (Exception e) { e.printStackTrace(); }
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

}

