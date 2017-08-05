package name.isergius.freelance.protowpreader;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Kondratyev
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder> {

    private static final String TAG = ArticleAdapter.class.getSimpleName();

    private List<Article> articles = new ArrayList<>();

    public void setArticles(List<Article> articles) {
        Log.i(TAG, "setArticles: " + articles);
        this.articles = articles;
        notifyDataSetChanged();
    }

    @Override
    public ArticleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_article, null);
        view.setClickable(true);
        return new ArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleHolder holder, int position) {
        Article article = articles.get(position);
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class ArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        private Article article;

        public ArticleHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = itemView.findViewById(R.id.item_article_title);
        }

        public void bind(Article article) {
            this.article = article;
            textView.setText(article.getTitle());
        }

        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: " + article);

        }
    }
}
