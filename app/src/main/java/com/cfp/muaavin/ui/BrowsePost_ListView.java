package com.cfp.muaavin.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.AsyncResponsePostsDet;
import com.cfp.muaavin.facebook.BrowsePostCustomAdapter;
import com.cfp.muaavin.facebook.Browser_CustomAdapter;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.PostDetail;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.web.User;
import com.cfp.muaavin.web.WebHttpGetReq;

import java.util.ArrayList;
import java.util.HashMap;

import static com.cfp.muaavin.ui.TwitterLoginActivity.session;


public class BrowsePost_ListView extends ActionBarActivity implements AsyncResponsePosts {

    Context context;
    String Group_name;
    String user_id , TwitterUserId;

    ListView browsePost_Listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_post_layout);
        context = this;
        browsePost_Listview = (ListView)findViewById(R.id.BrowsePost_Listview);

        Intent intent =  getIntent();
        Group_name = intent.getStringExtra("Group_name");
        user_id = intent.getStringExtra("user_id");
        String serverURL = null;
        try {
            if(session==null) { TwitterUserId = "";  } else { TwitterUserId = String.valueOf(session.getUserId()); }
            serverURL = "http://192.168.8.101:8080/Muaavin-Web/rest/UsersPosts/GetUsersPosts?name="+ AesEncryption.encrypt(Group_name)+"&user_id="+AesEncryption.encrypt(User.getLoggedInUserInformation().id)+"&isSpecificUserPost="+true+"&TwitterUserID="+AesEncryption.encrypt(TwitterUserId);
        } catch (Exception e) { e.printStackTrace(); }


        new WebHttpGetReq(context,BrowsePost_ListView.this, 4,this,null).execute(serverURL);


    }



    @Override
    public void getUserAndPostData(ArrayList<Post> result) {

        ArrayList<Post> Posts = new ArrayList<Post>();
        BrowsePostCustomAdapter c = new BrowsePostCustomAdapter(BrowsePost_ListView.this, result, Group_name , User.getLoggedInUserInformation().id);
        browsePost_Listview.setAdapter(c);




    }
}
