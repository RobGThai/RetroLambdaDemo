package com.github.api.service;

import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by RobGThai on 11/5/15.
 */
@Module
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

    @Provides
    public Retrofit.Builder provideBuilder() {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }


    @SuppressWarnings("unused")
    public Retrofit.Builder get() {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

}
