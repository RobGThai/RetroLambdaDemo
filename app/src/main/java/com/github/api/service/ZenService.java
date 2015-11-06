package com.github.api.service;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.github.api.retrofit.converter.ToStringFactory;
import com.github.api.rx.transformer.AndroidHelper;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Inject;

import retrofit.Retrofit;
import rx.Observable;

public class ZenService {

    Retrofit.Builder builder;
    private GitHubService service;

    @Inject
    public ZenService(Retrofit.Builder builder) {
        this.builder = builder;
        build();
    }

    private void build() {
        service = this.builder
            .addConverterFactory(new ToStringFactory())
            .client(new OkHttpClient())
            .build().create(GitHubService.class);
    }

    @RxLogObservable
    public Observable<String> zen() {
        return service.zen().compose(AndroidHelper.applySchedulers());
    }
}
