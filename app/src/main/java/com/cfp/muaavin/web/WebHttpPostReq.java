package com.cfp.muaavin.web;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.cfp.muaavin.ui.R;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class WebHttpPostReq extends AsyncTask<String, Void, Void> {

    private String Content;
    public Activity activity;

    public static Activity a;
    public static Context con;

    TextView uiUpdate;
    TextView jsonParsed ;
    int sizeData = 0;
    EditText serverText;

    public static Context context ;
    private String Error = null;
    private ProgressDialog Dialog ;
    String data ="";

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public   WebHttpPostReq(Context c , Activity a)
    {

        context  = c;
        activity = a;

        Dialog = new ProgressDialog(context);
        uiUpdate = (TextView) activity.findViewById(R.id.output);
        jsonParsed = (TextView) activity.findViewById(R.id.jsonParsed);

        serverText = (EditText) activity.findViewById(R.id.serverText);

    }

    @Override
    protected void onPreExecute() {
        // NOTE: You can call UI Element here.

        //Start Progress Dialog (Message)

        Dialog.setMessage("Please wait..");
        Dialog.show();

        try{
            // Set Request parameter
            data +="&" + URLEncoder.encode("data", "UTF-8") + "="+serverText.getText();

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    protected Void doInBackground(String... params) {
        // TODO Auto-generated method stub
        BufferedReader reader=null;
        HttpClient httpclient = new DefaultHttpClient();
        // HttpPost httppost = new HttpPost("http://192.168.1.5:8080/DynamiWebProj/rest/products/Insert_Product?");
        HttpPost httppost = new HttpPost();

        // Send data
        try
        {
            JSONObject jsonobj = new JSONObject();

            jsonobj.put("name", "Sony");
            jsonobj.put("category", "ppp");



            List<org.apache.http.NameValuePair> nameValuePairs = new ArrayList<org.apache.http.NameValuePair>();

            /////////
            nameValuePairs.add(new BasicNameValuePair("name", "uvx"));
            nameValuePairs.add(new BasicNameValuePair("category", "xyz"));


            // Use UrlEncodedFormEntity to send in proper format which we need
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            InputStream inputStream = response.getEntity().getContent();


            StringWriter stringWriter = new StringWriter();
            IOUtils.copy(inputStream, stringWriter, "UTF-8");

            String text = stringWriter.toString();

            Log.e("response", "response -----" + text);



            // Append Server Response To Content String
            // Content = sb.toString();
            Content = text;
        }

        catch(Exception ex)
        {
            Error = ex.getMessage();
        }


        return null;
    }


    @Override
    protected void onPostExecute(Void unused) {
        // NOTE: You can call UI Element here.

        // Close progress dialog
        Dialog.dismiss();
        jsonParsed.setText("Content :"+Content);

    }



}
