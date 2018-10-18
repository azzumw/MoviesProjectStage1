package com.example.macintosh.moviesprojectstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.macintosh.moviesprojectstage1.database.Review;

import java.util.List;

class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> mReviewList;

    public ReviewAdapter() {
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdforListItem = R.layout.list_item_review;
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(layoutIdforListItem,parent,false);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review currentReview = mReviewList.get(position);
        holder.authortxtview.setText(currentReview.getAuthor());
        holder.reviewtxtview.setText(currentReview.getReview());
    }

    @Override
    public int getItemCount() {
        if(mReviewList == null){
            return 0;
        }
        return mReviewList.size();
    }

    public void setmReviewData(List<Review> reviewData){
        this.mReviewList = reviewData;
        notifyDataSetChanged();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView authortxtview;
        private TextView reviewtxtview;

       public ReviewViewHolder(View view){
           super(view);
           authortxtview = view.findViewById(R.id.authorNameLabel);
           reviewtxtview = view.findViewById(R.id.reviewLabel);

       }
    }
}
