package com.cfp.muaavin.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.widget.TextView;
import android.widget.Toast;

import com.cfp.muaavin.ui.FacebookLoginActivity;
import com.cfp.muaavin.ui.MainActivity;
import com.cfp.muaavin.ui.MenuActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 */
    public class FacebookUtil {

    TextView textView;
    CallbackManager callback_manager;

    Context context;
    LoginButton login_button;
    public ArrayList<Post> User_Posts = new ArrayList<Post>();
    public ArrayList<Friend> friend_list = new ArrayList<Friend>();


   /* public void login(LoginButton lb , Context c, CallbackManager call_back_manager)
    {

                        context = c;

                        callback_manager = call_back_manager;
                        login_button = lb;
                        // callbackManager = CallbackManager.Factory.create();
                        // callback_manager = call_back_manager;
                        LoginManager.getInstance().registerCallback(call_back_manager,
                        new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // App code
                            login_button.setReadPermissions(Arrays.asList("public_profile", "user_friends"));
                            //Toast.makeText(MainActivity.this, "Login Successful",
                            //       Toast.LENGTH_LONG).show();


                            ////////////////////////////////

                            Intent intent = new Intent(context, MenuActivity.class);
                            intent.putExtra("User_Posts", User_Posts);
                            intent.putExtra("User_friends", friend_list);
                            context.startActivity(intent);

                            //////////////////////////////

                            //////////////////////////////////////////////////////// Get User Posts
                            getPosts();

                            ////////////////////////////////////////////////////////// Get Friends List
                            Populate_Friendlist();

                        }

                        @Override
                        public void onCancel() {
                            // App code
                            //  Toast.makeText(MainActivity.this, "Cancel",
                            //        Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            // Toast.makeText(MainActivity.this, "Error",
                            //         Toast.LENGTH_LONG).show();
                        }
                    });




    }
*/
 /*         public void getPosts(){

            // Context context = c;

            // LoginManager.getInstance().logInWithReadPermissions(c, Arrays.asList("user_status", "user_photos", "user_videos", "user_tagged_places", "user_actions.video", "user_posts", "user_friends", "public_profile", "read_custom_friendlists"));
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
*/
 /*   public  ArrayList<Friend> getFriends()
    {

            Friend fr = new Friend();
            fr.name = "abc";
            fr.id = 1;
            friend_list.add(fr);
            fr = new Friend();
            fr.name = "lmn";
            fr.id = 2;
            friend_list.add(fr);
            fr = new Friend();
            fr.name = "xyz";
             fr.id = 3;
            friend_list.add(fr);

           // friend_list.add("abc");
           // friend_list.add("xyz");
           // friend_list.add("lmn");



        return friend_list;
    }

*/
 /*     public void Populate_Friendlist()
      {

                Bundle params1 = new Bundle();
                params1.putString("fields", "name");
                new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + AccessToken.getCurrentAccessToken().getUserId() + "/friends",

                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {


                        //Populate_Friendlist(response);

                        ///////////////
                        String sa = String.valueOf("Text2 " + response.getJSONObject());
                        //tv2.setText(sa);
                        // Toast.makeText(MainActivity.this, "Text2 " + sa,
                        //        Toast.LENGTH_LONG).show();

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
*/
 /*   public void getJsonDataPosts(GraphResponse response)
    {

        ///////////////
        //String s = String.valueOf(response.getJSONObject());
        //tv.setText(s);

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

                //user_data.message = "Message :" + subdata.optString("message");

                // str =str +  user_data.message + "\n";

                // check = check + 1;

                user_data.post_id = subdata.optString("id");

                //str = str + "User post id"+ user_data.post_id;

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

        //tv.setText(str);


    }


*/

}
