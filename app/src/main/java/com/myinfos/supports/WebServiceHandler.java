package com.myinfos.supports;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by admin on 13/02/2016.
 */
public class WebServiceHandler {

    public static String executePost(String requestUrl, String json) {
        StringBuffer response=null;
        String r;
        try {
            URL url = new URL(requestUrl);
            Log.e("Request JSON: ", json);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn=setRequestProperties(conn);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json);
            conn.connect();
            wr.flush();

            int httpStatus = conn.getResponseCode();
            Log.e("httpStatus: ", "" + httpStatus);

            if (httpStatus == 200) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line="";
                response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    //response.append('\r');
                }
                wr.close();
                rd.close();
                conn.disconnect();
                Log.e("Response: ", response.toString());


            }
        } catch (MalformedURLException me) {

        } catch (IOException ioe) {
            Log.e("IOE: ",ioe.getMessage());
           // response.append(ioe.getMessage().toString());
        }
        return response.toString();
    }


    private static HttpURLConnection setRequestProperties(HttpURLConnection httpURLConnection) throws ProtocolException {

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-type", "application/json");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

        return httpURLConnection;
    }
}
