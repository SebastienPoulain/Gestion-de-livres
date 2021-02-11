package com.example.livre3;

/**
 * @author  Émilie cormier houle
 * classe qui permet d'instancier rétrofit
 */

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitInstance {
    public static final String BASE_URL = "http://206.167.140.56:8080/420617RI/Equipe_2/";
    private static Retrofit retrofit;

    /**
     *
     * @return une instance de rétrofit
     */
    public static Retrofit getInstance(){

        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
