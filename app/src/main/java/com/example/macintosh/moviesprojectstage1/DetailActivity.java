package com.example.macintosh.moviesprojectstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;



public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvReleaseDateValue;
        TextView tvTitleValue;
        TextView tvPlotVotes;
        TextView tvPlotSynopsis;
        ImageView posterView;

        final String PLOT_SYN_KEY = "plotSynopsis";
        final String RELEASE_DATE_KEY = "releaseDate";
        final String TITLE_KEY = "title";
        final String IMAGE_KEY =  "image";
        final String PLOT_AVG_KEY = "plotAverage";

        tvPlotSynopsis = findViewById(R.id.tvPlotSynopsisValue);
        tvTitleValue = findViewById(R.id.tvTitle);
        tvReleaseDateValue = findViewById(R.id.releaseDateValue);
        tvPlotVotes = findViewById(R.id.plotAvgValue);
        posterView = findViewById(R.id.posterId);

        Intent intent = getIntent();
        String title = intent.getStringExtra(TITLE_KEY);

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
