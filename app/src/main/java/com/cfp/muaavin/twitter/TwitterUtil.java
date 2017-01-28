package com.cfp.muaavin.twitter;

import com.cfp.muaavin.facebook.Comment;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.PostDetail;
import com.cfp.muaavin.ui.TwitterLoginActivity;
import com.cfp.muaavin.web.User;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

import static com.cfp.muaavin.ui.TwitterLoginActivity.session;

/**
 *
 */

public class TwitterUtil {

    public static ArrayList TwitterPosts = new ArrayList<Post>();
    public static ArrayList<User> TwitterUsers = new ArrayList<User>();
    public static  ArrayList<Post> Tweets = new ArrayList<Post>();
    public  static PostDetail ReportTwitterDetail = new PostDetail();
    public static ArrayList<String> tweetIds = new ArrayList<String>();
    public static ArrayList<User> Followers = new ArrayList<User>();
    public static ArrayList<String> BlockedUserIds = new ArrayList<String>();
    public static User user = new User();



    public ArrayList<Post> getTweets(List<Tweet> result)
    {
        ArrayList<Post> tweets = new ArrayList<Post>();
        for(int i = 0 ; i < result.size(); i++)
        {
            Post post = new Post();
            post.id = result.get(i).idStr;
            post.message = result.get(i).text+"\n" +"\n"+result.get(i).createdAt;
            post.PostOwner.id = result.get(i).user.idStr;
            post.PostOwner.name = result.get(i).user.name;
            post.PostOwner.profile_pic = result.get(i).user.profileImageUrl;

            if((!tweetIds.contains(post.id))&&(!TwitterUtil.BlockedUserIds.contains(post.PostOwner.id))&&(!post.PostOwner.id.equals(String.valueOf(session.getUserId()))))
            { tweetIds.add(post.id); Tweets.add(post); }
            tweets.add(post);
        }
        return Tweets;
    }

    public static void clearTwitterData()
    {
        Tweets = new ArrayList<Post>();
        tweetIds = new ArrayList<String>();
        TweetsAsynchronousLoad.FollowersCursor = -1l;
        TweetsAsynchronousLoad.maxId = 0l;
        Followers = new ArrayList<User>();
    }

}
