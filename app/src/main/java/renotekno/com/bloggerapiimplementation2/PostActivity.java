package renotekno.com.bloggerapiimplementation2;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.inthecheesefactory.thecheeselibrary.widget.AdjustableImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import renotekno.com.bloggerapiimplementation2.Loader.PostLoader;
import renotekno.com.bloggerapiimplementation2.Model.Data;

public class PostActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<View>> {

    private LinearLayout postViewGroup;
    private Toolbar postActivityToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initView();

        setSupportActionBar(postActivityToolBar);

        // check if intent is not null
        if (getIntent() != null) {
            // process request post data based on Post ID got from Intent
            requestPostData(getIntent().getStringExtra(Data.POST_ID));
        }
    }

    private void requestPostData(String postID) {
        SingleVolley.getIstance(getApplicationContext()).addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                // get the post data URL from v3 Blogger API
                Data.getPostData(postID),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /*
                         * Request has been finished and successful
                         * Extract the data we need from the JSON
                         * and populate it to the view screen
                         */
                        try {
                            processSuccessResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error happened

                    }
                })
        );
    }

    private void processSuccessResponse(JSONObject response) throws JSONException {
        // get the HTML body content of the post
        String content = response.getString("content");

        // Create new bundle instance to deliver the HTML body content to the loader
        Bundle bundle = new Bundle();
        // package the HTML body content to the bundler
        bundle.putString(Data.POST_CONTENT_TAG_BUNDLE, content);

        // Start loading data using loader with id Data.POST_RENDERER_LOADER_ID (1)
        getSupportLoaderManager().initLoader(Data.POST_RENDERER_LOADER_ID, bundle, this);
    }

    private void inflateView(List<View> viewsComponents) {
        // keep track of the image URL arrays index if exist start with 0
        int imageIndex = 0;
        // check if the views arrays is not null
        if (viewsComponents.size() > 0) {
            // loop through each view to inflate it to the viewgroup
            for (int i = 0; i < viewsComponents.size(); i++) {
                View view = viewsComponents.get(i);

                // if the view is instance of ImageView start loading image from the URL with glide
                if (view instanceof AdjustableImageView) {
                    // configure the imageView attributes and values
                    // Must be done in the MainThread
                    AdjustableImageView imageView = (AdjustableImageView) view;
                    imageView.setPadding(0, 0, 0, 24);
                    imageView.setAdjustViewBounds(true);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                    // Load the image with glide
                    Glide.with(PostActivity.this)
                            .load(Data.imagesSrc.get(imageIndex))
                            .apply(requestOptions)
                            .into(imageView);

                    // Increase image index position to be corresponded with the images URL array index
                    imageIndex++;
                }

                // inflate the view to the viewgroup
                if (view.getParent() != null) {
                    ((ViewGroup) view.getParent()).removeView(view);
                }
                postViewGroup.addView(view);
            }
        }
    }

    @Override
    public Loader<List<View>> onCreateLoader(int id, Bundle args) {
        return new PostLoader(PostActivity.this, args.getString(Data.POST_CONTENT_TAG_BUNDLE));
    }

    @Override
    public void onLoadFinished(Loader<List<View>> loader, List<View> viewsComponents) {
        // Loader has finish creating array of views
        // If the view array is not null and the size > 0 inflate the view
        if (viewsComponents != null && viewsComponents.size() > 0) {
            inflateView(viewsComponents);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<View>> loader) {

    }

    @Override
    protected void onDestroy() {
        postViewGroup.removeAllViews();
        postViewGroup = null;
        getLoaderManager().destroyLoader(Data.POST_RENDERER_LOADER_ID);
        super.onDestroy();
    }

    private void initView() {
        postViewGroup = (LinearLayout) findViewById(R.id.postViewGroup);
        postActivityToolBar = (Toolbar) findViewById(R.id.postActivityToolBar);
    }
}
