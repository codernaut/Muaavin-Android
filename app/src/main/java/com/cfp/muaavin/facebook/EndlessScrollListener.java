package com.cfp.muaavin.facebook;

import android.content.Context;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cfp.muaavin.ui.MenuActivity;
import com.cfp.muaavin.web.User;

import java.util.ArrayList;

/**
 *
 */
public class EndlessScrollListener implements AbsListView.OnScrollListener, AsyncResponsePosts {

    ArrayList<Post> User_Posts = new ArrayList<Post>();
    public static ArrayList<User> users = new ArrayList<User>() ;
    public Context context;
    ListView user_listView;
    User user;

    public EndlessScrollListener(Context contex,ArrayList<Post> Posts,ArrayList<User> users,ListView lv)
    {
        context = contex;
        User_Posts =  Posts;
        this.users =  users;
        user_listView =  lv;
    }

    private static final String TAG = "CacheToDBActivity.EndlessScrollListener";
    private boolean loading = false;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!(loading) && (totalItemCount - visibleItemCount) <= (firstVisibleItem)) {
            //Log.d(TAG, "Load Next Page!");
            loading = true;
            user = User.getLoggedInUserInformation();
            boolean isClipboardData = false;
            if (LoadPostsAyscncTask.nextResultsRequests != null)
            {
                 new LoadPostsAyscncTask("ReportUsers",context,EndlessScrollListener.this,  user.id, isClipboardData, "", /*User_Posts*/new ArrayList<Post>(), users).execute(new ArrayList<Post>());
            }

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading)
    {
        this.loading = loading;
    }

    @Override
    public void getUserAndPostData(ArrayList<Post> result, String option) {

        users = LoadPostsAyscncTask.getUsers();
        ////////
        users = FacebookUtil.users;
        ////////
        User_Posts = result;

        Users_CustomAdapter c = new Users_CustomAdapter( context, /*User_Posts*/FacebookUtil.Posts, users, false);
        user_listView.setAdapter(c);
        loading = false;

    }
}


