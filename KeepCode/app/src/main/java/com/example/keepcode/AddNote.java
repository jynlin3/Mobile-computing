package com.example.keepcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNote extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle;
    EditText noteDetails;
    int noteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteTitle = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails);
        noteID = -1;

        Note note = (Note) getIntent().getSerializableExtra("note");
        if (note != null) {
            noteTitle.setText(note.getTitle());
            noteDetails.setText(note.getContent());
            getSupportActionBar().setTitle(note.getTitle());
            noteID = note.getId();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save)
        {
            //TODO: Update note.
            Date currentTime = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            Note note = new Note(noteTitle.getText().toString(), noteDetails.getText().toString(),
                    dateFormat.format(currentTime), dateFormat.format(currentTime));
            NoteDatabase.getInstance(this).noteDao().insert(note);
            goToMain();
        }
        if (item.getItemId() == R.id.delete)
        {
            if(noteID != -1) {
                NoteDatabase.getInstance(this).noteDao().deleteByNoteId(noteID);
                Toast.makeText(this, "Note: " + noteTitle.getText().toString() + " is deleted.", Toast.LENGTH_SHORT)
                        .show();
            }
            goToMain();
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
