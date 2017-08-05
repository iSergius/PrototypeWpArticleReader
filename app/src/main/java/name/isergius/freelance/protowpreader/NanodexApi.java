package name.isergius.freelance.protowpreader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Sergey Kondratyev
 */

public interface NanodexApi {

    static NanodexApi create() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule("Article deserializer").addDeserializer(Article.class, new ArticleDeserializer()));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://news.nanodex.com/wp-json/wp/v2/")
                .client(new OkHttpClient())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(NanodexApi.class);
    }

    @GET("/posts?categories={:category}")
    Flowable<List<Article>> getAll(@Query("category") int cat);
}
