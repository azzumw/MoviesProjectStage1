package com.example.macintosh.moviesprojectstage1;

import com.example.macintosh.moviesprojectstage1.utilities.NetworkUtils;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testUriPath(){
        URL url = NetworkUtils.buildUrl("popular");
        String expectvalue = "http://api.themoviedb.org/3/movie/popular?api_key=51ed01ec1db0ac9a518638cb27934aec";
        String returnedValue = url.toString();
        assertEquals(expectvalue,returnedValue);
    }
}