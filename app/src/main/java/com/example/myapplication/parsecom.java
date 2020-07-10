package com.example.myapplication;

import android.app.Application;
import com.parse.Parse;

public class parsecom extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("KEY")
                .clientKey("ID")
                .enableLocalDataStore() // Активируем локальное хранилище для вопросов
                .server("https://parseapi.back4app.com/").build()
        );
    }
}
