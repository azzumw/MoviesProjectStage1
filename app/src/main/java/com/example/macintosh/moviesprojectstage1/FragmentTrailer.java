package com.example.macintosh.moviesprojectstage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.macintosh.moviesprojectstage1.database.AppExecutors;
import com.example.macintosh.moviesprojectstage1.database.Trailer;
import com.example.macintosh.moviesprojectstage1.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class FragmentTrailer extends Fragment implements TrailerAdapter.TrailerAdapterOnClickHandler{

    private View view;

    private RecyclerView mRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int movieId;

    private TextView errorMessage;

    public FragmentTrailer() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_trailer,container,false);
        errorMessage = view.findViewById(R.id.frag_trailer__tv_error_message_display);
        mRecyclerView =  view.findViewById(R.id.recyclerViewTrailerFrag);
        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(getContext(),this);
        // make network request to get the video links

        mRecyclerView.setAdapter(mTrailerAdapter);

        movieId = this.getArguments().getInt("movie_id");

        if(isOnline()){
            loadTrailers();
        }else{
            displayErrorMessage();
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void loadTrailers(){


        final URL searchURL = NetworkUtils.buildUrl(movieId,EndPoints.VIDEOS.getType());

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
                            List<Trailer> trailersList = NetworkUtils.getJSONTrailerData(httpResponse[0]);
                            setTrailers(trailersList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }


    private void setTrailers(List<Trailer> trailers) {
        mTrailerAdapter.setTrailerData(trailers);
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

    @Override
    public void onClickHandler(Trailer trailer) {
        String videoId = trailer.getKey();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+videoId));
        intent.putExtra("VIDEO_ID", videoId);
        startActivity(intent);
    }
}
