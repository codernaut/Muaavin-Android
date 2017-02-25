package com.cfp.muaavin.facebook;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.helper.UrlHelper;
import com.cfp.muaavin.ui.BrowsePost_ListView;
import com.cfp.muaavin.ui.FacebookLoginActivity;
import com.cfp.muaavin.ui.R;
import com.cfp.muaavin.web.ImageSelectorAsyncTask;
import com.cfp.muaavin.web.WebHttpGetReq;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import static com.cfp.muaavin.ui.TwitterLoginActivity.session;

/**
 *
 */
public class BrowsePostCustomAdapter extends BaseAdapter {

    public ArrayList<Post> Posts;
    Context context;
    private static LayoutInflater inflater=null;
    public String GroupName;
    public  String user_id;


    public BrowsePostCustomAdapter(BrowsePost_ListView browsePost_activity, ArrayList<Post> posts, String group_name, String userId)
    {
        Posts  = posts;
        GroupName = group_name;
        context = browsePost_activity;
        user_id = userId;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public class Holder {

        TextView PostTextview;
        Button CrossButton;
        ImageView post_image;
        TextView PostHeading;
    }


    @Override
    public int getCount() {
        return Posts.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        final Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.browsepost_row_layout, null);//
        holder.PostTextview = (TextView)rowView.findViewById(R.id.Textbox1);
        holder.PostHeading = (TextView)rowView.findViewById(R.id.Textbox2);
        holder.CrossButton = (Button)rowView.findViewById(R.id.CrossButton);
        holder.post_image =  (ImageView)rowView.findViewById(R.id.Image_view);
        holder.PostTextview.setText(Posts.get(position).message);

        if(Posts.get(position).IsTwitterPost)
        {
            holder.PostHeading.setBackgroundColor(Color.parseColor("#00BFFF"));
            holder.PostHeading.setText("Tweet");
        }


        holder.CrossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String serverURL = null;
                try
                {
                  if(Posts.get(position).IsTwitterPost) { user_id = String.valueOf(session.getUserId()); }
                  serverURL = "http://13.76.175.64:8080/Muaavin-Web/rest/Posts_Query/DeletePosts?Post_id=" + AesEncryption.encrypt(Posts.get(position).id) + "&Group_name=" + AesEncryption.encrypt(GroupName) + "&User_id=" + AesEncryption.encrypt(user_id) +"&InfringingUserID="+AesEncryption.encrypt(Posts.get(position).PostOwner.id)+ "&isPostOfSpecificUser=" + true+"&IsTwitterPost="+Posts.get(position).IsTwitterPost+"&IsComment="+Posts.get(position).IsComment;
                } catch (Exception e) { e.printStackTrace(); }

                new WebHttpGetReq(context, 5, holder.PostTextview, 6).execute(serverURL);

            }
        });

        holder.PostHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UrlHelper.showDataOnBrowser(context , Posts.get(position).post_url);
            }
        });

        Posts.get(position).image = UrlHelper.getDecodedUrl((Posts.get(position).image));
        new ImageSelectorAsyncTask(holder.post_image, holder.PostTextview).execute(Posts.get(position).image);




        return rowView;
    }

}
