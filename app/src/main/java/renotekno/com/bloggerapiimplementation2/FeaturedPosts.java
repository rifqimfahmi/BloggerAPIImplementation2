package renotekno.com.bloggerapiimplementation2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import renotekno.com.bloggerapiimplementation2.Adapter.PostsListAdapter;
import renotekno.com.bloggerapiimplementation2.Decoration.DividerPosts;
import renotekno.com.bloggerapiimplementation2.Model.Data;
import renotekno.com.bloggerapiimplementation2.Model.PostSnippet;

/**
 * Created by zcabez on 20/08/2017.
 */

public class FeaturedPosts extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private PostsListAdapter postsListAdapter;

    // swipe to refresh view
    private SwipeRefreshLayout swapRefreshPostsList;

    // swipe refresh empty layout and as the emptyview ViewGroup
    private SwipeRefreshLayout swapRefreshEmptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // declare variable value/instance that we want to retain
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_view, container, false);
        initView(view);

        // configure recyclerview such as fixedsize true, set the adapter, layout manager, etc
        configureRV();

        /**
         * create new listener SwipeRefreshLayout.OnRefreshListener and attach it to the {@link #swapRefreshPostsList} and {@link #swapRefreshEmptyView}
         */
        configureSwapRefresher();

        return view;
    }

    private void configureSwapRefresher() {
        // Create the refresh listener when user swipe up to refresh
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // refresh posts list by starting new request
                requestFeaturedPosts();
            }
        };

        // attach the newly created listener to it
        swapRefreshPostsList.setOnRefreshListener(onRefreshListener);
        swapRefreshEmptyView.setOnRefreshListener(onRefreshListener);
    }

    private void configureRV() {
        /**
         * initiate new Adapter which is new {@link PostsListAdapter} for {@link #recyclerView}
         */
        postsListAdapter = new PostsListAdapter();

        // this recyclerview has fixed size
        recyclerView.setHasFixedSize(true);

        // set the layout manager to be vertical LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // set the adapter
        recyclerView.setAdapter(postsListAdapter);

        // Set overscroll so that no bounce blue wave feedback at the vertocal edge
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        /**
         * Add divider for {@link #recyclerView} with our custom ItemDecoration class {@link DividerPosts}
         */
        recyclerView.addItemDecoration(new DividerPosts(ContextCompat.getDrawable(getContext(), R.drawable.divider_posts_list)));
    }

    /**
     * Start requesting data and displaying data on the screen in onStart()
     */
    @Override
    public void onStart() {
        super.onStart();
        /*
         * Request the featured posts from the blogger API v3
         * using Volley google library
         * the featured is got from posts labeled with "featured"
         */

        // only request data when its the first time request
        // check if the posts array has no item yet
        if (Data.featuredPostSnippetList.size() == 0) {
            // show refresh indicator of the posts view
            swapRefreshPostsList.setRefreshing(true);

            // if there is not item yet in the posts array start request data
            requestFeaturedPosts();
        } else {
            /**
             * if @{@link Data.featuredPostSnippetList} size is not equal != 0 mean we have already
             * extract the data from the previous request, just notify recyclerview adapter {@link #postsListAdapter}
             */
            updateView();
        }
    }

    private void requestFeaturedPosts() {
        /**
         *  Start network request using Volley with {@link JsonObjectRequest}
         */
        SingleVolley.getIstance(getContext()).addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                // get the featured posts URL from v3 Blogger API
                Data.getFeaturedPostsURL(),
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
                        // error happened, check if posts array is empty
                        if (Data.featuredPostSnippetList.isEmpty()) {
                            // if empty show empty view
                            showEmptyView();
                        }

                        // display toast message letter
                        Toast.makeText(getContext(), "Failed requesting data, try again later", Toast.LENGTH_SHORT).show();
                        // remove the loading indicator
                        swapRefreshPostsList.setRefreshing(false);
                        swapRefreshEmptyView.setRefreshing(false);
                    }
                })
        );
    }

    private void processSuccessResponse(JSONObject response) throws JSONException {
        // check if the returned JSON has posts data > 0
        if (response.has("items")) {
            /*
             * we are about to extract posts data from JSON.
             * Clear the data from the array and notify adapter to empty recycleView item view.
             * This is useful when user refresh the page and the request is successful
             */
            Data.featuredPostSnippetList.clear();

            // start extracting posts data from JSON response
            extractPostsData(response);

            // update the view that the extracting has been done
            updateView();

            // remove the loading indicator
            swapRefreshPostsList.setRefreshing(false);
            swapRefreshEmptyView.setRefreshing(false);
        } else {
            // No posts data returned. Display empty view to the view
            showEmptyView();
        }
    }

    private void extractPostsData(JSONObject response) throws JSONException {
        // get the items array that contain posts
        JSONArray jsonArray = response.getJSONArray("items");
        // loop through each element of the array that contain data of each post
        for (int i = 0; i < jsonArray.length(); i++) {
            // get the single post object
            JSONObject post = jsonArray.getJSONObject(i);

            /**
             * Extract data needed to display the post snippet and create new instance of @{@link PostSnippet}
             */
            String postTitle = post.getString("title");
            String postPublishedDate = post.getString("published");
            String postImageThumbnail = post.has("images") ? post.getJSONArray("images").getJSONObject(0).getString("url") : null;
            String postRepliesTotal = post.getJSONObject("replies").getString("totalItems");
            String postLabel = post.has("labels") ? post.getJSONArray("labels").getString(0) : Data.UNCATEGORIZED_POST_LABEL;
            String postID = post.getString("id");

            Data.featuredPostSnippetList.add(new PostSnippet(postTitle, postLabel, postRepliesTotal, postImageThumbnail, postPublishedDate, postID));
        }
    }

    private void showEmptyView() {
        swapRefreshEmptyView.setVisibility(View.VISIBLE);
        swapRefreshPostsList.setVisibility(View.INVISIBLE);
    }

    private void removeEmptyView() {
        // hide empty view layout when it is currently shown in the screen, otherwise not
        if (swapRefreshEmptyView.isShown() && !swapRefreshPostsList.isShown()) {
            swapRefreshEmptyView.setVisibility(View.INVISIBLE);
            swapRefreshPostsList.setVisibility(View.VISIBLE);
        }
    }

    // Notify recyclerview adapter that the data it observed has been changed or added
    private void updateView() {
        // notify adapter that array items has been changed
        postsListAdapter.notifyDataSetChanged();

        /**
         * hide empty view layout which is {@link #swapRefreshEmptyView}
         */
        removeEmptyView();
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.postsListRV);
        swapRefreshPostsList = (SwipeRefreshLayout) view.findViewById(R.id.swapRefreshPostsList);
        swapRefreshEmptyView = (SwipeRefreshLayout) view.findViewById(R.id.swapRefreshEmptyView);
    }

}
