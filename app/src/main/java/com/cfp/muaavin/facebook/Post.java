package com.cfp.muaavin.facebook;

import java.io.Serializable;
import java.util.ArrayList;


public class Post implements Serializable {

   public  String id ;
   public  String message;
   public  String image;
   public String post_url;
   public ArrayList<Comment> Comments = new ArrayList<Comment>();

   public Post()
   {

      image = "";
      message = "";
      post_url = "";

   }

   public Post setPost(String id ,String message ,String image , String post_url  )
   {
      Post post = new Post();
      post. id = id;
      post. message = message;
      post. image =image;
      post.post_url = post_url;
      return post;
   }
}

