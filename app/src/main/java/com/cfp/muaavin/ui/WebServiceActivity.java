package com.cfp.muaavin.ui;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cfp.muaavin.web.WebHttpGetReq;
import com.cfp.muaavin.web.WebHttpPostReq;


public class WebServiceActivity extends ActionBarActivity {

        public Context context ;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);
        context = this;



        final Button GetServerData = (Button) findViewById(R.id.GetServerData);

        GetServerData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                String serverURL = "http://169.254.68.212:8080/WebService/rest/posts/Insert_Post?Post_id=aslavc&Group_id=19&Group_name=abc&Profile_name=xyz&user_id=5632&Post_Det=bhjb";
                new WebHttpGetReq(context, WebServiceActivity.this).execute(serverURL);

               // String serverURL = "http://169.254.68.212:8080/WebService/rest/Query/Highlights?name=abc";
               // new WebHttpGetReq(c, WebServiceActivity.this).execute(serverURL);
            }
        });



    }




}
