package com.shaihi.note_app;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class, User.class}, version = 2)
public abstract class ContactsDatabase extends RoomDatabase {
    private static ContactsDatabase instance;

    public abstract ContactDAO contactDAO();
    public abstract UserDAO userDAO();

    public static synchronized ContactsDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ContactsDatabase.class, "contacts database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
