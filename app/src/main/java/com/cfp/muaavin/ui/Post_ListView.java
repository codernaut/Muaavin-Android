package com.cfp.muaavin.ui;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.LoadPostsAyscncTask;
import com.cfp.muaavin.facebook.Posts_CustomAdapter;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.Users_CustomAdapter;
import com.cfp.muaavin.web.User;

import java.util.ArrayList;


public class Post_ListView extends ActionBarActivity implements AsyncResponsePosts {

    ArrayList<Post> User_Posts;
    ListView lv ;
    boolean ClipBoardOption ;
    Context context;
    boolean IsTwitterData;

    public ArrayList<Post> SelectivePosts = new ArrayList<Post>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        SelectivePosts = (ArrayList<Post>) getIntent().getSerializableExtra("user_posts");
        User_Posts = new ArrayList<Post>();

        ClipBoardOption = getIntent().getBooleanExtra("ClipBoardOption", false);
        IsTwitterData = getIntent().getBooleanExtra("isTwitterData", false);
        context = this;

        lv=(ListView) findViewById(R.id.listView1);
        Posts_CustomAdapter c = new Posts_CustomAdapter( Post_ListView.this, SelectivePosts , User.getLoggedInUserInformation().id,ClipBoardOption);
        lv.setAdapter(c);
    }

    public void loadPosts(View view)
    {
        if(IsTwitterData) {}
        else
        {
            if (LoadPostsAyscncTask.nextResultsRequests != null)
            {
                new LoadPostsAyscncTask(context, Post_ListView.this, User.getLoggedInUserInformation().id, ClipBoardOption, "", new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
            }
        }
    }


    @Override
    public void getUserAndPostData(ArrayList<Post> result)
    {
        SelectivePosts = FacebookUtil.Posts;
        SelectivePosts = Users_CustomAdapter.getSelectivePosts(FacebookUtil.ReportPostDetail.infringing_user_id, SelectivePosts);
        Posts_CustomAdapter c = new Posts_CustomAdapter( Post_ListView.this, SelectivePosts, User.getLoggedInUserInformation().id,ClipBoardOption);
        lv.setAdapter(c);
        //((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();

    }
}
