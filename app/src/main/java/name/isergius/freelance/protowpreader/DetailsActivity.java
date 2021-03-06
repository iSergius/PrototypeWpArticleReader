package name.isergius.freelance.protowpreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

/**
 * @author Sergey Kondratyev
 */

public class DetailsActivity extends AppCompatActivity {

    private static final String ARTICLE = "article";
    private WebView content;

    public static void startActivity(Context context, Article article) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(ARTICLE, article);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_details_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Article article = (Article) getIntent().getExtras().getSerializable(ARTICLE);
        content = (WebView) findViewById(R.id.activity_details_content);
        content.loadData(article.getContent(), "text/html", "UTF-16");
    }
}
