package com.example.macintosh.moviesprojectstage1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.macintosh.moviesprojectstage1.model.Movie;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    private TextView tvReleaseDateValue;
    private TextView tvTitleValue;
    private TextView tvPlotVotes;
    private TextView tvPlotSynopsis;
    private ImageView posterView;

    private final String PLOT_SYN_KEY = "plotSynopsis";
    private final String RELEASE_DATE_KEY = "releaseDate";
    private final String ID_KEY = "id";
    private final String TITLE_KEY = "title";
    private final String IMAGE_KEY =  "image";
    private final String PLOT_AVG_KEY = "plotAverage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        tvPlotSynopsis = findViewById(R.id.tvPlotSynopsisValue);
        tvTitleValue = findViewById(R.id.tvTitle);
        tvReleaseDateValue = findViewById(R.id.releaseDateValue);
        tvPlotVotes = findViewById(R.id.plotAvgValue);

        posterView = findViewById(R.id.posterId);


        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

//         int id = movie.getid();
//         int voteCount = movie.getVoteCount();
         String imageUrl = intent.getStringExtra(IMAGE_KEY);
         String plotSynopsis = intent.getStringExtra(PLOT_SYN_KEY);
         int plotAverage = intent.getIntExtra(PLOT_AVG_KEY,0);
         String releaseDate = intent.getStringExtra(RELEASE_DATE_KEY);


         Picasso.with(this).load(imageUrl).into(posterView);
         tvTitleValue.setText(title);
         tvReleaseDateValue.setText(releaseDate);
         tvPlotVotes.setText(String.valueOf(plotAverage));
         tvPlotSynopsis.setText(plotSynopsis);



    }


}
