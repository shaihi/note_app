package com.shaihi.note_app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDAO {
    @Insert
    public void insert(Contact contact);

    @Delete
    void delete(Contact contact);

    // get the latest entry
    @Query("SELECT * FROM contacts ORDER BY id DESC LIMIT 1")
    Contact getLastContact();

    @Query("SELECT * FROM contacts")
    List<Contact> getAllContacts();
}
