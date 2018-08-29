package com.example.macintosh.moviesprojectstage1;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.macintosh.moviesprojectstage1.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macintosh on 28/08/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private ArrayList<Movie> movieArrayList ;

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIDforListItem = R.layout.list_item;
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(layoutIDforListItem,parent,false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.mMovieTextView.setText(movieArrayList.get(position).getTitle());
        holder.mMoviePoster.setImageBitmap(movieArrayList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        if(movieArrayList ==null) return 0;
        return movieArrayList.size();
    }

    public void setMovieData(ArrayList<Movie> movieList){
        movieArrayList = movieList;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        TextView mMovieTextView;
        ImageView mMoviePoster;

        public MovieViewHolder(View itemView){
            super(itemView);
            this.mMovieTextView = itemView.findViewById(R.id.tv_item);
            this.mMoviePoster = itemView.findViewById(R.id.poster);
        }

    }
}
