package com.github.api.retrofit.converter;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit.Converter;

public class ToStringFactory extends Converter.Factory {
    @Override
    public Converter<ResponseBody, ?> fromResponseBody(Type type, Annotation[] annotations) {
        return new StringResponseConverter();
    }

    @Override
    public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
        return new StringRequestConverter();
    }

    final class StringResponseConverter implements Converter<ResponseBody, String> {
        @Override public String convert(ResponseBody body) throws IOException {
            return body.string();
        }
    }

    final class StringRequestConverter implements Converter<String, RequestBody> {
        @Override public RequestBody convert(String value) throws IOException {
            return RequestBody.create(
                    MediaType.parse("text/plain"),
                    value);
        }
    }
}