package com.robgthai.demo.retrolambda.dagger;

import com.github.api.service.ServiceBuilder;
import com.github.api.service.ZenService;

import dagger.Component;
import retrofit.Retrofit;

/**
 * Created by RobGThai on 11/7/15.
 */
@Component(modules = ServiceBuilder.class)
public interface ZenComponent {
    ZenService createService();
    Retrofit.Builder createBuilder();
}
