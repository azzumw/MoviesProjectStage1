package com.example.macintosh.moviesprojectstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.macintosh.moviesprojectstage1.database.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movieList;
    private Context mContext;
    MovieViewHolder movieViewHolder;

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
        movieViewHolder = new MovieViewHolder(view);

        return movieViewHolder;
    }

    public int getPosition(){
        return movieViewHolder.getAdapterPosition();
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie currentMovie = movieList.get(position);
        String imageUrl = currentMovie.getImageUrl();
        Picasso.with(mContext).load(imageUrl).into(holder.mMoviePoster);
    }

    @Override
    public int getItemCount() {
        if(movieList ==null) return 0;
        return movieList.size();
    }

    public void setMovieData(List<Movie> list){
        movieList = list;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mMoviePoster;

        public MovieViewHolder(View itemView){
            super(itemView);
            this.mMoviePoster = itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }

        public int getPostion(){
            return getAdapterPosition();
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = movieList.get(adapterPosition);
            mMovieAdapterOnClickHandler.onClickHandler(movie);
        }
    }
}
