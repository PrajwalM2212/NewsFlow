package com.bhavaneulergmail.newsflow.utils;

import com.bhavaneulergmail.newsflow.templates.NewsInfo;
import com.bhavaneulergmail.newsflow.templates.SourceInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JsonFetch {

    public static ArrayList<SourceInfo> getSourceInfo(String jsonResponse) {

        ArrayList<SourceInfo> sourceInfoList = new ArrayList<>();


        try {

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("sources");

            for (int i = 0; i < jsonArray.length(); i++) {
                SourceInfo sourceInfo = new SourceInfo();
                JSONObject sourceObject = jsonArray.getJSONObject(i);
                sourceInfo.sourceName = sourceObject.getString("name");
                sourceInfo.sourceCategory = sourceObject.getString("category");
                sourceInfo.sourceId = sourceObject.getString("id");
                sourceInfoList.add(sourceInfo);

            }


        } catch (JSONException e) {

        }

        return sourceInfoList;
    }

    public static ArrayList<NewsInfo> getNewsInfo(String jsonResponse) {

        ArrayList<NewsInfo> newsInfoList = new ArrayList<>();
        if (jsonResponse != null) {


            try {

                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray jsonArray = jsonObject.getJSONArray("articles");

                for (int i = 0; i < jsonArray.length(); i++) {
                    NewsInfo newsInfo = new NewsInfo();
                    JSONObject newsObject = jsonArray.getJSONObject(i);
                    newsInfo.author = newsObject.getString("author");
                    newsInfo.description = newsObject.getString("description");
                    newsInfo.publishedAt = newsObject.getString("publishedAt");
                    newsInfo.title = newsObject.getString("title");
                    newsInfo.url = newsObject.getString("url");
                    newsInfo.urlToImage = newsObject.getString("urlToImage");
                    newsInfoList.add(newsInfo);

                }

            } catch (JSONException e) {

                e.printStackTrace();
            }

        }

        return newsInfoList;


    }
}
