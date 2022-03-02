package kg.geekteck.newsapp40;

import android.app.Application;

import androidx.room.Room;

import kg.geekteck.newsapp40.data.AppDataBase;

public class App extends Application {
    public static AppDataBase dataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        dataBase= Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class, "news-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
