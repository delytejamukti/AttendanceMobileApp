package teja.dely.cameraapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DELY on 10/24/2018.
 */

public class Server {
    public static final String url = "http://etc.if.its.ac.id/";

    private static Retrofit retrofit;

    public static Retrofit getclient()
    {
        if (retrofit==null)
        {
            retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
