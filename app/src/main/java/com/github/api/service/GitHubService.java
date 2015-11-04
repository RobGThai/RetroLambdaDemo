package com.github.api.service;

import com.squareup.okhttp.ResponseBody;

import retrofit.http.GET;
import rx.Observable;

public interface GitHubService {

    @GET("/zen")
    Observable<ResponseBody> zen();
}
