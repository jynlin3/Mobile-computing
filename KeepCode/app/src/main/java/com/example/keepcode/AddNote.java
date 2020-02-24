package com.example.keepcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import net.cryptobrewery.syntaxview.SyntaxView;

public class AddNote extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Toolbar toolbar;
    EditText noteTitle;
    Note note;
    ArrayList<Spinner> spinners;
    ArrayList<SyntaxView> codeViews;
    ArrayList<EditText> textViews;
    LinearLayout layout;
    static final String codePrefix = "<<code>>";
    static final String codeSuffix = "<</code>>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinners = new ArrayList<>();
        codeViews = new ArrayList<>();
        textViews = new ArrayList<>();
        noteTitle = findViewById(R.id.noteTitle);
        EditText noteDetails = findViewById(R.id.noteDetails);
        textViews.add(noteDetails);
        layout = findViewById(R.id.content);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fonts_names, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        note = (Note) getIntent().getSerializableExtra("note");
        if (savedInstanceState != null) {
            // Load title and content from the saved instance
            noteTitle.setText(savedInstanceState.getString("title"));
            getSupportActionBar().setTitle(savedInstanceState.getString("title"));

            // Create Code View and Text View according to the saved instance
            String[] contents = savedInstanceState.getString("content")
                    .split(codePrefix+"|"+codeSuffix);
            noteDetails.setText(contents[0]);
            for (int i = 1; i<contents.length;i+=2)
            {
                addCodeView();
                codeViews.get(i/2).getCode().setText(contents[i]);
                if(i+1 < contents.length)
                    textViews.get(i/2+1).setText(contents[i+1]);
            }

        }
        else if (note != null) {
            noteTitle.setText(note.getTitle());
            getSupportActionBar().setTitle(note.getTitle());

            // Create Code View and Text View according to the saved content
            String[] contents = note.getContent().split(codePrefix+"|"+codeSuffix);
            noteDetails.setText(contents[0]);
            for (int i = 1; i<contents.length;i+=2)
            {
                addCodeView();
                codeViews.get(i/2).getCode().setText(contents[i]);
                if(i+1 < contents.length)
                    textViews.get(i/2+1).setText(contents[i+1]);
            }
        }
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            Date currentTime = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            if (note==null) {
                // Add Note
                Note newNote = new Note(noteTitle.getText().toString(), getNoteDetails(),
                        dateFormat.format(currentTime), dateFormat.format(currentTime));
                NoteDatabase.getInstance(this).noteDao().insert(newNote);
            }
            else {
                // Update Note
                Note newNote = new Note(note.getId(), noteTitle.getText().toString(), getNoteDetails(),
                        note.getCreate_time(), dateFormat.format(currentTime));
                NoteDatabase.getInstance(this).noteDao().update(newNote);
            }
            goToMain();
        }
        if (item.getItemId() == R.id.delete)
        {
            if(note != null) {
                NoteDatabase.getInstance(this).noteDao().deleteByNoteId(note.getId());
                Toast.makeText(this, "Note: " + noteTitle.getText().toString() + " is deleted.", Toast.LENGTH_SHORT)
                        .show();
            }
            goToMain();
        }
        if(item.getItemId() == R.id.addCode)
        {
            addCodeView();
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private String getNoteDetails()
    {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < textViews.size(); i++)
        {
            sb.append(textViews.get(i).getText().toString());
            if(i<codeViews.size()) {
                sb.append(codePrefix);
                sb.append(codeViews.get(i).getCode().getText().toString());
                sb.append(codeSuffix);
            }
        }
        return sb.toString();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", noteTitle.getText().toString());
        outState.putString("content", getNoteDetails());
    }

    private void addCodeView()
    {
        //create spinner to choose programming language
        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Java");
        spinnerArray.add("C");
        spinnerArray.add("C++");
        spinnerArray.add("Python");
        spinnerArray.add("JavaScript");

        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new SpinnerActionListener(codeViews.size()));
        layout.addView(spinner);
        spinners.add(spinner);

        //create SyntaxView
        SyntaxView codeView = new SyntaxView(this);
        codeView.setAutoIndent(true);
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
        //configure multiline EditText
        textView.setBackground(null);
        textView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        textView.setMaxLines(4);
        textView.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        layout.addView(textView);
        textViews.add(textView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String fontName = (String) parent.getSelectedItem();
        String file = "";
        if (fontName.equals("Open Sans"))
        {
            file = "OpenSans-Regular.ttf";
        }
        if (fontName.equals("Inconsolata"))
        {
            file = "progfont.ttf";
        }
        if (fontName.equals("Roboto"))
        {
            file = "Roboto-Regular.ttf";
        }
        for (EditText tv : textViews )
        {
            Typeface tf = Typeface.createFromAsset(getAssets(), file);
            tv.setTypeface(tf);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private class SpinnerActionListener implements AdapterView.OnItemSelectedListener {

        private int index;

        public SpinnerActionListener(int index) {
            this.index = index;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedItem = parent.getItemAtPosition(position).toString();
            SyntaxView curCodeView = codeViews.get(index);
            // Update syntax highlighting for different languages
            String code = curCodeView.getCode().getText().toString();
            curCodeView.getCode().setText("");
            curCodeView.setLanguage(selectedItem);
            curCodeView.getCode().setText(code);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
