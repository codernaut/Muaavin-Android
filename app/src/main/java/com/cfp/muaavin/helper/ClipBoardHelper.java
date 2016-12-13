package com.cfp.muaavin.helper;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.web.DialogBox;

/**
 *
 */
public class ClipBoardHelper {

    public static ClipboardManager clipboard;
    public static void getPostFromClipBoard(Context context, AsyncResponsePosts Postdelegate , String user_id)
    {

        clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("url", "https://m.facebook.com/story.php?story_fbid=1341003545930283&substory_index=0&id=487101601320486");
        clipboard.setPrimaryClip(clip);

        String ClipboardData = "";///
        if(clipboard.hasPrimaryClip())
        {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            ClipboardData = String.valueOf( item.getText());

            if(ClipboardData!=null)
            {
                String post_id =  UrlHelper.getQueryFieldsFromURL( ClipboardData);
                DialogBox.showQuestionDialog(context, Postdelegate, user_id, post_id, ClipboardData);
            }
        }
    }
}
