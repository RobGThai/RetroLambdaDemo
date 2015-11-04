package com.github.api.service;

import com.squareup.okhttp.OkHttpClient;

import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by RobGThai on 11/5/15.
 */
public class ServiceBuilder {

    private static final String url = "https://api.github.com";

    public static Retrofit build() {

        return new Retrofit.Builder()
                           .baseUrl(url)
                           .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                           .client(new OkHttpClient())
                           .build();
    }

}
