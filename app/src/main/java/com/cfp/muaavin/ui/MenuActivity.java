package com.cfp.muaavin.ui;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.UserInterface;
import com.cfp.muaavin.helper.ClipBoardHelper;
import com.cfp.muaavin.helper.DataLoaderHelper;
import com.cfp.muaavin.twitter.TwitterUtil;
import com.cfp.muaavin.facebook.User;
import com.cfp.muaavin.web.DialogBox;
import com.cfp.muaavin.web.FriendManagement;
import com.cfp.muaavin.web.WebHttpGetReq;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import java.util.ArrayList;
import io.fabric.sdk.android.Fabric;
import static com.cfp.muaavin.facebook.FacebookUtil.clearFacebookData;
import static com.cfp.muaavin.twitter.TwitterUtil.clearTwitterData;
import static com.cfp.muaavin.ui.TwitterLoginActivity.session;
import static com.cfp.muaavin.web.DialogBox.promptInputDialog;



public  class MenuActivity extends AppCompatActivity implements  UserInterface , UiUpdate {

    public static String[] ReportPostOption  =  {"Report Posts","Report Tweets","Report Group Posts" };
    static String[] HighlightUserOption  =  {"Highlight Facebook Users","Highlight Twitter Users"};
    static String[] BrowseOption  =  {"Browse Reports","Browse Posts" };
    FriendManagement  friend_management;
    Context contex;
    String user_id ;
    public static ArrayList<User> users = new ArrayList<User>() ;
    DataLoaderHelper controller;
    BottomNavigationView bottomNavigationBar;
    int selectedItem;
    private static final String TWITTER_KEY = "qWbMCnZUcB9hOliWDG6IOtkNP";
    private static final String TWITTER_SECRET = "H4KIPod4y561OXJ7u8Cd4EuGCtIofAi0HhR2hW80Ng84JgQaQ3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(MenuActivity.this, new Twitter(authConfig));
        ///////////////////
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //////////////////
        setContentView(R.layout.activity_menu);

        //Fragment Mainfragment = new Fragment();
        //getFragmentManager().beginTransaction().replace(R.id.fragment_container,  Mainfragment).commit();

        bottomNavigationBar = (BottomNavigationView)findViewById(R.id.bottom_navigation_view);
        bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Report:

                        //if(selectedItem != item.getItemId())
                        {
                            selectedItem = item.getItemId();
                            //removeFragmentsFromStack();
                            DialogBox.SelectReportOption(contex,"Select Option",ReportPostOption,1,user_id,MenuActivity.this,MenuActivity.this,false);
                        }  break;

                    case R.id.HighlightUsers:

                        //if(selectedItem != item.getItemId())
                        {   selectedItem = item.getItemId();
                            //removeFragmentsFromStack();
                            DialogBox.SelectReportOption(contex,"Select Option",HighlightUserOption,1,user_id,MenuActivity.this,MenuActivity.this,false);
                        }  break;

                    case R.id.Browse:
                        //if(selectedItem != item.getItemId())
                        {
                            selectedItem = item.getItemId();
                            //removeFragmentsFromStack();
                            DialogBox.SelectReportOption(contex,"Select Option",BrowseOption,1,user_id,MenuActivity.this,MenuActivity.this,false);
                        }   break;
                }
                return true;

            }
        });

        contex = this;
        clearFacebookData();
        clearTwitterData();
        controller = new DataLoaderHelper(contex,this,MenuActivity.this);

        friend_management = new FriendManagement();
        user_id = getIntent().getStringExtra("User_signedID");
        //192.168.1.5  13.76.175.64 //192.168.8.101
        String serverURL = null;
        serverURL = "http://13.76.175.64:8080/Muaavin-Web/rest/Users/getBlockedUsers?";
        new WebHttpGetReq(contex,MenuActivity.this, 9,null, this).execute(serverURL);
        //new FriendsLoadAsyncTask(contex).execute();
        //ClipBoardHelper.getPostFromClipBoard(contex , this, user_id );
    }

    public void Reporting(View v)
    {
        if(User.user_authentication == false) { return; }
        friend_management.reportFriends(contex,MenuActivity.this);
    }

    public void reportTwitter(View view)
    {
        if(User.user_authentication == false) { return; }
        if(session == null)
        {
            Intent intent = new Intent(MenuActivity.this, TwitterLoginActivity.class);
            intent.putExtra("option", "LoadTweets"); startActivity(intent);
        }
        else {  controller.loadTwitterData( "LoadTweets"); }
    }

    public void Highlights(View v)
    {
        if(User.user_authentication == false) { return; }
        friend_management.Highlights(contex,this,MenuActivity.this);
    }

    public void highlightTwitterUsers(View view)
    {
        if(User.user_authentication == false) { return; }
        if(session == null)
        {
            Intent intent = new Intent(MenuActivity.this, TwitterLoginActivity.class);
            intent.putExtra("option", "LoadFollowers"); startActivity(intent);
        }
        else { controller.loadTwitterData( "LoadFollowers"); }
    }

    public void Browse(View v)
    {
        if(User.user_authentication == false) { return; }
        friend_management.Browse(contex,MenuActivity.this);
    }

    public void BrowsePost(View v)
    {
        if(User.user_authentication == false) { return; }
        friend_management.BrowsePost(contex, user_id,MenuActivity.this);
    }

    public  void ReportGroupPosts(View view)
    {
        promptInputDialog(contex,MenuActivity.this);
    }

    public   void LogOut()
    {
        LoginManager.getInstance().logOut(); // Logout from Facebook
        //disconnectFromFacebook();
        CookieSyncManager.createInstance(this);// Logout from Twitter
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        Twitter.getSessionManager().clearActiveSession();
        session = null;   Twitter.logOut();
    }


    @Override
    public void getReportedFriends(ArrayList<String> Friends, String dataType) {

    }

    @Override
    public void getBlockedUsers(ArrayList<String> FacebookBlockedUserIds, ArrayList<String> TwitterBlockedUserIds) {

        FacebookUtil.BlockedUsersIds = FacebookBlockedUserIds;
        TwitterUtil.BlockedUserIds = TwitterBlockedUserIds;
        if(FacebookBlockedUserIds.contains(user_id)) { User.user_authentication = false;  } else { User.user_authentication = true; }
        ClipBoardHelper.getPostFromClipBoard(contex , user_id,MenuActivity.this );
    }

    @Override
    public void updateUi(ArrayList<User> Users, ArrayList<String> InfringingUserIds,String dataType) {
        WebServiceActivity frag = new WebServiceActivity(); Bundle args = new Bundle();
        args.putSerializable("InfringingUsers", Users); args.putSerializable("InfringingUsersIds", InfringingUserIds); args.putString("DataType", dataType);
        frag.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_quote:
                // TODO put your code here to respond to the button tap
                LogOut();
                Intent intent = new Intent(MenuActivity.this,FacebookLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            case android.R.id.home:
                if (getFragmentManager().getBackStackEntryCount() > 0) { getFragmentManager().popBackStack(); }
                else super.onBackPressed();
            default:  return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0)
        {
            getFragmentManager().popBackStack();
        }
        else
        {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startMain);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragment = getFragmentManager();
        if (fragment != null) {
            fragment.findFragmentByTag("TwitterFragment").onActivityResult(requestCode, resultCode, data);
        }
        else Log.d("Twitter", "fragment is null");
    }

    public void removeFragmentsFromStack()
    {
        for(int i = 0 ; i < getFragmentManager().getBackStackEntryCount() ; i++ )
        {
            getFragmentManager().popBackStack();
        }
    }


}
