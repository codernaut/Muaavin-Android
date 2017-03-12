package com.cfp.muaavin.web;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cfp.muaavin.adapter.BrowsePostCustomAdapter;
import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.AsyncResponsePostsDet;
import com.cfp.muaavin.adapter.Browser_CustomAdapter;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.User;
import com.cfp.muaavin.facebook.UserInterface;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.PostDetail;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.twitter.TwitterUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
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
    public static Context context ;
    private String Error = null;
    private ProgressDialog Dialog ;
    String data ="";
    int check;
    public UserInterface UserInterfaceDelegate = null;
    public AsyncResponsePostsDet PostDetailDelegate = null;
    public AsyncResponsePosts Postdelegate = null;
    int ItemPosition;
    ImageButton DislikeBtn;
    BrowsePostCustomAdapter.uIUpdate BrowsePostLayoutDelegate;
    Browser_CustomAdapter.UiUpdate BrowseLayoutDelegate;

    public   WebHttpGetReq(Context c, Activity activity ,int check, AsyncResponsePosts PostDelegate, UserInterface userInterfaceDelegate)
    {
        context  = c;
        this.check = check;
        Dialog = new ProgressDialog(context);
        Postdelegate = PostDelegate;
        UserInterfaceDelegate = userInterfaceDelegate;
        this.activity = activity;
    }

    public   WebHttpGetReq(Context c, int check, TextView text_view, int value, BrowsePostCustomAdapter.uIUpdate browsePostLayout, Browser_CustomAdapter.UiUpdate BrowseLayout)
    {
        context  = c;
        this.check = check;
        Dialog = new ProgressDialog(context);
        //uiUpdate = text_view;
        ItemPosition = value;
        BrowsePostLayoutDelegate = browsePostLayout;
        BrowseLayoutDelegate = BrowseLayout;
    }



    public   WebHttpGetReq(Context c , Activity a, int check , AsyncResponsePostsDet delegate) {

        context = c;
        activity = a;
        this.check = check;
        Dialog = new ProgressDialog(context);
        //uiUpdate = (TextView) activity.findViewById(R.id.output);
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
            //////////
            InputStream is = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet= new HttpGet(params[0]);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if(entity!=null)
            is = entity.getContent();

            //////////
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();


            // Get the server response
            if(is!=null) { reader = new BufferedReader(new InputStreamReader(/*conn.getInputStream())*/is)); }
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            if(reader!=null) while((line = reader.readLine()) != null){ sb.append(line + ""); }
            Content = sb.toString();
        }

        catch(Exception ex){ Error = ex.getMessage(); ex.printStackTrace();}

        finally{ try{ reader.close(); } catch(Exception ex) {ex.printStackTrace();} }
        return null;
    }


    @Override
    protected void onPostExecute(Void unused) {

    Dialog.dismiss();

    if (Error != null) { Content = "Output : "+Error; }

    else
    {
       try
       {   if((Content!=null)&&(!Content.equals(""))) Content = AesEncryption.decrypt(Content);

           if((check == 0 ) || (check == 5)){ DialogBox.showErrorDialog(context,"Message",Content); return; }

           else if((check == 1)||(check == 9) ||(check == 7)){ getInfringingUsersDataFromDB(Content); } // check = 7 // Twitter data

           else if(check == 2) { getReportedPostDetailFromDB( Content); }

           else if(check == 3) { BrowseLayoutDelegate.updateDislikeButton(ItemPosition,Content); }

           else if(check == 10) { BrowseLayoutDelegate.updateFeedBack(ItemPosition,Content); }

           else if(check == 4){ getPostsFromDB(Content); }

           else if(check == 11){ BrowsePostLayoutDelegate.removeItem(ItemPosition); }

       } catch (JSONException e) {  e.printStackTrace(); }
         catch (Exception e) {  e.printStackTrace(); }
    }
 }


    // Get Infringing Users Data From Web Service
    public void getInfringingUsersDataFromDB(String Content) throws JSONException
    {
        ArrayList<String> FacebookBlockedUserIds = new ArrayList<String>();
        ArrayList<String> TwitterBlockedUserIds = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray(Content);
        ArrayList<String> FrendIds = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            User friend = new User();
            JSONObject jsonChildNode = jsonArray.optJSONObject(i);
            friend.setUserInformation(jsonChildNode.optString("User_ID"),jsonChildNode.optString("User_Name"),jsonChildNode.optString("Profile_Pic"),"","unBlocked");
            if(jsonChildNode.optBoolean("IsTwitterUser"))
            { TwitterBlockedUserIds.add(jsonChildNode.optString("User_ID")); }
            else { FacebookBlockedUserIds.add(jsonChildNode.optString("User_ID")); }
            FrendIds.add(/*UrlHelper.getDecodedUrl(friend.profile_pic)*/friend.id);
        }
        String dataType = "Twitter";
        if((check == 1)||(check == 7)) { if(check == 1){ dataType = "Facebook";} UserInterfaceDelegate.getReportedFriends(FrendIds,dataType); }
        else if((check == 9)){  UserInterfaceDelegate.getBlockedUsers(FacebookBlockedUserIds , TwitterBlockedUserIds); }
    }

    // Get Posts From Web Service
    public void getPostsFromDB(String Content) throws JSONException {
        JSONArray jsonArray = new JSONArray(Content);
        ArrayList<String> PostIDs = new ArrayList<String>();
        ArrayList<Post> Posts = new ArrayList<Post>();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            Post post = new Post(); String PostUrl;
            JSONObject jsonChildNode = jsonArray.optJSONObject(i);
            post = post.setPost(jsonChildNode.optString("Post_ID"), jsonChildNode.optString("Post_Detail"), jsonChildNode.optString("Post_Image") ,"https://www.facebook.com/",0);
            post.PostOwner.id = jsonChildNode.optString("infringingUserId");
            if(post.IsTwitterPost = jsonChildNode.optBoolean("IsTwitterPost")) {post.post_url ="https://twitter.com/"+post.PostOwner.id+"/status/"; if(!TwitterUtil.BlockedUserIds.contains(post.PostOwner.id)) Posts.add(post);  };
            if(post.IsComment = jsonChildNode.optBoolean("IsComment")){if(!FacebookUtil.BlockedUsersIds.contains(post.PostOwner.id)) Posts.add(post); };

        }
        Postdelegate.getUserAndPostData(Posts,"BrowsePosts");
    }

    // Get Reports From WebService
    public void getReportedPostDetailFromDB(String Content) throws JSONException {

    JSONArray jsonArray = new JSONArray(Content);
    ArrayList<PostDetail> JsonPostDetails = new ArrayList<PostDetail>();
    HashMap<String, ArrayList<PostDetail>> dictionary = new HashMap<String, ArrayList<PostDetail>>();

    for (int i = 0; i < jsonArray.length(); i++)
    {
       JSONObject jsonChildNode = jsonArray.optJSONObject(i);
       PostDetail PostDetailObj = facebookUtil.getReportedPostDetail(i,jsonChildNode);
       JsonPostDetails.add(PostDetailObj);

       if(dictionary.containsKey(PostDetailObj.post_id))
       {
          ArrayList<PostDetail> reportedPostDetail = (ArrayList<PostDetail>) dictionary.get(PostDetailObj.post_id);
          if(!PostDetailObj.FeedBackMessage.equals("")) { reportedPostDetail.get(0).FeedBacks.add(PostDetailObj.FeedBackMessage); }
          dictionary.put(PostDetailObj.post_id,reportedPostDetail);
       }
       else
       {
          ArrayList<PostDetail> PostDetailslist = new ArrayList<PostDetail>();
          if(!PostDetailObj.FeedBackMessage.equals("")) { PostDetailObj.FeedBacks.add(PostDetailObj.FeedBackMessage); }
          PostDetailslist.add(PostDetailObj);
          if(PostDetailObj.IsTwitterPost){if(!TwitterUtil.BlockedUserIds.contains(PostDetailObj.infringing_user_id)) dictionary.put(PostDetailObj.post_id,PostDetailslist); }
          if(PostDetailObj.IsComment){if(!FacebookUtil.BlockedUsersIds.contains(PostDetailObj.infringing_user_id)) dictionary.put(PostDetailObj.post_id,PostDetailslist); }
       }
    }
       PostDetailDelegate.getPostsDetails(dictionary);
 }

}




