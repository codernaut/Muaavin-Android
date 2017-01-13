package com.cfp.muaavin.facebook;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import com.cfp.muaavin.ui.FacebookLoginActivity;
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
public class FriendsAsynchronousLoad extends AsyncTask<ArrayList<User> , Void, ArrayList<User>> {

    private ProgressDialog dialog;
    public Context context;
    public static ArrayList<User> users;

    public FriendsAsynchronousLoad(Context contex)
    {
        context = contex;

    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading Friends Data, Please Wait..");
        dialog.show();
        super.onPreExecute();

    }
    @Override
    protected ArrayList<User> doInBackground(ArrayList<User>... params) {

        getFriends();
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<User> result) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

    }

    public void getFriends()
    {

         Bundle params = new Bundle();
         params.putString("limit","900");
         GraphRequest gr =  new GraphRequest(
         AccessToken.getCurrentAccessToken(),
        "/"+User.getLoggedInUserInformation().id+"/taggable_friends",
         params,
         HttpMethod.GET,
         new GraphRequest.Callback() {
         public void onCompleted(GraphResponse response) {
         /* handle the result */
         if(response.getError()==null)
         getJsonFriends(response);
         }});
         gr.executeAndWait();

    }


    public ArrayList<User> getJsonFriends(GraphResponse response)
    {
        ArrayList<User> users = new ArrayList<User>();
        JSONArray FriendsData = response.getJSONObject().optJSONArray("data");

        for(int i = 0 ; i < FriendsData.length() ; i++)
        {
            User user = new User();
            JSONObject friend = FriendsData.optJSONObject(i);
            user.setUserInformation(friend.optString("id"),friend.optString("name"),friend.optJSONObject("picture").optJSONObject("data").optString("url"),"https://www.facebook.com/" + friend.optString("id"),"");
            users.add(user);
        }
        FacebookLoginActivity.friend_list = users;
        return users;
    }

}
