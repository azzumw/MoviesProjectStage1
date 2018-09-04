package com.example.macintosh.moviesprojectstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.macintosh.moviesprojectstage1.model.Movie;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private ArrayList<Movie> movieArrayList ;
    private Context mContext;

    private final MovieAdapterOnClickHandler mMovieAdapterOnClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClickHandler(Movie movie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler movieAdapterOnClickHandler, Context context){
        mMovieAdapterOnClickHandler = movieAdapterOnClickHandler;
        mContext = context;
    }

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
        Movie currentMovie = movieArrayList.get(position);
        String imageUrl = currentMovie.getImage();
        Picasso.with(mContext).load(imageUrl).into(holder.mMoviePoster);
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

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mMoviePoster;

        public MovieViewHolder(View itemView){
            super(itemView);
            this.mMoviePoster = itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = movieArrayList.get(adapterPosition);
            mMovieAdapterOnClickHandler.onClickHandler(movie);
        }
    }
}
