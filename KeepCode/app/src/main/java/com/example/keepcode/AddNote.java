package com.example.keepcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import net.cryptobrewery.syntaxview.SyntaxView;

public class AddNote extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle;
    int noteID;
    ArrayList<SyntaxView> codeViews;
    ArrayList<EditText> textViews;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        codeViews = new ArrayList<>();
        textViews = new ArrayList<>();
        noteTitle = findViewById(R.id.noteTitle);
        EditText noteDetails = findViewById(R.id.noteDetails);
        textViews.add(noteDetails);
        noteID = -1;
        layout = findViewById(R.id.content);

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
            //TODO: Save content of textviews and codeviews
            Note note = new Note(noteTitle.getText().toString(), "",//noteDetails.getText().toString(),
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
        if(item.getItemId() == R.id.addCode)
        {
            //create SyntaxView
            SyntaxView codeView = new SyntaxView(this);
            codeView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.addView(codeView);
            codeViews.add(codeView);

            //create EditText
            EditText textView = new EditText(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //set margins of EditText with dp
            int dpValue = 16;
            float d = this.getResources().getDisplayMetrics().density;
            int margin = (int) (dpValue * d);
            lp.setMargins(margin,margin, margin, margin);
            textView.setLayoutParams(lp);
            textView.setBackground(null);
            textView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            textView.setMaxLines(4);
            textView.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);

            layout.addView(textView);
            textViews.add(textView);

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
