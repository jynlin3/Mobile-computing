package com.example.keepcode;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private String create_time;
    private String update_time;

    public Note(String title, String content, String create_time, String update_time) {
        this.title = title;
        this.content = content;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }
}
