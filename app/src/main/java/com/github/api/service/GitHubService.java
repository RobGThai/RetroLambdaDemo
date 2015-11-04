package com.github.api.service;
import retrofit.http.GET;
import rx.Observable;

public interface GitHubService {

    @GET("/zen")
    Observable<String> zen();
}
