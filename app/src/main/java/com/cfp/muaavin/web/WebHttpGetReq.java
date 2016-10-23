package com.cfp.muaavin.web;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import com.cfp.muaavin.ui.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class WebHttpGetReq extends AsyncTask<String, Void, Void> {


    private String Content;
    public Activity activity;

    public static Activity a;


    TextView uiUpdate;
    TextView jsonParsed ;
    int sizeData = 0;
    EditText serverText;

    public static Context context ;
    private String Error = null;
    private ProgressDialog Dialog ;
    String data ="";

    public   WebHttpGetReq(Context c , Activity a)
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

        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL(params[0]);

            // Send POST data request

            URLConnection conn = url.openConnection();


            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();


            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "");

            }

            // Append Server Response To Content String
            Content = sb.toString();
        }

        catch(Exception ex)
        {
            Error = ex.getMessage();
        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {}
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void unused) {

        Dialog.dismiss();

        if (Error != null) {

            uiUpdate.setText("Output : "+Error);

        } else {

            // Show Response Json On Screen (activity)
            uiUpdate.setText( Content );

            /****************** Start Parse Response JSON Data *************/

            String OutputData = "";
            JSONObject jsonResponse;

            try {


                jsonResponse = new JSONObject(Content);


                JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

                /// Process each JSON Node ///

                int lengthJsonArr = jsonMainNode.length();

                for(int i=0; i < lengthJsonArr; i++)
                {

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);




                }





                //jsonParsed.setText( OutputData );

            }


            catch (JSONException e) {

                e.printStackTrace();
            }

            //jsonParsed.setText(Content);

        }
    }

}




