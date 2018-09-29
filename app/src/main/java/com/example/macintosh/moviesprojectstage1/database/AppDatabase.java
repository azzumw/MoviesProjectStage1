package com.example.macintosh.moviesprojectstage1.database;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

public abstract class AppDatabase extends RoomDatabase {
  private static final String LOG_TAG = AppDatabase.class.getCanonicalName();
  private static final Object LOCK = new Object();
  private static final String DATABASE_NAME = "favouritemovies";
  private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context) {
        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG,"Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,AppDatabase.DATABASE_NAME).build();
            }
        }

        Log.d(LOG_TAG,"Getting the database instance");
        return sInstance;
    }

    abstract MovieDao movieDao();
}
