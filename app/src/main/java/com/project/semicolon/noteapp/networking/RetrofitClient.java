package com.project.semicolon.noteapp.networking;

import android.content.Context;
import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.project.semicolon.noteapp.Constant;
import com.project.semicolon.noteapp.utils.SharedHelper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient;
    private static final int REQUEST_TIMEOUT = 60;
    private static final String BASE_URL = "https://demo.androidhive.info/";

    public static Retrofit getClient(Context ctx) {
        if (okHttpClient == null) {
            initHttpClient(ctx);
        }

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();


        }

        return retrofit;
    }

    private static void initHttpClient(final Context ctx) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);

        builder.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                requestBuilder.addHeader("Accept", "application/json");
                requestBuilder.addHeader("Content-Type", "application/json");
                String apiKey = SharedHelper.getFromPref(ctx, Constant.API_KEY, "");
                if (!TextUtils.isEmpty(apiKey))
                    requestBuilder.addHeader("Authorization", apiKey);
                return chain.proceed(requestBuilder.build());
            }

        });

        okHttpClient = builder.build();
    }
}
