package name.isergius.freelance.protowpreader;

import java.io.Serializable;

/**
 * @author Sergey Kondratyev
 */

public class Article implements Serializable {

    private static final long serialVersionUID = 4422437925968029296L;

    private final long id;
    private final String title;
    private final String content;

    public Article(long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
