package com.cfp.muaavin.facebook;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.cfp.muaavin.ui.FacebookLoginActivity;
import com.cfp.muaavin.ui.MenuActivity;
import com.cfp.muaavin.web.User;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadPostsAyscncTask extends AsyncTask<ArrayList<Post> , Void, ArrayList<Post>> {

    public static FacebookUtil facebookUtil = new FacebookUtil();
    ArrayList<Post> Posts;
    private ProgressDialog dialog;
    AsyncResponsePosts delegate;
    Context context;
    public String userId;
    GraphResponse lastGraphResponse;
    public String user_id;
    public static ArrayList<User> users;
    public String Post_ID;
    public static boolean PostResponse;
    public  static int count = 0;
    public static GraphRequest nextResultsRequests;

    boolean isClipboardData;


    public LoadPostsAyscncTask(Context context, AsyncResponsePosts delegate , String user_id, boolean isClipboardData, String post_id,ArrayList<Post> Posts,ArrayList<User> users) {


        this.delegate = delegate;
        userId = user_id;
        this.isClipboardData = isClipboardData;
        Post_ID = post_id;
        PostResponse = true;
        this.Posts = Posts;
        this.users=users;
        this.context = context;


    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading Posts, Please wait...");
        dialog.show();
        super.onPreExecute();

    }

    @Override
    protected ArrayList<Post> doInBackground(ArrayList<Post>... params) {

        facebookUtil = new FacebookUtil();
        if(!isClipboardData)
        Posts = getAllPosts(Posts); //  Get All Posts

        else
        {
            Posts = new ArrayList<Post>();
            getPost(Post_ID);
        }

        users = facebookUtil.getUsers();

        return Posts;
    }

    @Override
    protected void onPostExecute(ArrayList<Post> result) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        delegate.getUserAndPostData(result);


    }



    public ArrayList<Post> getAllPosts(final ArrayList<Post> posts)
    {

        // 10205871243740520
        if(count == 0) {
            Bundle params = new Bundle();
            params.putString("fields", "message,full_picture,story,created_time,picture,comments{from{id,name,picture},id,message,comments{from{id,name,picture},id,message}}");//,comments.summary(true)
            new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/me/feed",
                    params,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            lastGraphResponse = response;
                            Posts = facebookUtil.getJsonDataPosts(response,posts);

                        }
                    }
            ).executeAndWait();

             nextResultsRequests = lastGraphResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
            count = count + 1;
        }
        else if(count > 0)/*while (nextResultsRequests!=null)*/
        {

            if (nextResultsRequests != null)
            {
                nextResultsRequests.setCallback(new GraphRequest.Callback()
                {
                    @Override
                    public void onCompleted(GraphResponse response)
                    {
                        lastGraphResponse = response;
                        Posts =  facebookUtil.getJsonDataPosts(response,posts);

                    }
                });
                nextResultsRequests.executeAndWait();
            }

            nextResultsRequests = lastGraphResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
        }

        //users = facebookUtil.getUsers();
        return Posts;

    }

    public void getPost(final String post_id)// get Single post
    {

        Bundle params = new Bundle();
        params.putString("fields", "message,full_picture,story,created_time,picture,comments.limit(900){from{id,name,picture},id,message,comments.limit(900){from{id,name,picture},id,message}}");
        GraphRequest gr = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+post_id,
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        if(response.getError()==null) {
                            JSONObject jObjResponse = response.getJSONObject();
                            Posts.add(facebookUtil.getPost(jObjResponse, 0));

                        }
                        else {PostResponse = false;}

                    }
                }
        );
        gr.executeAndWait();
    }

    public static ArrayList<User> getUsers()
    {
        users = facebookUtil.getUsers();
        return users;

    }

    public static boolean getPostResponse() // getPostResponse_ClipBoard
    {
        return PostResponse;

    }













}
