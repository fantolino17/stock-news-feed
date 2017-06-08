package com.example.frank.stocknews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Frank on 12/27/2016.
 */

public class FeedAdapter extends ArrayAdapter {

    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<FeedEntry> stocks;

    public FeedAdapter(Context context, int resource, List<FeedEntry> stocks){
        super(context,resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.stocks = stocks;
    }

    public int getCount(){
        return stocks.size();
    }

    public View getView(int position, View convertView, ViewGroup parent){

        FeedEntry currentArticle = stocks.get(position);
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.articleTitle.setText(currentArticle.getTitle());
        viewHolder.articleLink.setTag(currentArticle.getLink());
        //viewHolder.articleLink.setText(currentArticle.getLink());
        return convertView;
    }

    //Inner class to hold views so they can be reused
    public class ViewHolder{
        final Button articleLink;
        final TextView articleTitle;

        ViewHolder(View v){
            articleLink = (Button) v.findViewById(R.id.Link);
            articleTitle = (TextView) v.findViewById(R.id.articleTitle);
        }

    }
}
