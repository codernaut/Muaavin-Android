    package com.cfp.muaavin.web;

    import android.app.AlertDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import com.cfp.muaavin.facebook.AsyncResponsePosts;
    import com.cfp.muaavin.facebook.FacebookUtil;
    import com.cfp.muaavin.facebook.LoadPostsAyscncTask;
    import com.cfp.muaavin.facebook.Post;
    import com.cfp.muaavin.twitter.Controller;
    import com.cfp.muaavin.twitter.TwitterUtil;
    import com.cfp.muaavin.ui.BrowsePost_ListView;
    import com.cfp.muaavin.ui.Browse_Activity;
    import com.cfp.muaavin.ui.TwitterLoginActivity;
    import com.cfp.muaavin.ui.WebServiceActivity;
    import com.twitter.sdk.android.core.models.Tweet;
    import java.util.ArrayList;
    import static com.cfp.muaavin.ui.TwitterLoginActivity.session;

    public class DialogBox {

    static String[] group  =  {"A","B","C","All"};

    public static void ShowDialogBOx3(final Context context , String str , final String[] category, final int option , final String user_id , final boolean isTwitterData)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str);

        builder.setItems(category, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String group_name = group[which];
                if(isTwitterData)
                {
                    TwitterUtil.ReportTwitterDetail.group_id = which + 1;

                    if(option == 7)
                    {
                        Intent intent = new Intent(context, WebServiceActivity.class);
                        intent.putExtra("Group_name", group_name);
                        intent.putExtra("check", option);
                        context.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(context, WebServiceActivity.class);
                        intent.putExtra("Group_name", group_name);
                        intent.putExtra("check", 5);
                        context.startActivity(intent);
                    }
                }
                else {
                    FacebookUtil.ReportPostDetail.group_id = which + 1;

                    if (option == 0) {
                        Intent intent = new Intent(context, WebServiceActivity.class);
                        intent.putExtra("Group_name", group_name);
                        intent.putExtra("check", option);
                        context.startActivity(intent);
                    } else if (option == 1) {

                        Intent intent = new Intent(context, WebServiceActivity.class);
                        intent.putExtra("Group_name", group_name);
                        intent.putExtra("check", option);
                        context.startActivity(intent);

                    } else if (option == 2) {
                        Intent intent = new Intent(context, Browse_Activity.class);
                        intent.putExtra("Group_name", group_name);
                        context.startActivity(intent);
                    } else if (option == 4) {

                        Intent intent = new Intent(context, BrowsePost_ListView.class);
                        intent.putExtra("Group_name", group_name);
                        intent.putExtra("user_id", user_id);
                        context.startActivity(intent);
                    }
                }
            }
        });
        builder.show();
    }

        public static  void showErrorDialog(Context context )
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(" Error");

            builder.setMessage("Permission Error");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    //
                }
            });
            builder.show();

        }

        public static  void showQuestionDialog(final Context context,final AsyncResponsePosts delegate, final String user_id, final  String post_id, final String link, final boolean IsFacebookPost)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(" Link");

            builder.setMessage("Here is a link , do you want to report it ?" + "\n" + link);
            builder.setPositiveButton("Show", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    if(IsFacebookPost) {
                        boolean isClipboardData = true;
                        FacebookUtil.users = new ArrayList<User>();
                        FacebookUtil.friendsIds = new ArrayList<String>();
                        LoadPostsAyscncTask.count = 0;
                        new LoadPostsAyscncTask("ReportUsers",context, delegate, user_id, isClipboardData, post_id, new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
                    }

                    else {  if(session == null)
                    {
                        Intent intent = new Intent(context, TwitterLoginActivity.class);
                        intent.putExtra("option", "Load Specific Tweet"); context.startActivity(intent);
                    }    else {Controller controller = new Controller(context); controller.loadTwitterData("Load Specific Tweet"); }}
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    boolean isClipboardData = false;
                }

            });
            builder.show();
        }

        public static  void showTweetDialog(final Context context,final Tweet tweet  )
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(" Tweet");

            builder.setMessage(tweet.text);
            builder.setPositiveButton("Report", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Group name"); final String[] group = {"A","B","C"};

                    builder.setItems(group, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TwitterUtil.ReportTwitterDetail.infringing_user_name = tweet.user.name;
                            TwitterUtil.ReportTwitterDetail.infringing_user_id= tweet.user.idStr;
                            TwitterUtil.ReportTwitterDetail.infringing_user_profile_pic = tweet.user.profileImageUrl;
                            TwitterUtil.ReportTwitterDetail.post_Detail = tweet.text;
                            TwitterUtil.ReportTwitterDetail.post_id = tweet.idStr;
                            String group_name = group[i];
                            Intent intent = new Intent(context, WebServiceActivity.class);
                            intent.putExtra("Group_name", group_name);
                            intent.putExtra("check", 5);
                            context.startActivity(intent);


                        }
                    }).show();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();

        }

}
