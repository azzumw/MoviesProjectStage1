package com.example.macintosh.moviesprojectstage1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.macintosh.moviesprojectstage1.database.AppExecutors;
import com.example.macintosh.moviesprojectstage1.database.Movie;
import com.example.macintosh.moviesprojectstage1.database.Review;
import com.example.macintosh.moviesprojectstage1.utilities.NetworkUtils;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class FragmentReviews extends Fragment{
    private View view;
    private RecyclerView mRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int movieId;
    private final String MOVIE_ID_KEY= "movie_id";
    private TextView errorMessage;

    public FragmentReviews(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_review,container,false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!= null)
            movieId = getArguments().getInt(MOVIE_ID_KEY);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        errorMessage = view.findViewById(R.id.frag_review__tv_error_message_display);

        mRecyclerView = view.findViewById(R.id.recyclerViewReviewFrag);
        //get the list
        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter();

        if(isOnline()){
            loadReviews();
        }else{
            displayErrorMessage();
        }

        mRecyclerView.setAdapter(mReviewAdapter);

    }

    public static FragmentReviews getInstance(int param){
        FragmentReviews fragment = new FragmentReviews();
        Bundle args = new Bundle();
        args.putInt("movie_id", param);
        fragment.setArguments(args);
        return fragment;
    }


    private void loadReviews(){


        final URL searchURL = NetworkUtils.buildUrl(movieId,EndPoints.REVIEWS.getType());

        final String[] httpResponse = new String[1];
        AppExecutors.getInstance().getNetworkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    httpResponse[0] = NetworkUtils.getResponseFromHttpUrl(searchURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<Review> reviewList = NetworkUtils.getJSONReviewData(httpResponse[0]);
                            if(reviewList.isEmpty()){
                                disPlayNoResultsFound();
                            } else{
                                setReviews(reviewList);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    private void setReviews(List<Review> reviews){
        mReviewAdapter.setmReviewData(reviews);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void displayErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setText(getString(R.string.no_review_error));
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void disPlayNoResultsFound(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setText("No reviews found");
        errorMessage.setVisibility(View.VISIBLE);
    }

}
