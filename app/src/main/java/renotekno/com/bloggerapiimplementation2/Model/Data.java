package renotekno.com.bloggerapiimplementation2.Model;

import android.net.Uri;
import android.view.View;

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
    public static final String BLOG_ID = "7038144870910668918";

    // This is your API which you get from Google Cloud API for blogger v3
    public static final String API_KEY = "AIzaSyBAuHn6Udd53-J91JImqmjByfwpufAkqMo";
    /**
     * End interchangeably values
     */

    // The type of data we want to query which is the posts of your blog
    public static final String POST_TYPE = "posts";

    // the query parameter for API key
    public static final String KEY_PARAM = "key";

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

    /**
     *
     * All static variable needed to load the post to the view {@link renotekno.com.bloggerapiimplementation2.PostActivity}
     *
     */
    // regex to match h2s and its child/innerHTML in the text
    public static final String H2_REGEX_MATCHER = "<[\\s*]?+h2[\\s*]?+>(.*?)<[\\s*]?+\\/h2[\\s*]?+>";

    // regex to match img tag <img/> and its src value as group 1
    public static final String IMG_REGEX_MATCHER = "<img\\s[^>]*?src\\s*=\\s*['\\\"]([^'\\\"]*?)['\\\"][^>]*?>";

    // regex to match li's tag <li> and match its child/innerHTML as group 2
    public static final String LI_REGEX_MATCHER = "[<](\\s+)?li[^>]*[>](.+?)[<](\\s+)?[\\/]?(\\s+)?li[^>]*[>]";
    // regex to match li's opening tag <li>
    public static final String LI_OPENING_REGEX_MATCHER = "[<](\\s+)?li[^>]*[>]";
    // regex to match li's closing tag </li>
    public static final String LI_CLOSING_REGEX_MATCHER = "[<](\\s+)?(/)(\\s+)?li[^>]*[>]";

    // regex to match br tags <br>
    public static final String BR_MATCHER = "[<](/)?br[^>]*[>]";

    // tag for passing html body content in bundle to loader
    public static final String POST_CONTENT_TAG_BUNDLE = "post_content_tag_bundle";

    public static final String NON_BREAKING_SPACES = "&nbsp;";

    // Loader id to create array of views
    public static final int POST_RENDERER_LOADER_ID = 1;

    /**
     * Singleton variable for each post data
     * This variable will be interchangeably based on post to be loaded
     */

    // hold the images URL and the arrangement based on image position
    public static List<String> imagesSrc = new ArrayList<>();
    // image <img> wrapped in <a>
    public static List<String> imageLink = new ArrayList<>();
    /**
     *
     * End All static variable needed to load the post to the view {@link renotekno.com.bloggerapiimplementation2.PostActivity}
     *
     */


    public static String getFeaturedPostsURL() {
        return new Uri.Builder()
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
                .appendQueryParameter(Data.KEY_PARAM, Data.API_KEY)
                .build()
                .toString();
    }

    public static String getPostData(String postID) {
        return new Uri.Builder()
                .scheme("https")
                .authority("www.googleapis.com")
                .appendPath("blogger")
                .appendPath("v3")
                .appendPath("blogs")
                .appendPath(Data.BLOG_ID)
                .appendPath(Data.POST_TYPE)
                .appendPath(postID)
                .appendQueryParameter(Data.IS_FETCH_IMAGES_PARAM, "true")
                .appendQueryParameter(Data.KEY_PARAM, Data.API_KEY)
                .build()
                .toString();
    }
}
