package com.cfp.muaavin.web;

import android.content.Context;
import android.content.Intent;

import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.ui.Browse_Activity;
import com.cfp.muaavin.ui.Users_ListView;

import java.util.ArrayList;



public class FriendManagement {

    static String[] group1  =  {"A","B","C","All"};



    public void Highlights(Context context)
    {
        int check = 1;
        DialogBox dialogBox = new DialogBox();
        String user_id = "";
        dialogBox.ShowDialogBOx3(context, "Select Group", FriendManagement.group1, check, user_id);


    }

    public void  reportFriends( ArrayList<User> unique_users,final Context context, ArrayList<Post> posts ,final String user_id, final ArrayList<User> users)
    {


        Intent intent  = new Intent(context, Users_ListView.class);

        intent.putExtra("user_id",user_id);
        intent.putExtra("user_posts",posts);
        intent.putExtra("commented_users",unique_users);

        context.startActivity(intent);
    }

    public void Browse(Context context)
    {


        int check = 2;
        DialogBox dialogBox = new DialogBox();
        String user_id = "";
        dialogBox.ShowDialogBOx3(context, "Select Group", FriendManagement.group1, check, user_id);




    }

    public void BrowsePost(Context context ,String user_id)
    {

        int check = 4;
        DialogBox db = new DialogBox();

        db.ShowDialogBOx3(context, "Select Group", FriendManagement.group1, check, user_id);


    }





}
