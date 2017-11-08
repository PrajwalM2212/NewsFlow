package com.bhavaneulergmail.newsflow.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class NewsContract {

    private NewsContract() {

    }

    public static final String CONTENT_AUTHORITY = "com.bhavaneulergmail.newsflow";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_NEWS = "news";

    public static final class NewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();

        public static final String HEADLINE = "headline";

        public static final String AUTHOR = "author";

        public static final String TABLE_NAME = "news";

        public static final String NEWS_URL = "url";


    }

}
