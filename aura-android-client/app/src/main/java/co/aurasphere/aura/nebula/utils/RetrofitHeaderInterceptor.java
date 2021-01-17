package co.aurasphere.aura.nebula.utils;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Class which intercepts a Retrofit request and adds some headers.
 * Created by Donato on 27/05/2016.
 */
public class RetrofitHeaderInterceptor implements Interceptor {

    @Inject
    @Named("aura.rest.jwt.token")
    String JWT_TOKEN;

    private final static String TAG = "HeaderInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();

        // Adds some headers.
        requestBuilder.addHeader("Accept", "application/json");
        requestBuilder.addHeader("Content-Type", "application/json;charset=utf-8");
        requestBuilder.addHeader("Authorization", "Bearer " + JWT_TOKEN);
        requestBuilder.addHeader("Connection", "Keep-Alive");
        requestBuilder.addHeader("Keep-Alive", "timeout=60, max=10");

        Log.d(TAG, "Added JWT Token: " + JWT_TOKEN);

        // Chains the request.
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    public void setJwtToken(String jwtToken) {
        this.JWT_TOKEN = jwtToken;
    }
}
