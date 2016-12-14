package com.cfp.muaavin.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.cfp.muaavin.facebook.Friend;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.web.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.ArrayList;
import java.util.Arrays;


public class FacebookLoginActivity extends ActionBarActivity  {

        TextView textview;

        public Context context;
        String user_id;
        public static ArrayList<Friend> friend_list = new ArrayList<Friend>();
        public static User user = new User();




        CallbackManager callbackManager;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_facebook_login);

        textview = (TextView)findViewById(R.id.tv2);
        context = FacebookLoginActivity.this;

       /* AppEventsLogger.activateApp(this);*/

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_status", "user_photos", "user_videos", "user_tagged_places", "user_actions.video", "user_posts", "user_friends", "public_profile", "read_page_mailboxes", "read_custom_friendlists","user_managed_groups"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


              @Override
              public void onSuccess(LoginResult loginResult) {

                 user_id = loginResult.getAccessToken().getUserId();
                 user.id = user_id;

                 Toast.makeText(FacebookLoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();

                 Intent intent = new Intent(FacebookLoginActivity.this, MenuActivity.class);
                 intent.putExtra("User_signedID", user_id);
                 startActivity(intent);

                 }

                    @Override
                    public void onCancel() {

                        Toast.makeText(FacebookLoginActivity.this, "Cancel",Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(FacebookException exception) {

                        Toast.makeText(FacebookLoginActivity.this, "Error",Toast.LENGTH_LONG).show();

                    }
                });

    }



        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode, data);

         }

}
