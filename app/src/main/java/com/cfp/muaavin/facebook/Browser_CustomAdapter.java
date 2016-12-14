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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.helper.UrlHelper;
import com.cfp.muaavin.ui.Browse_Activity;
import com.cfp.muaavin.ui.FacebookLoginActivity;
import com.cfp.muaavin.ui.Post_ListView;
import com.cfp.muaavin.ui.R;
import com.cfp.muaavin.web.DialogBox;
import com.cfp.muaavin.web.ImageSelectorAsyncTask;
import com.cfp.muaavin.web.WebHttpGetReq;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Browser_CustomAdapter extends BaseAdapter {

    HashMap<String, ArrayList<PostDetail>> result;
    ArrayList<PostDetail> Post_Details;
    Context context;
    String s;
    int[] thumbValueAlreadyset;


    private static LayoutInflater inflater=null;

    public Browser_CustomAdapter(Browse_Activity browser_activity, HashMap<String, ArrayList<PostDetail>> post_details) {

        result=post_details;
        context = browser_activity;
        thumbValueAlreadyset = new int[result.size()];



        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView text_view;

        ImageView image;

        EditText edit_text;

        Button submit ;

        TextView connectionText;

        TextView total_unlikes;

        ImageButton ThumbDownButton;

        TextView   PostHeading;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {



        View rowView;
        rowView = inflater.inflate(R.layout.browse_row_layout, null);
        final Holder holder = getHolder(rowView);//new Holder();


            ///////////
            final ArrayList<String> keys = new ArrayList<String>(result.keySet());

            String key = keys.get(position);
            Post_Details = result.get(key);
            String post_message1 =  Post_Details .get(0).post_Detail;
            holder.text_view.setText(post_message1);

            String image = UrlHelper.getDecodedUrl(Post_Details.get(0).post_image);

            LinearLayout linear_layout = (LinearLayout) rowView.findViewById(R.id.r2);


            for (int i = 0; i < Post_Details .size(); i++) {


                String infringingUserPic = UrlHelper.getDecodedUrl(Post_Details.get(i).infringing_user_profile_pic);


                String user_comment = Post_Details.get(i).infringing_user_name + " : " + Post_Details.get(i).comment;

                final TextView rowTextView = getRowTextView(user_comment , i);
                ImageView image_view = getImageView();

                final RelativeLayout relative_layout = new RelativeLayout(context);
                linear_layout.addView(relative_layout);
                relative_layout.addView(rowTextView,0);
                relative_layout.addView(image_view,1);


                new ImageSelectorAsyncTask(image_view, holder.text_view).execute(infringingUserPic);

            }


            new ImageSelectorAsyncTask(holder.image, holder.text_view).execute(image);




            holder.submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = holder.edit_text.getText().toString();
                    String serverURL = null;
                    try {
                        serverURL = "http://169.254.68.212:8080/Muaavin-Web/rest/FeedBack/Add_FeedBack?user_id=" + AesEncryption.encrypt(FacebookLoginActivity.user.id) + "&post_id=" + /*Post_Details.get(0).post_id*/AesEncryption.encrypt(result.get(keys.get(position)).get(0).post_id )+ "&comment=" +AesEncryption.encrypt( comment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new WebHttpGetReq(context, -1, holder.text_view, 5).execute(serverURL);
                    Toast.makeText(context, "Feedback Sent", Toast.LENGTH_LONG).show();

                }
            });

            holder.PostHeading.setOnClickListener(new View.OnClickListener() {
                @Override
                 public void onClick(View v) {

                UrlHelper.showDataOnBrowser(context , result.get(keys.get(position)).get(0).PostUrl);
                }
            });


            holder.total_unlikes.setText(String.valueOf(result.get(keys.get(position)).get(0).unlike_value));
            holder.ThumbDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {

                String serverURL = null;
                try {
                    serverURL = "http://169.254.68.212:8080/Muaavin-Web/rest/ThumbsDown/Add_ThumbsDown?user_id="+ AesEncryption.encrypt(FacebookLoginActivity.user.id)+"&post_id="+AesEncryption.encrypt(result.get(keys.get(position)).get(0).post_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new WebHttpGetReq(context, 3,holder.total_unlikes,result.get(keys.get(position)).get(0).unlike_value).execute(serverURL);



            }
        });

        return rowView;


    }

    public Holder getHolder(View rowView )
    {
        Holder holder = new Holder();
        holder.text_view=(TextView) rowView.findViewById(R.id.Textbox1);
        holder.image=(ImageView) rowView.findViewById(R.id.Image_view);
        holder.edit_text = (EditText) rowView.findViewById(R.id.edit_text1);
        holder.submit = (Button)rowView.findViewById(R.id.submit);
        holder.connectionText =(TextView)rowView.findViewById(R.id.Textbox2);
        holder.total_unlikes = (TextView)rowView.findViewById(R.id.total_unlikes);
        holder.ThumbDownButton = (ImageButton)rowView.findViewById(R.id.image_button);
        holder.PostHeading = (TextView)rowView.findViewById(R.id.Textbox2);

        return holder;
    }

    /// Get Text View
    public TextView getRowTextView( String text , int id)
    {
        TextView rowTextView = new TextView(context);
        rowTextView.setId(id);
        rowTextView.setText(text);
        rowTextView.setTextSize(20);
        rowTextView.setLayoutParams(getLinearLayoutParams());
        rowTextView.setBackgroundColor(Color.parseColor("#dfe3ee"));

        return rowTextView;

    }

    // Get Layout  for Text View
    public LinearLayout.LayoutParams getLinearLayoutParams()
    {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(50, 0, 20, 20);

        return params;

    }

    public ImageView getImageView()
    {

        ImageView image_view = new ImageView(context);
        image_view.setLayoutParams(getRelativeLayoutParams());

        return image_view;

    }

    // Get Layout for Image View
    public RelativeLayout.LayoutParams getRelativeLayoutParams()
    {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        return params;

    }

}
