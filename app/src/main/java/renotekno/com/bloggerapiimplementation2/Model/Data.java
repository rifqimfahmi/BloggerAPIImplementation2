package renotekno.com.bloggerapiimplementation2.Model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcabez on 20/08/2017.
 * Data class to hold public static variable
 */

public final class Data {
    /**
     * This values can be interchangeably based on the {@link #BLOG_ID} and {@link #API_KEY}
     * {@link #BLOG_ID} is your blog id
     * {@link #API_KEY} is your API key you get from google cloud platform
     */
    // This is your blog id
    public static final String BLOG_ID = "2171814962865963788";

    // This is your API which you get from Google Cloud API for blogger v3
    public static final String API_KEY = "AIzaSyBAuHn6Udd53-J91JImqmjByfwpufAkqMo";
    /**
     * End interchangeably values
     */

    // The type of data we want to query which is the posts of your blog
    public static final String POST_TYPE = "posts";

    // the query parameter if we want to fetch bodies or not, value must be boolean
    public static final String IS_FETCH_BODIES_PARAM = "fetchBodies";

    // the query parameter if we want to fetch images or not, value must be boolean
    public static final String IS_FETCH_IMAGES_PARAM = "fetchImages";

    // the query parameter for label of post we want to query
    public static final String LABEL_QUERY_PARAM = "labels";

    // default label for post with no label
    public static final String UNCATEGORIZED_POST_LABEL = "Uncategorized";

    // default post title length for the snippet post
    public static final int MAX_POST_SNIPPED_TITLE = 69;

    // post ID label tag for intent
    public static final String POST_ID = "post_id";

    /**
     * static array that hold the featured posts {@link renotekno.com.bloggerapiimplementation2.FeaturedPosts}  instance
     */
    public static List<PostSnippet> featuredPostSnippetList = new ArrayList<>();

    public static String getFeaturedPostsURL() {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .authority("www.googleapis.com")
                .appendPath("blogger")
                .appendPath("v3")
                .appendPath("blogs")
                .appendPath(Data.BLOG_ID)
                .appendPath(Data.POST_TYPE)
                .appendQueryParameter(Data.IS_FETCH_BODIES_PARAM, "false")
                .appendQueryParameter(Data.IS_FETCH_IMAGES_PARAM, "true")
                .appendQueryParameter(Data.LABEL_QUERY_PARAM, "featured")
                .appendQueryParameter("key", Data.API_KEY)
                .build();


        return uri.toString();
    }
}
