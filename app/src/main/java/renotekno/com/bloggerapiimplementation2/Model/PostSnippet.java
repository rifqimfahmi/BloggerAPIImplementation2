package renotekno.com.bloggerapiimplementation2.Model;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zcabez on 20/08/2017.
 */

public class PostSnippet {
    private String postTitle;
    private String postLabel;
    private String postRepliesTotal;
    private String postImageThumbnail;
    private String postPublishedDate;

    public PostSnippet(String postTtitle, String postLabel, String postRepliesTotal, String postImageThumbnail, String postPublishedDate) {
        this.postTitle = postTtitle;
        this.postLabel = postLabel;
        this.postRepliesTotal = postRepliesTotal;
        this.postImageThumbnail = postImageThumbnail;
        this.postPublishedDate = postPublishedDate;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostLabel() {
        return postLabel;
    }

    public String getPostRepliesTotal() {
        return postRepliesTotal;
    }

    public String getPostImageThumbnail() {
        return postImageThumbnail;
    }

    public String getPostPublishedDate() {
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat desiredFormat  = new SimpleDateFormat("d MMM yyyy");
        String dateFormat = null;
        try {
            Date date = currentDateFormat.parse(postPublishedDate);
            dateFormat = desiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateFormat;
    }
}
