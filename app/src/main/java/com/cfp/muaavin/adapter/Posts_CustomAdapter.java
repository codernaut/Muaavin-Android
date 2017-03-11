package com.cfp.muaavin.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.helper.UrlHelper;
import com.cfp.muaavin.ui.Post_ListView;
import com.cfp.muaavin.ui.R;
import com.cfp.muaavin.web.DialogBox;
import com.cfp.muaavin.web.ImageSelectorAsyncTask;

import java.util.ArrayList;

/**
 *
 */
public class Posts_CustomAdapter extends BaseAdapter {


    ArrayList<Post> result;


    Context context;
    private static LayoutInflater inflater=null;
    //public String[] group = {"A","B","C"};
    String[] group = new String[]{"Sexual harassment", "Incitement to violence","Trans rights"};
    public String user_signed_inID;
    boolean ClipBoardOption ;
    boolean IsReportedPost = false;
    boolean GroupPostOption ;



    public Posts_CustomAdapter(Activity PostList_viewActivity, ArrayList<Post> selective_posts, String user_signed_id , boolean ClipBoardOption, boolean groupPostOption) {

        result = selective_posts;
        context = PostList_viewActivity;
        user_signed_inID =  user_signed_id;
        this.ClipBoardOption = ClipBoardOption;
        GroupPostOption = groupPostOption;
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
        TextView tv1;

        ImageView img;

        TextView tv3;

        TextView postTextView;

        TextView PostDetail;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = inflater.inflate(R.layout.post_layout, null);
        LinearLayout linear_layout = (LinearLayout) rowView.findViewById(R.id.r2);
        final Holder holder = getHolder( rowView );

        holder.postTextView.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {

                UrlHelper.showDataOnBrowser(context,result.get(position).post_url);
            }
        });

        holder.tv1.setText(" "+ result.get(position).message);

        for (int i = 0; i < result.get(position).Comments.size(); i++) {

        final TextView rowTextView =  getRowTextView( result.get(position).Comments.get(i).message);
        final int num = i;
        rowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FacebookUtil.ReportPostDetail.setPostInformation(result.get(position).id,removeHash(removeHash(result.get(position).message,"#", " "),"'","''"), UrlHelper.getEncodedUrl(result.get(position).image),result.get(position).post_url);
                FacebookUtil.ReportPostDetail.setCommentInformation(result.get(position).Comments.get(num).comment_id,removeHash(removeHash(result.get(position).Comments.get(num).message, "#", " "),"'","''"), result.get(position).Comments.get(num).parent_comment_id,"");

                if((ClipBoardOption) ||(GroupPostOption)) getInfringingUserDetail(position,num,-1,IsReportedPost);

                DialogBox.ShowDialogBOx3(context, "Select Group ", group, 0, user_signed_inID,null,null,false);
                }
            });

            linear_layout.addView(rowTextView);
            if((ClipBoardOption) ||(GroupPostOption))
            {
                for(int reply_index = 0 ; reply_index < result.get(position).Comments.get(i).replies.size(); reply_index++)
                {
                    final int  index = reply_index;
                    final TextView replyTextView =  getRowTextView( result.get(position).Comments.get(num).replies.get(index).message);
                    replyTextView.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {

                            FacebookUtil.ReportPostDetail.setPostInformation(result.get(position).id,removeHash(removeHash(result.get(position).message,"#", " "),"'","''"), UrlHelper.getEncodedUrl(result.get(position).image),result.get(position).post_url);
                            FacebookUtil.ReportPostDetail.setCommentInformation(result.get(position).Comments.get(num).replies.get(index).comment_id,removeHash(removeHash(result.get(position).Comments.get(num).replies.get(index).message, "#", " "),"'","''"), result.get(position).Comments.get(num).replies.get(index).parent_comment_id,"");

                            getInfringingUserDetail(position,num,index,IsReportedPost);

                            DialogBox.ShowDialogBOx3(context, "Select Group ", group, 0, user_signed_inID,null,null,false);
                        }
                    });
                    linear_layout.addView(replyTextView);
                }
            }
        }

        if((ClipBoardOption) ||(GroupPostOption))
        holder.PostDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsReportedPost = true;
                FacebookUtil.ReportPostDetail.setPostInformation(result.get(position).id,removeHash(removeHash(result.get(position).message,"#", " "),"'","''"), UrlHelper.getEncodedUrl(result.get(position).image),result.get(position).post_url);
                FacebookUtil.ReportPostDetail.setCommentInformation(" ",removeHash(removeHash(result.get(position).message, "#", " "),"'","''"), " ","");
                getInfringingUserDetail(position,0,-1,IsReportedPost);
                IsReportedPost = false;
                DialogBox.ShowDialogBOx3(context, "Select Group ", group, 0, user_signed_inID,null,null,false);
            }
        });

        new ImageSelectorAsyncTask(holder.img, holder.tv1).execute(result.get(position).image);
        return rowView;
    }


    public TextView getRowTextView( String text)
    {

        TextView rowTextView = new TextView(context);
        rowTextView.setLayoutParams(getLayoutParams());
        rowTextView.setText(text);
        rowTextView.setTextSize(20);
        rowTextView.setBackgroundColor(Color.parseColor("#dfe3ee"));

        return rowTextView;
    }

    public LayoutParams getLayoutParams()
    {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.setMargins(20, 20, 20, 20);
        return params;

    }

    public Holder getHolder(View rowView )
    {
        Holder holder = new Holder();
        holder.tv1=(TextView) rowView.findViewById(R.id.Textbox1);
        holder.img=(ImageView) rowView.findViewById(R.id.Image_view);
        holder.tv3 = (TextView)rowView.findViewById(R.id.Textbox3);
        holder.postTextView = (TextView)rowView.findViewById(R.id.Textbox2);
        holder.PostDetail = (TextView)rowView.findViewById(R.id.Textbox1);
        return holder;

    }

    public String removeHash(String text, String replacedValue, String replaceWith )
    {

        if (text.contains(replacedValue)) {
            text = text.replaceAll(replacedValue, replaceWith);
        }
        return text;
    }

    public void getInfringingUserDetail(int position,int commentIndex,int replyIndex, boolean IsReportedPost)
    {
        if(IsReportedPost)
        {
            FacebookUtil.ReportPostDetail.infringing_user_name = result.get(position).PostOwner.name;
            FacebookUtil.ReportPostDetail.infringing_user_id = result.get(position).PostOwner.id;
            FacebookUtil.ReportPostDetail.infringing_user_profile_pic = result.get(position).PostOwner.profile_pic;
            FacebookUtil.ReportPostDetail.infringing_user_profile_pic = UrlHelper.getEncodedUrl(FacebookUtil.ReportPostDetail.infringing_user_profile_pic);
            FacebookUtil.ReportPostDetail.user_state = "UnBlocked";

        }

        else if(replyIndex > -1)
        {
            FacebookUtil.ReportPostDetail.infringing_user_name = result.get(position).Comments.get(commentIndex).replies.get(replyIndex).name;
            FacebookUtil.ReportPostDetail.infringing_user_id = result.get(position).Comments.get(commentIndex).replies.get(replyIndex).user_id;
            FacebookUtil.ReportPostDetail.infringing_user_profile_pic = result.get(position).Comments.get(commentIndex).replies.get(replyIndex).profilePic;
            FacebookUtil.ReportPostDetail.infringing_user_profile_pic = UrlHelper.getEncodedUrl(FacebookUtil.ReportPostDetail.infringing_user_profile_pic);
            FacebookUtil.ReportPostDetail.user_state = "UnBlocked";
        }
        else
        {
            FacebookUtil.ReportPostDetail.infringing_user_name = result.get(position).Comments.get(commentIndex).name;//MenuActivity.users.get(position).name;
            FacebookUtil.ReportPostDetail.infringing_user_id = result.get(position).Comments.get(commentIndex).user_id;
            FacebookUtil.ReportPostDetail.infringing_user_profile_pic = result.get(position).Comments.get(commentIndex).profilePic;
            FacebookUtil.ReportPostDetail.infringing_user_profile_pic = UrlHelper.getEncodedUrl(FacebookUtil.ReportPostDetail.infringing_user_profile_pic);
            FacebookUtil.ReportPostDetail.user_state = "UnBlocked";
        }
    }


}
