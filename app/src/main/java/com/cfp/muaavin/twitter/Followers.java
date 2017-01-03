package com.cfp.muaavin.twitter;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.Timeline;

import java.util.List;

/**
 *
 */

public class Followers {
    @SerializedName("users")
    public final List<User> users;





    public Followers(List<User> users) {
        this.users = users;
    }
}
