package com.cfp.muaavin.web;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable {

    public String name;
    public String id;
    public String profile_pic;
    public String profile_url;
    public static boolean user_authentication = true;
    public String state;

   public User()
   {


       profile_pic = "";
       profile_url = "";


    }

    public static User getLoggedInUserInformation() {

        User user = new User();

        Profile profile = Profile.getCurrentProfile();
        user.id = AccessToken.getCurrentAccessToken().getUserId();
        user.name = profile.getFirstName() + " " + profile.getLastName();
        user.profile_pic = (profile.getProfilePictureUri(20, 20).toString());
        user.state = "UnBlocked";
        return user;

    }
    public static User getTwitterUserLoggedInInformation() {

        User user = new User();
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        user.id = String.valueOf(session.getUserId());
        user.name = String.valueOf(session.getUserName());
        user.state = "unBlocked";
        user.profile_pic = "fyfhgh";
        return user;

    }

    public  void setUserInformation(String user_id , String user_name, String profilePic, String profileUrl, String State) {

        id  = user_id;
        name = user_name;
        profile_pic = profilePic;
        profile_url = profileUrl;
        state = State;
    }
}
