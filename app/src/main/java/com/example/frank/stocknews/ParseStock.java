package com.example.frank.stocknews;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Frank on 12/27/2016.
 */

public class ParseStock {
    private static final String TAG = "ParseFeed";
    ArrayList<FeedEntry> articles;

    //Constructor
    public ParseStock(){
        this.articles = new ArrayList<FeedEntry>();
    }

    public ArrayList<FeedEntry> getStocks(){
        return this.articles;
    }


    public boolean parse(String XMLData){

        boolean status = true;
        FeedEntry currentRecord = null;
        boolean inEntry = false; //To make sure in entry
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();//used to make an instance of xmlPullParser
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();//create instance of xmlPullParser
            xpp.setInput( new StringReader(XMLData) );//pass method argument as input

            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if("item".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentRecord = new FeedEntry();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("item".equalsIgnoreCase(tagName)){
                                inEntry = false;
                                articles.add(currentRecord);
                            }else if("title".equalsIgnoreCase(tagName))
                                currentRecord.setTitle(textValue);
                            else if("link".equalsIgnoreCase(tagName))
                                currentRecord.setLink(textValue);
                          //  else if("item".equalsIgnoreCase(tagName))
                        //        currentRecord.setItem(textValue);
                        }
                        break;
                    default://finished
                }
                eventType = xpp.next();
            }
        }
        catch(XmlPullParserException e) {
            status = false;
            Log.e(TAG, "parseStock: " + e.getMessage() );
        }catch(IOException e){
            status = false;
            Log.e(TAG, "parseStock: IOEXCEPTION" + e.getMessage() );
        }
        return status;
    }//Populates 'articles' with FeedEntry objects for each article


}
