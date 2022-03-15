package kg.geekteck.newsapp40.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import kg.geekteck.newsapp40.models.News;

@Database(entities = {News.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract NewsDao newsDao();
}
