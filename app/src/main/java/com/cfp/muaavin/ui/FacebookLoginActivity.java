package com.cfp.muaavin.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.Friend;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.UserData;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;


public class FacebookLoginActivity extends ActionBarActivity {

        TextView textview;
        FacebookUtil facebook_util = new FacebookUtil();
        public  ArrayList<Post> User_Posts = new ArrayList<Post>();
        public ArrayList<Friend> friend_list = new ArrayList<>();


        CallbackManager callbackManager;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_facebook_login);

        textview = (TextView)findViewById(R.id.tv2);

       /* AppEventsLogger.activateApp(this);*/

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_status", "user_photos", "user_videos", "user_tagged_places", "user_actions.video", "user_posts", "user_friends", "public_profile", "read_custom_friendlists"));


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                 @Override
                 public void onSuccess(LoginResult loginResult) {

                  Toast.makeText(FacebookLoginActivity.this, "Login Successful",Toast.LENGTH_LONG).show();



                  //////////////////////////////////////////////////////// Get User Posts

                    getPosts();

                  ////////////////////////////////////////////////////////// Get Friends List


                    Populate_Friendlist();
                     getFriends();




                     Intent intent = new Intent(FacebookLoginActivity.this, MenuActivity.class);
                     intent.putExtra("User_Posts", /*facebook_util.User_Posts*/User_Posts);
                     intent.putExtra("User_friends", /*facebook_util.friend_list*/friend_list);
                     startActivity(intent);


                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(FacebookLoginActivity.this, "Cancel",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(FacebookLoginActivity.this, "Error",
                                Toast.LENGTH_LONG).show();
                    }
                });

    }



        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,
                resultCode, data);
         }


         public void getPosts() {

             Bundle params = new Bundle();
             params.putString("fields", "message,full_picture,story,created_time,picture");
             new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        getJsonDataPosts(response);

                    }
                }
                ).executeAsync();



    }

         public void getJsonDataPosts(GraphResponse response) {

        ///////////////
            String s = String.valueOf(response.getJSONObject());
            textview.setText(s);

        //////////////
            JSONObject jObjResponse = response.getJSONObject();
            JSONArray data = jObjResponse.optJSONArray("data");


            int check = 0;
            String str = "";
            UserData user_data= new UserData();

            for (int i = 0; i < data.length(); i++) {
            JSONObject subdata = data.optJSONObject(i);
            str = "";
            if (subdata.has("message")) {

                user_data.message = "Message :" + subdata.optString("message");

                str =str +  user_data.message + "\n";

                check = check + 1;

            }

            if (subdata.has("id")) {



                user_data.post_id = subdata.optString("id");



            }



            if (subdata.has("story")) {

                user_data.story = "Story :" + subdata.optString("story");

                str = str + user_data.story + "\n";

                check = check + 1;


            }

            if (subdata.has("created_time")) {

                user_data.created_time = "Created Time :" + subdata.optString("created_time");

                str = str + user_data.created_time + "\n";



                check = check + 1;




            }

            if (subdata.has("full_picture")) {

                user_data.full_picture = subdata.optString("full_picture");


                Post post = new Post();
                post.message = (user_data.full_picture);
                post.id = user_data.post_id;

                User_Posts.add(post);


            }



            if (check > 0) {


               // arr.add(str);

                Post post = new Post();
                post.message = (str);
                post.id = user_data.post_id;

                User_Posts.add(post);

                check = 0;

            }

        }
        //tv.setText(str);

       // setArrayAdapte();
        str = "";
        for(int i = 0 ; i < User_Posts.size() ; i++ )
        {

           str = str + User_Posts.get(i).message + " " + "id :"+ User_Posts.get(i).id +" ";


        }

        //textview.setText(str);


    }

        public void Populate_Friendlist(){

        Bundle params1 = new Bundle();
        params1.putString("fields", "name");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + AccessToken.getCurrentAccessToken().getUserId() + "/friends",

                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                                /* handle the result */



                        String sa = String.valueOf("Text2 " + response.getJSONObject());


                        JSONObject jobj = ( response.getJSONObject());

                        JSONArray jArray =    jobj.optJSONArray("Data");
                        if(jArray == null)
                        {

                            return;

                        }


                        for(int i = 0; i  < jArray.length(); i++)
                        {

                            JSONObject jsonObj =  jArray.optJSONObject(i);
                            String name = jsonObj.optString("name");
                            Double id = jsonObj.optDouble("id");

                            Friend friend = new Friend();
                            friend.name = name;
                            friend.id = id;

                            friend_list.add(friend);

                        }
                        return;


                        //////////////

                    }
                }
        ).executeAsync();



    }

        public  ArrayList<Friend> getFriends()
        {

            Friend friend = new Friend();
            friend.name = "abc";
            friend.id = 1;
            friend_list.add(friend);
            friend= new Friend();
            friend.name = "lmn";
            friend.id = 2;
            friend_list.add(friend);
            friend= new Friend();
            friend.name = "xyz";
            friend.id = 3;
            friend_list.add(friend);





        return friend_list;
    }

    


}
