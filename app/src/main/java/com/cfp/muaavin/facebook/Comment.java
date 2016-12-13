package com.cfp.muaavin.facebook;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class Comment  implements Serializable {

    public String parent_comment_id;
    public String comment_id;
    public String name;
    public String post_id;
    public String user_id;
    public String message;

    public ArrayList<Comment> replies = new ArrayList<Comment>();

    public  Comment()
    {

        parent_comment_id = "";
        comment_id = "";
        user_id = "";
        name = "";
        post_id = "";


    }
}


