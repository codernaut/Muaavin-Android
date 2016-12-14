package com.cfp.muaavin.facebook;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfp.muaavin.ui.Post_ListView;
import com.cfp.muaavin.ui.R;
import com.cfp.muaavin.ui.Users_ListView;
import com.cfp.muaavin.web.DialogBox;
import com.cfp.muaavin.web.User;
import com.cfp.muaavin.web.ImageSelectorAsyncTask;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 *
 */
public class Users_CustomAdapter extends BaseAdapter {


    ArrayList<User> result;
    ArrayList<Post> User_selective_posts = new ArrayList<Post>();
    ArrayList<Post> User_posts;

    Context context;
    private static LayoutInflater inflater=null;
    public String user_signed_inID;

    public Users_CustomAdapter(Context users_viewActivity,  ArrayList<Post> user_posts, String user_signed_id, ArrayList<User> unique_users) {

        result=unique_users;
        context = users_viewActivity;
        user_signed_inID =  user_signed_id;
        User_posts = user_posts;


        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() { return result.size(); }

    @Override
    public Object getItem(int position) { return position; }

    @Override
    public long getItemId(int position) { return position; }

    public class Holder
    {
        TextView tv1;
        ImageView img;
        Button userProfile;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = inflater.inflate(R.layout.user_row_layout, null);
        final Holder holder= getHolder(rowView);
        holder.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showUserProfileOnBrowser(result.get(position).profile_url);

            }
        });

        holder.tv1.setText(" "+ result.get(position).name);
        new ImageSelectorAsyncTask(holder.img, holder.tv1).execute(result.get(position).profile_pic);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FacebookUtil.ReportPostDetail.infringing_user_id = result.get(position).id;
                FacebookUtil.ReportPostDetail.infringing_user_name = result.get(position).name;
                FacebookUtil.ReportPostDetail.infringing_user_profile_pic = result.get(position).profile_pic;
                getDecodedImage();
                User_selective_posts = getSelectivePosts(FacebookUtil.ReportPostDetail.infringing_user_id, User_posts );

                boolean ClipBoardOption = false;
                Intent intent = new Intent(context, Post_ListView.class);
                intent.putExtra("user_posts",User_selective_posts);  //User_id
                intent.putExtra("User_id", user_signed_inID);
                intent.putExtra("ClipBoardOption", ClipBoardOption);
                context.startActivity(intent);

            }
        });
        return rowView;
    }


    public static ArrayList<Post> getSelectivePosts(String Friend_id ,ArrayList<Post> User_Posts  )
    {

        ArrayList<Post> selective_posts = new ArrayList<Post>();
        ArrayList<Comment> comments = new ArrayList<Comment>();
        ArrayList<Comment> replies = new ArrayList<Comment>();
        ArrayList<Comment> commentsArr = new ArrayList<Comment>();
        ArrayList<Post> Posts = new ArrayList<Post>();
        ArrayList<String> PostIDs = new ArrayList<String>();

        for(int i =0 ; i < User_Posts.size(); i++)
        {
            comments = User_Posts.get(i).Comments;
            for(int j = 0 ; j < comments.size(); j++)
            {
                Comment comment = comments.get(j);
                if(Friend_id.equals(comment.user_id))
                {
                    commentsArr.add(comment);
                    if(!PostIDs.contains(User_Posts.get(i).id))
                    {
                        PostIDs.add(User_Posts.get(i).id);
                        Posts.add(User_Posts.get(i));
                    }
                }
                replies =  comments.get(j).replies;
                for(int index = 0 ; index < replies.size(); index++)
                {
                    if(Friend_id.equals(replies.get(index).user_id))
                    {
                        commentsArr.add(replies.get(index));
                        if(!PostIDs.contains(User_Posts.get(i).id))
                        {
                            PostIDs.add(User_Posts.get(i).id);
                            Posts.add(User_Posts.get(i));
                        }
                    }
                }

            }
        }

        for(int i = 0 ; i < Posts.size(); i++)
        {

            int check = 0;
            Post post = new Post();
            for(int j = 0 ; j < commentsArr.size(); j++)
            {

                if( Posts.get(i).id.equals(commentsArr.get(j).post_id))
                {
                    check = 1;
                    //post = Posts.get(i);
                    post.id = Posts.get(i).id;
                    post.message = Posts.get(i).message;
                    post.image = Posts.get(i).image;
                    post.post_url = Posts.get(i).post_url;

                    Comment comment1 = commentsArr.get(j);

                    post.Comments.add(comment1);
                }
            }
            if(check == 1)
            selective_posts.add(post);
        }
        return selective_posts;
    }

    public  void showUserProfileOnBrowser(String url)
    {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(facebookIntent);

    }

    public String getDecodedImage()
    {

        try {

            FacebookUtil.ReportPostDetail.infringing_user_profile_pic = URLEncoder.encode(FacebookUtil.ReportPostDetail.infringing_user_profile_pic, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return FacebookUtil.ReportPostDetail.infringing_user_profile_pic;
    }

    public Holder getHolder( View rowView)
    {

        Holder holder=new Holder();
        holder.tv1=(TextView) rowView.findViewById(R.id.Textbox1);
        holder.img=(ImageView) rowView.findViewById(R.id.Image_view);
        holder.userProfile = (Button)rowView.findViewById(R.id.UserProfile);
        return holder;

    }

}
