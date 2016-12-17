package com.cfp.muaavin.web;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.AsyncResponsePostsDet;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.UserInterface;


import com.cfp.muaavin.facebook.Friend;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.PostDetail;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.ui.R;
import com.cfp.muaavin.ui.WebServiceActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


public class WebHttpGetReq extends AsyncTask<String, Void, Void> {


    FacebookUtil facebookUtil = new FacebookUtil();
    private String Content;
    public Activity activity;
    public static Activity a;
    TextView uiUpdate;
    TextView jsonParsed ;

    public static Context context ;
    private String Error = null;
    private ProgressDialog Dialog ;
    String data ="";
    int check;
    public UserInterface UserInterfaceDelegate = null;
    public AsyncResponsePostsDet PostDetailDelegate = null;
    public AsyncResponsePosts Postdelegate = null;
    int total_unlikes;

    ArrayList<Friend> Common_FriendsIds = new ArrayList<Friend>();

    public   WebHttpGetReq(Context c, Activity activity ,int check, AsyncResponsePosts PostDelegate, UserInterface userInterfaceDelegate)
    {
        context  = c;
        this.check = check;
        Dialog = new ProgressDialog(context);
        Postdelegate = PostDelegate;
        UserInterfaceDelegate = userInterfaceDelegate;
        this.activity = activity;

    }
    public   WebHttpGetReq(Context c, int check, TextView text_view, int value)
    {
        context  = c;
        this.check = check;
        Dialog = new ProgressDialog(context);
        uiUpdate = text_view;
        total_unlikes = value;
    }



    public   WebHttpGetReq(Context c , Activity a, int check , AsyncResponsePostsDet delegate) {

        context = c;
        activity = a;
        this.check = check;
        Dialog = new ProgressDialog(context);
        uiUpdate = (TextView) activity.findViewById(R.id.output);
        jsonParsed = (TextView) activity.findViewById(R.id.jsonParsed);
        PostDetailDelegate = delegate;
    }



    @Override
    protected void onPreExecute() {

        Dialog.setMessage("Please wait..");
        Dialog.show();

        try{ data +="&" + URLEncoder.encode("data", "UTF-8") + "=";}//+serverText.getText();

        catch (UnsupportedEncodingException e) {  e.printStackTrace();  }

    }

    @Override
    protected Void doInBackground(String... params) {

        BufferedReader reader=null;

        try
        {
            URL url = new URL(params[0]);

            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null){ sb.append(line + ""); }
            Content = sb.toString();
        }

        catch(Exception ex){ Error = ex.getMessage(); }

        finally{ try{ reader.close(); } catch(Exception ex) {ex.printStackTrace();} }
        return null;
    }


    @Override
    protected void onPostExecute(Void unused) {

        Dialog.dismiss();

        if (Error != null) { Content = "Output : "+Error; }

         else {

            JSONArray jsonArray;
            try {
                Content = AesEncryption.decrypt(Content);

                if(check == 0 ) {
                    uiUpdate = (TextView) activity.findViewById(R.id.output);
                    uiUpdate.setText(Content);
                    WebServiceActivity.response = Content;
                    return;
                }

                else if ((check == 1)||(check == 9)) {

                    ArrayList<String> BlockedUserIds = new ArrayList<String>();
                    jsonArray = new JSONArray(Content);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        Friend friend = new Friend();
                        JSONObject jsonChildNode = jsonArray.optJSONObject(i);
                        friend.setFriendData(jsonChildNode.optString("User_ID"), jsonChildNode.optString("User_Name"));
                        BlockedUserIds.add(jsonChildNode.optString("User_ID"));
                        Common_FriendsIds.add(friend);
                    }
                    if(check == 1) { UserInterfaceDelegate.getReportedFriends(Common_FriendsIds); }
                    else if(check == 9){  UserInterfaceDelegate.getBlockedUsers(BlockedUserIds); }
                }

                else if(check == 2)
                {

                    jsonArray = new JSONArray(Content);
                    ArrayList<PostDetail> JsonPostDetails = new ArrayList<PostDetail>();
                    HashMap<String, ArrayList<PostDetail>> dictionary = new HashMap<String, ArrayList<PostDetail>>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonChildNode = jsonArray.optJSONObject(i);
                        PostDetail PostDetailObj = facebookUtil.getReportedPostDetail(i,jsonChildNode);//new PostDetail();
                        JsonPostDetails.add(PostDetailObj);

                        if(dictionary.containsKey(PostDetailObj.post_id)) {
                            ArrayList<PostDetail> reportedPostDetail = (ArrayList<PostDetail>) dictionary.get(PostDetailObj.post_id);
                            reportedPostDetail.add(PostDetailObj);
                            dictionary.put(PostDetailObj.post_id,reportedPostDetail);
                        }
                        else
                        {
                            ArrayList<PostDetail> PostDetailslist = new ArrayList<PostDetail>();
                            PostDetailslist.add(PostDetailObj);
                            dictionary.put(PostDetailObj.post_id,PostDetailslist);
                        }
                    }
                    PostDetailDelegate.getPostsDetails(dictionary);
                }

                else if(check == 3)
                {
                    if(Content.equals("record successfully inserted"))
                    {
                        total_unlikes=total_unlikes+1;
                        uiUpdate.setText(String.valueOf(total_unlikes));
                    }
                    else
                    {
                        total_unlikes=total_unlikes-1;
                        uiUpdate.setText(String.valueOf(total_unlikes));
                    }

                }

                else if(check == 4)
                {

                    jsonArray = new JSONArray(Content);
                    ArrayList<Post> Posts = new ArrayList<Post>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Post post = new Post();
                        JSONObject jsonChildNode = jsonArray.optJSONObject(i);
                        post = post.setPost(jsonChildNode.optString("Post_ID"), jsonChildNode.optString("Post_Detail"), jsonChildNode.optString("Post_Image") ,"https://www.facebook.com/"+jsonChildNode.optString("Post_ID"));
                        Posts.add(post);
                    }
                    Postdelegate.getUserAndPostData(Posts);
                }

                else if(check == 5)
                {

                    if(Content.equals("User Already Blocked"))
                        User.user_authentication = false;
                    else User.user_authentication = true;
                }
            } catch (JSONException e) {  e.printStackTrace(); }
              catch (Exception e) {  e.printStackTrace(); }

        }
    }

    public   void callOnUiThread()
    {
        if(Content.equals("record successfully inserted"))
        {

            total_unlikes=total_unlikes+1;
            uiUpdate.setText(String.valueOf(total_unlikes));
        }
        else
        {
            total_unlikes=total_unlikes-1;
            uiUpdate.setText(String.valueOf(total_unlikes));
        }

    }



}




