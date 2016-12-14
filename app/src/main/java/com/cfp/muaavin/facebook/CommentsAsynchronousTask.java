package com.cfp.muaavin.facebook;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.cfp.muaavin.ui.MenuActivity;
import com.cfp.muaavin.web.User;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class CommentsAsynchronousTask extends AsyncTask<ArrayList<Post> , Void, ArrayList<Post>> {

    private ProgressDialog dialog;
    public Context context;
    ArrayList<Post> Posts;

    public int check;
    ArrayList<String> friendsIds;
    public static ArrayList<User> users;
    public ArrayList<Comment> comments;
    public String user_id;
    FacebookUtil facebookUtil ;



    public AsyncResponseComments delegate = null;


    public CommentsAsynchronousTask( Context contex , int index ,AsyncResponseComments delegate , int check, String user_signedin_ID, int option) {

        this.context = contex;
        this.delegate = delegate;
        this.check = check;
        user_id = user_signedin_ID;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();
        super.onPreExecute();

    }
    @Override
    protected ArrayList<Post> doInBackground(ArrayList<Post>... params) {

        Posts = new ArrayList<Post>();
        friendsIds = new ArrayList<String>();
        users = new ArrayList<User>();
        facebookUtil = new FacebookUtil();

        Posts = params[0];

       // getCommentsAndReplies();
       // getProfilePics();
        getIndividualPost("10205871243740520_10203936050721904"/*"1678113695797841"*/);

        return Posts ;
    }




    @Override
    protected void onPostExecute(ArrayList<Post> result) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        MenuActivity.check = check;
        delegate.getAllComments(result);


    }

    public  ArrayList<Comment> getJsonComments(GraphResponse response, String post_id ,String parent_CommentID, int isReply)
    {

        ArrayList<Comment> comments = new ArrayList<Comment>();
        JSONObject content = response.getJSONObject();
        JSONArray data =  content.optJSONArray("data");

        for(int i = 0 ; i < data.length() ; i++)
        {

            JSONObject obj = data.optJSONObject(i);
            JSONObject  from = obj.optJSONObject("from");
            Comment comment = new Comment();
            comment.name  =       from.optString("name");
            comment.user_id= from.optString("id");
            comment.comment_id = obj.optString("id");
            comment.post_id = post_id;
            String  message = obj.optString("message");
            comment.message = message;
            ///////////////
            JSONObject  coments = obj.optJSONObject("comments");
            JSONArray replies =  coments.optJSONArray("data");

            for(int r = 0 ; r < replies.length(); r++ )
            {
                JSONObject reply_obj = replies.optJSONObject(r);
                JSONObject  from1 = reply_obj.optJSONObject("from");
                Comment comment1 = new Comment();
                comment1.name  =       from1.optString("name");
                comment1.user_id= from1.optString("id");
                comment1.comment_id = reply_obj.optString("id");
                comment1.post_id = post_id;
                comment1.message = reply_obj.optString("message");
                comment1.parent_comment_id = parent_CommentID;
                comments.add(comment1);

                if(!friendsIds.contains(comment1.user_id)&&(!comment1.user_id.equals(user_id)))
                {
                    friendsIds.add(comment1.user_id);

                    User user = new User();
                    user.id = comment1.user_id;
                    user.name = comment1.name;
                    user.profile_url = "https://www.facebook.com/"+comment1.user_id;

                    users.add(user);
                }



            }



            //////////////


            if(!friendsIds.contains(comment.user_id)&&(!comment.user_id.equals(user_id)))
            {
                friendsIds.add(comment.user_id);

                User user = new User();
                user.id = comment.user_id;
                user.name = comment.name;
                user.profile_url = "https://www.facebook.com/"+comment.user_id;

                users.add(user);
            }

            if(isReply == 1)
            {
             comment.parent_comment_id = parent_CommentID;
            }

            comments.add(comment);

        }


        return comments;

    }

    public String getProfile_pic(GraphResponse response)
    {

        JSONObject content = response.getJSONObject();
        JSONObject data =  content.optJSONObject("data");
        String   url =  data.optString("url");


        return url;
    }


    public void getGraphApiComments(final int post_index)
    {

        Bundle params = new Bundle();
        //params.putString("limit","0");
        params.putString("fields","id,comments.summary(true),message,from,likes");


        GraphRequest gr = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + Posts.get(post_index ).id + "/comments",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        if(response.getError()==null)
                        Posts.get(post_index).Comments =    getJsonComments(response, Posts.get(post_index).id,"",0);


                    }
                }
        );

        gr.executeAndWait();


    }

    public void getGraphApiReplies(final String CommentID, final int comment_index , final int post_index)
    {

        GraphRequest gr = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + CommentID + "/comments",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        if(response.getError()==null)
                        comments.get(comment_index).replies  =    getJsonComments(response, Posts.get(post_index).id,CommentID,1);


                    }
                }
        );

        gr.executeAndWait();


    }

    public void getGraphApiProfilePics(final int user_index)
    {

        Bundle params1 = new Bundle();
        params1.putBoolean("redirect", false);
        GraphRequest gr = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + users.get(user_index).id + "/picture",
                params1,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                            /* handle the result */
                        if(response.getError()==null)
                        users.get(user_index).profile_pic = getProfile_pic(response);


                    }
                }
        );

        gr.executeAndWait();


    }

    public void getCommentsAndReplies()
    {
        for(int postID = 0 ;postID < Posts.size() ; postID++)
        {
            final  int post_index = postID;
            getGraphApiComments(post_index);

            comments = Posts.get(post_index).Comments;

            for(int j = 0; j < comments.size(); j++ )
            {
                final int comment_index = j;

                final String CommentID = comments.get(j).comment_id;

               // getGraphApiReplies(CommentID,comment_index,  post_index);

            }
        }

    }

    public void getProfilePics()
    {

        for(int user_index = 0; user_index < users.size(); user_index++)
        {

            final int index = user_index;
            getGraphApiProfilePics(index);

        }

        FacebookUtil.Commented_users = users;

    }

    public void getIndividualPost(final String post_id)
    {

        Bundle params = new Bundle();
        params.putString("fields","message,full_picture,story,created_time,picture,comments.limit(900){from{id,name,picture},id,message,comments.limit(900){from{id,name,picture},id,message}}");
        GraphRequest gr = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+post_id,
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        JSONObject jObjResponse = response.getJSONObject();
                        Posts.add(facebookUtil.getPost(jObjResponse,0));
                    }
                }
        );
        gr.executeAndWait();


    }


}
