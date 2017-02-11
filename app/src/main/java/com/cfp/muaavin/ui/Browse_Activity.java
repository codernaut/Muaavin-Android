package com.cfp.muaavin.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.AsyncResponsePostsDet;
import com.cfp.muaavin.facebook.Browser_CustomAdapter;
import com.cfp.muaavin.facebook.PostDetail;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.web.WebHttpGetReq;

import java.util.ArrayList;
import java.util.HashMap;


public class Browse_Activity extends ActionBarActivity implements AsyncResponsePostsDet {

    public Context context;
    ListView list_view;
    String Group_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_layout);
        context = this;
        list_view = (ListView)findViewById(R.id.listView1);
        Intent intent =  getIntent();
        Group_name = intent.getStringExtra("Group_name");

        String serverURL = null;
        try {
            serverURL = "http://192.168.1.13:8080/Muaavin-Web/rest/Posts_Query/GetPosts?name="+ AesEncryption.encrypt(Group_name);
        } catch (Exception e) { e.printStackTrace(); }


        new WebHttpGetReq(context, Browse_Activity.this, 2,this).execute(serverURL);


    }


    @Override
    public void getPostsDetails(HashMap<String, ArrayList<PostDetail>> result) {

        Browser_CustomAdapter c = new Browser_CustomAdapter(Browse_Activity.this, result);
        list_view.setAdapter(c);
    }
}
