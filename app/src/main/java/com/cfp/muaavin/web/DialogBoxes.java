    package com.cfp.muaavin.web;

    import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.cfp.muaavin.facebook.Friend;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.UserData;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.ArrayList;

    /**
    *
    */
    public class DialogBoxes {

    final ArrayList<Post> SelectivePosts = new ArrayList<Post>();
    ArrayList<Double> FriendIds = new ArrayList<Double>();

    final InfringingUser infringing_user = new InfringingUser();



    public void ShowDialogBOx1( final Context context , String str ,final String[] friends, final InfringingUser infringeUser ,  final ArrayList<Friend> friendlist )
    {




        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str);


        builder.setItems(friends, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                infringing_user.id = friendlist.get(which).id;

                getPosts();


                /// will return post type list & then that post type list will be converted to String[] and passed into 2nd dialog box

                String[] posts ={"kjnk", "khasxk", "ksahxk"};


                String[] postArr = Arraylist2StringConverter(SelectivePosts);
                ShowDialogBOx2(  context , "Select Posts" , postArr, SelectivePosts /**/);

            }
        });
        builder.show();


    }



    public void ShowDialogBOx2( final Context context , String str ,final String[] posts, final ArrayList<Post> postList)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str);


        builder.setItems(posts, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                infringing_user.postId = postList.get(which).id;


                ShowDialogBOx3(context, "Select Group", FriendManagement.group);

            }
        });
        builder.show();


    }



    public void ShowDialogBOx3( final Context context , String str ,final String[] group)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str);

        /////////////


        builder.setItems(group, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                infringing_user.groupId = which+1;



            }
        });
        builder.show();



    }


    public void getPosts()
    {

        Bundle params = new Bundle();
        params.putString("fields", "message,full_picture,story,created_time,picture");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                                /* handle the result */
                        getJsonDataPosts(response);

                    }
                }
        ).executeAsync();



    }

    public void getJsonDataPosts(GraphResponse response)
    {


        JSONObject jObjResponse = response.getJSONObject();
        JSONArray data = jObjResponse.optJSONArray("data");



        UserData user_data= new UserData();

        for (int i = 0; i < data.length(); i++) {
            JSONObject subdata = data.optJSONObject(i);
            String str = "";



            if (subdata.has("message")) {

                user_data.message = "Message :" + subdata.optString("message");


                if (subdata.has("id")) {

                   AddComments(subdata);



                    if(function(infringing_user.id, FriendIds)==true)
                    {

                        Post post = new Post();
                        post.message = subdata.optString("message");

                        SelectivePosts.add(post);


                    }





                }

            }



           else if (subdata.has("story")) {

                user_data.story = "Story :" + subdata.optString("story");

                if (subdata.has("id")) {


                    AddComments(subdata);

                    if(function(infringing_user.id, FriendIds)==true)
                    {

                        Post post = new Post();
                        post.message = subdata.optString("story");

                        SelectivePosts.add(post);


                    }



                }



            }



           else if (subdata.has("full_picture")) {

                user_data.full_picture = subdata.optString("full_picture");

                if (subdata.has("id")) {

                    AddComments(subdata);

                    if(function(infringing_user.id, FriendIds)==true)
                    {

                        Post post = new Post();
                        post.message = subdata.optString("message");

                        SelectivePosts.add(post);


                    }


                }


            }

        }


    }


    public void AddComments(JSONObject subdata)
    {

        JSONArray jArr = subdata.optJSONArray("comments");

        for(int i = 0 ; i< jArr.length(); i++)
        {

            JSONObject jObj = jArr.optJSONObject(i);

            Double friend_id = jObj.optDouble("id");

            FriendIds.add(friend_id);



        }



    }

    public Boolean function(double infringuserId , ArrayList<Double> friends_Ids)
    {

        for(int i = 0 ; i < FriendIds.size(); i++)
        {

            if(infringuserId == FriendIds.get(i))
            {

                return true;
            }

        }

        return false;

    }

    public String[] Arraylist2StringConverter(ArrayList<Post>  selectivePostsArrayList)
    {

        String[] SelectivePostArr = new String[selectivePostsArrayList.size()];
        for(int i = 0 ; i < selectivePostsArrayList.size(); i++)
        {


            SelectivePostArr[i] = selectivePostsArrayList.get(i).message;


        }

        return  SelectivePostArr;

    }

}
