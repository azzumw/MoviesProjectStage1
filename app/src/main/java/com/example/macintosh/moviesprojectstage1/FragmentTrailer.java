package com.example.macintosh.moviesprojectstage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private final String MOVIE_ID_KEY = "movie_id";
    private TextView errorMessage;

    public FragmentTrailer() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e("OnCreateView", "I am in OncReateView");
        view = inflater.inflate(R.layout.frag_trailer,container,false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        errorMessage = view.findViewById(R.id.frag_trailer__tv_error_message_display);
        mRecyclerView =  view.findViewById(R.id.recyclerViewTrailerFrag);
        mTrailerAdapter = new TrailerAdapter(getContext(),this);
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        if(isOnline()){
            loadTrailers();
        }else{
            displayErrorMessage();
        }

        mRecyclerView.setAdapter(mTrailerAdapter);

    }

    public static FragmentTrailer getInstance(int param){
        Bundle args = new Bundle();
        args.putInt("movie_id", param);
        FragmentTrailer fragment = new FragmentTrailer();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       Log.e("OnCreate", "I am in OncReate");

        if(getArguments()!= null){

            movieId = getArguments().getInt(MOVIE_ID_KEY);
        }

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
                            Log.e("runOnUIThread",""+trailersList.size());
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

    private boolean isOnline() {
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
