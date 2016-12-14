package com.cfp.muaavin.ui;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
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
    String User_SignedIn_id;
    boolean ClipBoardOption ;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        User_Posts = (ArrayList<Post>) getIntent().getSerializableExtra("user_posts");
        User_SignedIn_id = getIntent().getStringExtra("User_id");
        ClipBoardOption = getIntent().getBooleanExtra("ClipBoardOption", false);
        context = this;

        lv=(ListView) findViewById(R.id.listView1);
        Posts_CustomAdapter c = new Posts_CustomAdapter( Post_ListView.this, User_Posts, User_SignedIn_id,ClipBoardOption);
        lv.setAdapter(c);
    }

    public void loadPosts(View view)
    {

        if(LoadPostsAyscncTask.nextResultsRequests!=null)
        {

            new LoadPostsAyscncTask(context, Post_ListView.this, User_SignedIn_id, ClipBoardOption,"",User_Posts, new ArrayList<User>()).execute(User_Posts);
        }
    }


    @Override
    public void getUserAndPostData(ArrayList<Post> result)
    {
        User_Posts = result;
        User_Posts = Users_CustomAdapter.getSelectivePosts(FacebookUtil.ReportPostDetail.infringing_user_id, User_Posts);
        Posts_CustomAdapter c = new Posts_CustomAdapter( Post_ListView.this, User_Posts, User_SignedIn_id,ClipBoardOption);
        lv.setAdapter(c);

    }
}
