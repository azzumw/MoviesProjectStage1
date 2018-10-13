package com.example.macintosh.moviesprojectstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.macintosh.moviesprojectstage1.database.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{

    private List<Trailer> mTrailerList;
    private Context mContext;
    TrailerViewHolder trailerViewHolder;
    private TrailerAdapterOnClickHandler mTrailerAdapterClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClickHandler(Trailer trailer);
    }

    public TrailerAdapter(Context context, TrailerAdapterOnClickHandler trailerAdapterOnClickHandler){
        mContext = context;
        mTrailerAdapterClickHandler = trailerAdapterOnClickHandler;
    }
    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdforListItem = R.layout.list_item_trailers;
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(layoutIdforListItem,parent,false);
        trailerViewHolder = new TrailerViewHolder(view);
        return trailerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
//       Trailer currentTrailer = mTrailerList.get(position);
       int trailerNumber = position+1;

       holder.txtView.setText(mContext.getString(R.string.trailer_1_dummy_string));
       holder.txtView.append(" ");
       holder.txtView.append(String.valueOf(trailerNumber));
       Picasso.with(mContext).load(R.drawable.ytubefunky).into(holder.mIconYoutube);

    }

    @Override
    public int getItemCount() {
        if(mTrailerList == null){
            return 0;
        }
        return mTrailerList.size();
    }

    public void setTrailerData(List<Trailer> trailerList){
        mTrailerList = trailerList;
        notifyDataSetChanged();
    }


    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mIconYoutube;
        TextView txtView;

        public TrailerViewHolder(View itemView){
            super(itemView);
            this.mIconYoutube = itemView.findViewById(R.id.youtubeicon);
            this.txtView = itemView.findViewById(R.id.list_itemTV);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Trailer trailer = mTrailerList.get(adapterPosition);
            mTrailerAdapterClickHandler.onClickHandler(trailer);
        }
    }
}
