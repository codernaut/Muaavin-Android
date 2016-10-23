package com.cfp.muaavin.web;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.cfp.muaavin.facebook.Friend;

import java.util.ArrayList;

/**
 *
 */
public class FriendManagement {

    final  InfringingUser infringing_user = new InfringingUser();
    static String[] group  =  {"abc","xyz","lmn"};


    public void Highlights()
    {


    }

    public void  reportFriends( ArrayList<Friend> friendList,final Context context)
    {


        String[] friends_posts_Arraylist =  Arraylist2StingConverter(friendList);
        final InfringingUser infringing_user = new InfringingUser();
        DialogBoxes dialogBox = new DialogBoxes();
        dialogBox.ShowDialogBOx1(context, "Select Friends",  friends_posts_Arraylist, infringing_user, friendList);

    }



    public String[] Arraylist2StingConverter(ArrayList<Friend> array)
    {


      final   String[] str = new String[array.size()];

        for(int i = 0 ; i < array.size() ; i++ )
        {


                str[i] = array.get(i).name;
            //str[i] = array.get(i);

        }

        return str;

    }

}
