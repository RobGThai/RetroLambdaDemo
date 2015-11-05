package com.github.api.service;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.github.api.retrofit.converter.ToStringFactory;
import com.github.api.rx.transformer.AndroidHelper;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Retrofit;
import rx.Observable;

public class ZenService {
    private Retrofit.Builder builder;
    private GitHubService service;

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
