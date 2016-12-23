    package com.cfp.muaavin.web;

    import android.app.Activity;
    import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
    import android.content.Intent;

    import com.cfp.muaavin.facebook.AsyncResponsePosts;
    import com.cfp.muaavin.facebook.FacebookUtil;
    import com.cfp.muaavin.facebook.Friend;
    import com.cfp.muaavin.facebook.LoadPostsAyscncTask;
    import com.cfp.muaavin.facebook.Post;
    import com.cfp.muaavin.ui.BrowsePost_ListView;
    import com.cfp.muaavin.ui.Browse_Activity;
    import com.cfp.muaavin.ui.MenuActivity;
    import com.cfp.muaavin.ui.Post_ListView;
    import com.cfp.muaavin.ui.WebServiceActivity;

    import java.util.ArrayList;

    public class DialogBox {


    public static void ShowDialogBOx3( final Context context , String str ,final String[] group, final int option , final String user_id)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str);

        builder.setItems(group, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String group_name = group[which];
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

        public static  void showQuestionDialog(final Context context,final AsyncResponsePosts delegate, final String user_id, final  String post_id, final String link)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(" Link");

            builder.setMessage("Here is a link , do you want to report it ?" + "\n" + link);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    /*MenuActivity.*/
                    boolean isClipboardData = true;
                    FacebookUtil.users = new ArrayList<User>();
                    FacebookUtil.friendsIds = new ArrayList<String>();
                    LoadPostsAyscncTask.count = 0;
                    new LoadPostsAyscncTask(context, delegate, user_id, isClipboardData, post_id, new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
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

}
