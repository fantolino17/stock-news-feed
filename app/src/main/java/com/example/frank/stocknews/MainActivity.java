package com.example.frank.stocknews;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText tick;
    private ListView listStocks;
    private Button button;
    private Button refreshButton;
    private String URLfeed = "https://feeds.finance.yahoo.com/rss/2.0/headline?s=%s&region=US&lang=en-US";
    private String inputTicker = "appl";
    private Uri passedUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listStocks = (ListView) findViewById(R.id.XMLlistView);
        if(savedInstanceState != null){
            inputTicker = savedInstanceState.getString(inputTicker);
        }
        downloadURL(URLfeed,inputTicker);
        tick = (EditText) findViewById(R.id.ticker);
    }


    protected void onSaveInstanceState(Bundle outState){
        outState.putString(inputTicker,inputTicker);

    }



    private void downloadURL(String feedURL, String tick){
        DownloadData downLoadData = new DownloadData();
        downLoadData.execute(String.format(feedURL,tick));
    }

    //'Link Button' function openLink to be called on click
    public void openLink(View v){
        button = (Button) v.findViewById(R.id.Link);
        Uri uri = Uri.parse(button.getTag().toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void refresh(View v){
        Log.d(TAG, "refresh: button clicked");
        inputTicker = tick.getText().toString();
    //    String newURL = String.format(URLfeed,inputTicker);
        downloadURL(URLfeed,inputTicker);

    }



    private class DownloadData extends AsyncTask<String, Void, String>{ //URL,progress bar,return type

        private static final String TAG = "DownloadData";

        @Override//Parses XML passed to it, creates ArrayAdapter (FeedAdapter), so we can set adapter to listStocks listView
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ParseStock parseStock = new ParseStock();
            parseStock.parse(s);
            Log.d(TAG, "onPostExecute: " + inputTicker);
            Log.d(TAG, "onPostExecute: " + parseStock.getStocks().toString());

            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.list_item, parseStock.getStocks());

            listStocks.setAdapter(feedAdapter);
        }

        @Override
        protected String doInBackground(String... params) {//Calls downloadXML so it can be run in background, returns XML as String
            String rssFeed = downloadXML(params[0]);
            if(rssFeed == null)
                Log.d(TAG, "doInBackground: Error Downloading");
            return rssFeed;
        }

        private String downloadXML(String urlPath){
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(urlPath);//Get URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();//Open connection (gives inputStream), cast as HTTPURLConnection
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));//create bufferedReader

                int charsRead;
                char[] inputBuffer = new char[500];
                while(true){
                    charsRead = reader.read(inputBuffer);
                    if(charsRead < 0){
                        break;
                    }
                    if(charsRead > 0){
                        result.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                return result.toString();

            }
            catch(MalformedURLException e){
                Log.d(TAG, "downloadXML: Malformed URLException " + e.getMessage());
            }
            catch(IOException e){
                Log.d(TAG, "downloadXML: IOException " + e.getMessage());
            }
            return null;
        }//Gets XML from urlPath, returns a String of XML

    }


}
