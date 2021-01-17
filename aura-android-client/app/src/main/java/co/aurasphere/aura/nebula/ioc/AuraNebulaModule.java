package co.aurasphere.aura.nebula.ioc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.Calendar;

import javax.inject.Named;
import javax.inject.Singleton;

import co.aurasphere.aura.nebula.utils.CalendarSerializer;
import co.aurasphere.aura.nebula.utils.PropertiesReader;
import co.aurasphere.aura.nebula.utils.RetrofitHeaderInterceptor;
import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Donato on 23/05/2016.
 */
@Module
public class AuraNebulaModule {

    @Provides
    @Singleton
    public Retrofit getRetrofit(PropertiesReader propertiesReader, RetrofitHeaderInterceptor headerInterceptor){
        String auraRestBaseEndpoint = propertiesReader.getProperty("aura.rest.base.endpoint");

        // Configures the logging level.
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(logging);

        // Add an interceptor for shared headers.
        httpClient.networkInterceptors().add(headerInterceptor);

        // Register an adapter to manage the calendar serialization/deserialization.
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Calendar.class, new CalendarSerializer()).create();

        // Creates the retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(auraRestBaseEndpoint)
                .addConverterFactory(GsonConverterFactory.create(gson)).client(httpClient)
                .build();

        return retrofit;
    }

  /*  @Provides
    @Singleton
    public PropertiesReader getPropertiesReader(){
        return new PropertiesReader();
    }
*/
    @Provides
    @Singleton
    public RetrofitHeaderInterceptor getRetrofitHeaderInterceptor(@Named("aura.rest.jwt.token") String jwtToken){
        RetrofitHeaderInterceptor retrofitHeaderInterceptor = new RetrofitHeaderInterceptor();
        retrofitHeaderInterceptor.setJwtToken(jwtToken);
        return retrofitHeaderInterceptor;
    }

    @Provides
    @Named("aura.rest.jwt.token")
    public String getAuraRestJwtToken(PropertiesReader propertiesReader){
        return propertiesReader.getProperty("aura.rest.jwt.token");
    }

}
