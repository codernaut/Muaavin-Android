package com.cfp.muaavin.twitter;

//import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.Callback;

import com.google.gson.stream.JsonToken;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 */

public interface CustomService
{
    //statuses/mentions_timeline.json
    @GET("/1.1/followers/list.json")//@GET("/1.1/users/show.json") /1.1/followers/show.json,1.1/followers/list.json
    Call<ResponseBody> show(@Query("user_id") long id);

}
