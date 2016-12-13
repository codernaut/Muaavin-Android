package com.cfp.muaavin.web;

import com.facebook.AccessToken;
import com.facebook.Profile;

import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable {

    public String name;
    public String id;
    public String profile_pic;
    public String profile_url;
    public static boolean user_authentication;

   public User()
   {


       profile_pic = "";
       profile_url = "";
       user_authentication = true;

    }

    public static User getUserInformation() {

        User user = new User();

        Profile profile = Profile.getCurrentProfile();
        user.id = AccessToken.getCurrentAccessToken().getUserId();
        user.name = profile.getFirstName() + " " + profile.getLastName();
        user.profile_pic = (profile.getProfilePictureUri(20, 20).toString());
        return user;

    }
}
