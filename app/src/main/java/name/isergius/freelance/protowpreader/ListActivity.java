package name.isergius.freelance.protowpreader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * @author Sergey Kondratyev
 */

public class ListActivity extends AppCompatActivity {

    private ArticleAdapter articleAdapter;
    private NanodexApi nanodexApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Flowable<List<Article>> cache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolBar = (Toolbar) findViewById(R.id.activity_list_toolbar);
        setSupportActionBar(toolBar);
        nanodexApi = App.get(this)
                .getNanodexApi();
        initList();
        initCache();
        initSearch();
    }

    private void initList() {
        articleAdapter = new ArticleAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_list_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(articleAdapter);
    }

    private void initCache() {
        cache = nanodexApi.getAll(5)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .cache();
        Disposable disposable = cache.doOnNext(new Consumer<List<Article>>() {
            @Override
            public void accept(@NonNull List<Article> articles) throws Exception {
                articleAdapter.setArticles(articles);
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        }).subscribe();
        compositeDisposable.add(disposable);
    }

    private void initSearch() {
        final TextView search = (TextView) findViewById(R.id.activity_list_search_edit);
        EditTextPublisher textPublisher = new EditTextPublisher();
        search.addTextChangedListener(textPublisher);
        Disposable disposable = Flowable.fromPublisher(textPublisher)
                .debounce(200, TimeUnit.MILLISECONDS)
                .flatMap(new Function<CharSequence, Publisher<List<Article>>>() {
                    @Override
                    public Publisher<List<Article>> apply(@NonNull final CharSequence words) throws Exception {
                        return cache.flatMap(new Function<List<Article>, Publisher<Article>>() {
                            @Override
                            public Publisher<Article> apply(@NonNull List<Article> articles) throws Exception {
                                return Flowable.fromIterable(articles);
                            }
                        }).filter(new Predicate<Article>() {
                            @Override
                            public boolean test(@NonNull Article article) throws Exception {
                                String title = article.getTitle().toLowerCase();
                                String keywords = words.toString().toLowerCase();
                                return title.contains(keywords);
                            }
                        }).collect(new Callable<List<Article>>() {
                            @Override
                            public List<Article> call() throws Exception {
                                return new ArrayList<>();
                            }
                        }, new BiConsumer<List<Article>, Article>() {
                            @Override
                            public void accept(List<Article> articles, Article article) throws Exception {
                                articles.add(article);
                            }
                        }).toFlowable();
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<List<Article>>() {
                    @Override
                    public void accept(@NonNull List<Article> articles) throws Exception {
                        articleAdapter.setArticles(articles);
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }).subscribe();
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
