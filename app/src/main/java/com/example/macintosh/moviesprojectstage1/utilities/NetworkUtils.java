package com.example.macintosh.moviesprojectstage1.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.macintosh.moviesprojectstage1.database.Movie;
import com.example.macintosh.moviesprojectstage1.database.Review;
import com.example.macintosh.moviesprojectstage1.database.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtils {


    private static final String BASE_URL = "http://api.themoviedb.org/3/movie?";
    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY_VALUE = "";  //<--- insert your key here!

    public static URL buildUrl(String prefValue)  {

        Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath(prefValue).appendQueryParameter(API_KEY_PARAM,API_KEY_VALUE).build();

        URL  url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrl(int id,String type){
        String id_toString = String.valueOf(id);
        Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath(id_toString).appendPath(type).appendQueryParameter(API_KEY_PARAM,API_KEY_VALUE).build();

        Log.v("URI BUILT: ",uri.toString());
        URL  url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        //down cast the URLConnection object returned by openConnection()  it to its subclass type HttpURLconnection.
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }



    public static ArrayList<Movie> getJSONMovieData(String jsonString) throws JSONException {
        final String RESULTS_KEY = "results";
        final String TITLE_KEY = "title";
        final String ID_KEY= "id";
        final String VOTE_COUNT_KEY= "vote_count";
        final String VOTE_AVG_KEY = "vote_average";
        final String POSTER_PATH = "poster_path";
        final String IMAGE_URL = "http://image.tmdb.org/t/p/w500";
        final String RELEASE_DATE_KEY = "release_date";
        final String PLOT_SYNOPSIS_KEY = "overview";

        ArrayList<Movie> parsedMovieData  = new ArrayList<>();

        JSONObject rootJsonObject = new JSONObject(jsonString);
        JSONArray resultsArray = rootJsonObject.getJSONArray(RESULTS_KEY);

        for (int i =0; i < resultsArray.length(); i++){
            String jsonTitle = resultsArray.getJSONObject(i).optString(TITLE_KEY);
            int jsonID = resultsArray.getJSONObject(i).getInt(ID_KEY);
            int votes = resultsArray.getJSONObject(i).getInt(VOTE_COUNT_KEY);
            int vote_average = resultsArray.getJSONObject(i).getInt(VOTE_AVG_KEY);
            String release_date = resultsArray.getJSONObject(i).optString(RELEASE_DATE_KEY);
            String plot_synopsis = resultsArray.getJSONObject(i).optString(PLOT_SYNOPSIS_KEY);
            String jsonimage_path = resultsArray.getJSONObject(i).optString(POSTER_PATH);
            String finalJsonImagePath = IMAGE_URL+ jsonimage_path;
            parsedMovieData.add(new Movie(jsonTitle,jsonID,votes,finalJsonImagePath,release_date,vote_average,plot_synopsis));
        }

        return parsedMovieData;
    }

    public static List<Trailer> getJSONTrailerData(String jsonString) throws JSONException {

        final String RESULTS_KEY = "results";
        final String ID_KEY = "id";
        final String TRAILER_NAME_KEY = "name";
        final String VIDEO_KEY = "key";
        final String VIDEO_TYPE = "type";

        ArrayList<Trailer> parseTrailerData = new ArrayList<>();

        JSONObject jsonRootObj = new JSONObject(jsonString);
        JSONArray resultsArray = jsonRootObj.getJSONArray(RESULTS_KEY);

        for(int i=0; i<resultsArray.length();i++){
            String id = resultsArray.getJSONObject(i).getString(ID_KEY);
            String nameofTrailer = resultsArray.getJSONObject(i).getString(TRAILER_NAME_KEY);
            String videokey = resultsArray.getJSONObject(i).getString(VIDEO_KEY);
            String videoType = resultsArray.getJSONObject(i).getString(VIDEO_TYPE);
            parseTrailerData.add(new Trailer(id,nameofTrailer,videokey,videoType));
        }

        return parseTrailerData;
    }

    public static List<Review> getJSONReviewData(String jsonString) throws JSONException {
        final String AUTHOR_KEY = "author";
        final String REVIEW_KEY = "content";
        final String RESULTS_KEY = "results";

        ArrayList<Review> parsedReviewData = new ArrayList<>();

        JSONObject rootObj = new JSONObject(jsonString);
        JSONArray resultsArray = rootObj.getJSONArray(RESULTS_KEY);

        for(int i= 0; i < resultsArray.length(); i++){
            String author = resultsArray.getJSONObject(i).getString(AUTHOR_KEY);
            String review = resultsArray.getJSONObject(i).getString(REVIEW_KEY);
            parsedReviewData.add(new Review(review,author));
        }
        return parsedReviewData;
    }
}
