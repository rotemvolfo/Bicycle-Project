package com.example.inbar.bicycle_client;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class TipsManager {

    private JSONObject jsonObject = new JSONObject();
    private String url;

    private String httpPost(String myUrl) throws IOException, JSONException {
        String result = "";
        StringBuffer response = new StringBuffer();

        URL url = new URL(myUrl);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // 2. build JSON object
        JSONObject jsonObject = this.jsonObject;

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URL
        conn.connect();

        int status = conn.getResponseCode();
        if (status != 200) {
            throw new IOException("Post failed with error code " + status);
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }

        // 5. return response message
        return response.toString();

    }


    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return httpPost(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "Error!";
                }
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
           // tvResult.setText(result);
        }
    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }


    public String addNewTipByUser(String userName, String content, String lat, String lng, String title) throws JSONException, ExecutionException, InterruptedException {

        jsonObject.accumulate("latitude", lat.replaceAll("\\.",""));
        jsonObject.accumulate("longitude",  lng.replaceAll("\\.",""));
        jsonObject.accumulate("userName",  userName);
        jsonObject.accumulate("text", content);
        jsonObject.accumulate("title", title);
        url = "https://us-central1-myproj-a99c9.cloudfunctions.net/addReviewsAboutThePlace";

        return executeHTTPRequest();
    }


    public String getAllTipsToSpecificPlace(String lat, String lng) throws ExecutionException, InterruptedException, JSONException {

        jsonObject.accumulate("latitude", lat.replaceAll("\\.",""));
        jsonObject.accumulate("longitude",  lng.replaceAll("\\.",""));
        url = "https://us-central1-myproj-a99c9.cloudfunctions.net/getReviews";

        return executeHTTPRequest();
    }

    private String executeHTTPRequest() throws ExecutionException, InterruptedException {

        String response =  new HTTPAsyncTask().execute().get();
        removeAllFromJsonObject();
        return response;
    }

    private void removeAllFromJsonObject(){

        while(jsonObject.length()>0)
            jsonObject.remove(jsonObject.keys().next());
    }
}