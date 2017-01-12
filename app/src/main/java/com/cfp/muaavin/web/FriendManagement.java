package com.cfp.muaavin.web;

import android.content.Context;
import android.content.Intent;

import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.LoadPostsAyscncTask;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.ui.Browse_Activity;
import com.cfp.muaavin.ui.Users_ListView;

import java.util.ArrayList;



public class FriendManagement implements AsyncResponsePosts {

    static String[] group1  =  {"A","B","C","All"};
    Context context;



    public void Highlights(Context context)
    {
        int check = 1;
        DialogBox dialogBox = new DialogBox();
        String user_id = "";
        dialogBox.ShowDialogBOx3(context, "Select Group", FriendManagement.group1, check, user_id, false);



    }

    public void  reportFriends( ArrayList<User> unique_users,final Context context, ArrayList<Post> posts ,final String user_id, final ArrayList<User> users)
    {


        //Intent intent  = new Intent(context, Users_ListView.class);
        this.context = context; FacebookUtil.isUserPresent = false;
        if (FacebookUtil.users.size() > 0)
        {
            Intent intent = new Intent(context,Users_ListView.class); intent.putExtra("isTwitterData",false);
            context.startActivity(intent);
        }
        else{ new LoadPostsAyscncTask(context, FriendManagement.this, user_id, false, "", new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>()); }

        //intent.putExtra("user_id",user_id);
        //intent.putExtra("user_posts",posts);
        //intent.putExtra("commented_users",unique_users);
        //intent.putExtra("isTwitterData",false);

        //context.startActivity(intent);
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
    public void getUserAndPostData(ArrayList<Post> result) {
        Intent intent = new Intent(context, Users_ListView.class);
        intent.putExtra("isTwitterData",false);
        context.startActivity(intent);

    }
}
