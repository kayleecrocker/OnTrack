package com.kayleecrocker.ontrack.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kayleecrocker.ontrack.dao.TaskDao;
import com.kayleecrocker.ontrack.entities.Task;

@Database(entities = {Task.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "ontrack_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
