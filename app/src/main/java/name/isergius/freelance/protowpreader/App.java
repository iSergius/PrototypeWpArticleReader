package name.isergius.freelance.protowpreader;

import android.app.Application;
import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author Sergey Kondratyev
 */

public class App extends Application {

    private NanodexApi nanodexApi;

    public static App get(Context ctx) {
        return (App) ctx.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule("Article deserializer").addDeserializer(Article.class, new ArticleDeserializer()));
        OkHttpClient client = new OkHttpClient();
        client = client.newBuilder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://news.nanodex.com/wp-json/wp/v2/")
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        nanodexApi = retrofit.create(NanodexApi.class);
    }

    public NanodexApi getNanodexApi() {
        return nanodexApi;
    }
}
