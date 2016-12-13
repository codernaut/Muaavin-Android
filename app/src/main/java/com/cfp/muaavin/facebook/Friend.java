package com.cfp.muaavin.facebook;

import java.io.Serializable;
import java.util.ArrayList;


public class Friend implements Serializable {

    public String id;
    public String name;
    public String post_id;
    public String profile_pic;
    public ArrayList<String> commentsList = new ArrayList<String>();
    public String profile_url;

    public Friend()
    {

        post_id = "";
        profile_pic = "";
        profile_url = "";

    }


}
