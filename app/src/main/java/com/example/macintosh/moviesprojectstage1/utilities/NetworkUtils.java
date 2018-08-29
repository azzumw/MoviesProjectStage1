package com.example.macintosh.moviesprojectstage1.utilities;

import android.net.Uri;

import com.example.macintosh.moviesprojectstage1.model.Movie;

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

/**
 * Created by macintosh on 26/08/2018.
 */

public class NetworkUtils {
    //private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?api_key=51ed01ec1db0ac9a518638cb27934aec";
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";

    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY_VALUE = "51ed01ec1db0ac9a518638cb27934aec";  //<--- insert your key here!


    private static final String LANGUAGE_PARAM = "language";
    private static final String EN_US = "en-US";

    private static final String PARAM_SORT = "sort_by";
    private static final String SORTBY_POPULARITY = "popularity.desc";

    private static final String PARAM_KEYWORD = "with_keywords";


    public static URL buildUrl(){
        Uri uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(LANGUAGE_PARAM,EN_US).appendQueryParameter(PARAM_SORT,SORTBY_POPULARITY).build();
        URL  url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrl(String prefValue){
        Uri uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE).appendQueryParameter(LANGUAGE_PARAM,EN_US).appendQueryParameter(PARAM_SORT,prefValue).build();
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



    public static ArrayList<Movie> getJSONData(String jsonString) throws JSONException {
        final String RESULTS_KEY = "results";
        final String TITLE_KEY = "title";
        final String ID_KEY= "id";
        final String VOTE_COUNT_KEY= "vote_count";
        final String POSTER_PATH = "poster_path";
        final String IMAGE_URL = "http://image.tmdb.org/t/p/w500";

        ArrayList<Movie> parsedMovieData  = new ArrayList<>();

            JSONObject rootJsonObject = new JSONObject(jsonString);
            JSONArray resultsArray = rootJsonObject.getJSONArray(RESULTS_KEY);

            for (int i =0; i < resultsArray.length(); i++){
                String jsonTitle = resultsArray.getJSONObject(i).optString(TITLE_KEY);
                int jsonID = resultsArray.getJSONObject(i).getInt(ID_KEY);
                int votes = resultsArray.getJSONObject(i).getInt(VOTE_COUNT_KEY);
                String jsonimage_path = resultsArray.getJSONObject(i).optString(POSTER_PATH);
                String finalJsonImagePath = IMAGE_URL+ jsonimage_path;
                URL url = null;
                try {
                    url = new URL(finalJsonImagePath);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    parsedMovieData.add(new Movie(jsonTitle,jsonID,votes,inputStream));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                 catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return parsedMovieData;

    }

    public static String getQueryParam(){
        List<String> param = Uri.parse("https://api.themoviedb.org/3/discover/movie?api_key=51ed01ec1db0ac9a518638cb27934aec&language=en-US&sort_by=popularity.desc").getPathSegments();
        return param.toString();
    }
}
