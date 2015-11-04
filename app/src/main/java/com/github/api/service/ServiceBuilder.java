package com.github.api.service;

import com.github.api.retrofit.converter.ToStringFactory;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by RobGThai on 11/5/15.
 */
public class ServiceBuilder {

    private static final String url = "https://api.github.com";

//    public Retrofit build() {
//        return new Retrofit.Builder()
//                    .baseUrl(url)
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .addConverterFactory(new ToStringFactory())
//                    .client(new OkHttpClient())
//                    .build();
//    }

    public Retrofit.Builder get() {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

}
