package com.cfp.muaavin.twitter;

import android.content.Context;
import android.content.Intent;

import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.ui.Tweet_ListView;
import com.cfp.muaavin.ui.TwitterLoginActivity;
import com.cfp.muaavin.web.DialogBox;

import java.util.ArrayList;

import static com.cfp.muaavin.ui.TwitterLoginActivity.session;

/**
 * Created by Tooba Saeed on 11/01/2017.
 */

public class Controller implements TweetsAsynchronousResponse {

    public String option;
    public Context context;
    public String[] group = {"A","B","C","All"};
    TweetsAsynchronousResponse TwitterAsyncDelegate = this;

    public Controller(Context context, String option)
    {
        this.option = option;
        this.context = context;

        new TweetsAsynchronousLoad(context, TwitterAsyncDelegate,option).execute();

    }


    @Override
    public void tweetsAsynchronousResponse(ArrayList<Post> tweet) {
        if(option.equals("LoadTweets"))
        {
            Intent intent = new Intent(context, Tweet_ListView.class);
            context.startActivity(intent);
        }
        else if(option.equals("LoadFollowers"))
        {
            DialogBox.ShowDialogBOx3(context, "Select Group ", group, 7, String.valueOf(session.getUserId()),true);
        }
    }
}