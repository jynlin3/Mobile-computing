package com.example.keepcode;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteDatabase instance;
    private LiveData<List<Note>> liveDataNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        instance = NoteDatabase.getInstance(application);
        liveDataNotes = instance.noteDao().getAllNotes();
    }

    public LiveData<List<Note>> getLiveDataNotes() {
        return liveDataNotes;
    }
}
