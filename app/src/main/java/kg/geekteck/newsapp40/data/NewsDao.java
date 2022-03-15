package kg.geekteck.newsapp40.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kg.geekteck.newsapp40.models.News;

@Dao
public interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNews(News news);

    @Query("SELECT * FROM news")
    List<News> getAllNews();

    @Delete
    void deleteNews(News news);

    @Delete
    void delete(List<News> news);


}
