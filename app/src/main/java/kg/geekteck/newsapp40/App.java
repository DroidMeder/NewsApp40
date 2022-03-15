package kg.geekteck.newsapp40;

import android.app.Application;

import androidx.room.Room;

import kg.geekteck.newsapp40.data.AppDataBase;
import kg.geekteck.newsapp40.helper.AppSignatureHelper;

public class App extends Application {
    public static AppDataBase dataBase;
    //public static AppSignatureHelper appSignatureHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        dataBase= Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class, "news-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

       /* appSignatureHelper = new AppSignatureHelper(this);
        appSignatureHelper.getAppSignatures();*/
    }
}
