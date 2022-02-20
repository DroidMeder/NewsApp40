package kg.geekteck.newsapp40.ui.models;

import java.io.Serializable;

public class News implements Serializable {
    private String title;
    private long createdAt;

    public News(String title, long createdAt) {
        setTitle(title);
        setCreatedAt(createdAt);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
