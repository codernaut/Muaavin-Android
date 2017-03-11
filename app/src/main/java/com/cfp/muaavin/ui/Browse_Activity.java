package com.cfp.muaavin.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.cfp.muaavin.facebook.AsyncResponsePostsDet;
import com.cfp.muaavin.adapter.Browser_CustomAdapter;
import com.cfp.muaavin.facebook.PostDetail;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.web.WebHttpGetReq;
import java.util.ArrayList;
import java.util.HashMap;

import static com.cfp.muaavin.ui.MenuActivity.LogOut;


public class Browse_Activity extends Fragment implements AsyncResponsePostsDet,Browser_CustomAdapter.UiUpdate {

    public Context context;
    ListView list_view;
    String Group_name;
    HashMap<String, ArrayList<PostDetail>> PostDetails;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //super.onCreate(savedInstanceState);
        ///////////////////
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //////////////////
        //setContentView(R.layout.browse_layout);
        //context = this;
        View view  = inflater.inflate(R.layout.browse_layout, container, false);
        context = getActivity();
        list_view = (ListView)view.findViewById(R.id.listView1);
        Group_name = getArguments().getString("Group_name");
        //Intent intent =  getIntent();
        //Group_name = intent.getStringExtra("Group_name");

        String serverURL = null;
        try {
            //192.168.1.13 13.76.175.64
          serverURL = "http://13.76.175.64:8080/Muaavin-Web/rest/Posts_Query/GetPosts?name="+ AesEncryption.encrypt(Group_name);
        } catch (Exception e) { e.printStackTrace(); }

        new WebHttpGetReq(context, getActivity(), 2,this).execute(serverURL);
        return  view;
    }


    @Override
    public void getPostsDetails(HashMap<String, ArrayList<PostDetail>> result) {

        PostDetails = result;
        Browser_CustomAdapter c = new Browser_CustomAdapter(context,this, PostDetails);
        list_view.setAdapter(c);
    }

    @Override
    public void updateDislikeButton(int position, String response)
    {
        if(response.equals("Record Deleted"))
        {
            final ArrayList<String> keys = new ArrayList<String>(PostDetails.keySet());
            PostDetails.get(keys.get(position)).get(0).unlike_value--;
        }
        else
        {
            final ArrayList<String> keys = new ArrayList<String>(PostDetails.keySet());
            PostDetails.get(keys.get(position)).get(0).unlike_value++;
        }
        ((BaseAdapter) list_view.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void updateFeedBack(int position,String comment) {
        final ArrayList<String> keys = new ArrayList<String>(PostDetails.keySet());
        PostDetails.get(keys.get(position)).get(0).FeedBacks.add(comment);
        ((BaseAdapter) list_view.getAdapter()).notifyDataSetChanged();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_quote:
                // TODO put your code here to respond to the button tap
                LogOut();
                Intent intent = new Intent(Browse_Activity.this,FacebookLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            default:  return super.onOptionsItemSelected(item);
        }
    }*/
}
