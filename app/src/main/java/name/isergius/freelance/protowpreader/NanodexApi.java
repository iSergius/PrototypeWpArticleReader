package name.isergius.freelance.protowpreader;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Sergey Kondratyev
 */

public interface NanodexApi {

    @GET("posts")
    Flowable<List<Article>> getAll(@Query("categories") int cat);
}
