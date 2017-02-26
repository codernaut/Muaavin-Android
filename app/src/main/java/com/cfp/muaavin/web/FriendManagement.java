package com.cfp.muaavin.web;

import android.content.Context;
import android.content.Intent;

import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.LoadPostsAyscncTask;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.ui.Browse_Activity;
import com.cfp.muaavin.ui.FacebookLoginActivity;
import com.cfp.muaavin.ui.Users_ListView;

import java.util.ArrayList;

import static com.cfp.muaavin.facebook.FacebookUtil.clearFacebookData;


public class FriendManagement implements AsyncResponsePosts {

    //static String[] group1  =  {"A","B","C","All"};
    static String[] group1 = new String[]{"Sexual harassment", "Incitement to violence","Trans rights","All of the above"};
    Context context;



    public void Highlights(Context context)
    {
        int check = 1;
        DialogBox dialogBox = new DialogBox();
        String user_id = "";
        this.context = context;
        //dialogBox.ShowDialogBOx3(context, "Select Group", FriendManagement.group1, check, user_id, false);
        new LoadPostsAyscncTask("HighlightUsers",context, FriendManagement.this, user_id, false, "", new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
    }

    public void  reportFriends( ArrayList<User> unique_users,final Context context, ArrayList<Post> posts ,final String user_id, final ArrayList<User> users)
    {
        this.context = context; FacebookUtil.isUserPresent = false;
        clearFacebookData();
        new LoadPostsAyscncTask("ReportUsers",context, FriendManagement.this, user_id, false, "", new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
    }

    public void Browse(Context context)
    {

        int check = 2;
        DialogBox dialogBox = new DialogBox();
        String user_id = "";
        dialogBox.ShowDialogBOx3(context, "Select Group", FriendManagement.group1, check, user_id, false);

    }

    public void BrowsePost(Context context ,String user_id)
    {
        int check = 4;
        DialogBox db = new DialogBox();
        db.ShowDialogBOx3(context, "Select Group", FriendManagement.group1, check, user_id, false);
    }


    @Override
    public void getUserAndPostData(ArrayList<Post> result , String option) {
        //FacebookUtil.getFriends();
        FacebookLoginActivity.friend_list = FacebookUtil.users;
        if(option.equals("ReportUsers")) {
            Intent intent = new Intent(context, Users_ListView.class);
            intent.putExtra("isTwitterData", false);
            context.startActivity(intent);
        }
        else if(option.equals("HighlightUsers"))
        {
            int check = 1;
            DialogBox.ShowDialogBOx3(context, "Select Group", FriendManagement.group1, check, "", false);

        }
    }
}
