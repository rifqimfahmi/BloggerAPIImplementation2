package renotekno.com.bloggerapiimplementation2.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import renotekno.com.bloggerapiimplementation2.Model.Data;
import renotekno.com.bloggerapiimplementation2.Model.PostSnippet;
import renotekno.com.bloggerapiimplementation2.R;

/**
 * Created by zcabez on 20/08/2017.
 */

public class PostsListAdapter extends RecyclerView.Adapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_list_item, parent, false);
        return new PostItemVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostItemVH) {
            ((PostItemVH) holder).bindData(position);
        }
    }

    @Override
    public int getItemCount() {
        return Data.featuredPostSnippetList.size();
    }

    public class PostItemVH extends RecyclerView.ViewHolder {

        private TextView postTitle;
        private TextView postPublishedDate;
        private TextView postLabel;
        private TextView postRepliesTotal;
        private ImageView postThumbnail;
        private View view;

        public PostItemVH(View itemView) {
            super(itemView);
            view = itemView;
            postTitle = (TextView) itemView.findViewById(R.id.postTitle);
            postPublishedDate = (TextView) itemView.findViewById(R.id.postPublishedDate);
            postLabel = (TextView) itemView.findViewById(R.id.postLabel);
            postRepliesTotal = (TextView) itemView.findViewById(R.id.postRepliesTotal);
            postThumbnail = (ImageView) itemView.findViewById(R.id.postThumbnail);
        }

        public void bindData(int position) {
            PostSnippet postSnippet = Data.featuredPostSnippetList.get(position);

            // TODO : Add sanity check such as the length of the text and image URL is not null
            postTitle.setText(postSnippet.getPostTitle());
            postPublishedDate.setText(postSnippet.getPostPublishedDate());
            postLabel.setText(postSnippet.getPostLabel());
            postRepliesTotal.setText(postSnippet.getPostRepliesTotal());

            Glide.with(view.getContext()).load(postSnippet.getPostImageThumbnail()).into(postThumbnail);
        }
    }
}
