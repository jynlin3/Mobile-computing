package com.example.keepcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.listOfNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, new ArrayList<Note>());
        recyclerView.setAdapter(adapter);
        NoteViewModel model = new ViewModelProvider(this).get(NoteViewModel.class);
        model.getLiveDataNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });
        PreferenceManager.setDefaultValues(this,
                R.xml.root_preferences, false);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(final MenuItem item) {
                for (int i = 0; i < menu.size(); ++i)
                {
                    MenuItem item2 = menu.getItem(i);
                    if(item2 != item) item2.setVisible(false);
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(final MenuItem item) {
                for (int i = 0; i < menu.size(); ++i)
                {
                    MenuItem item2 = menu.getItem(i);
                    if(item2 != item) item2.setVisible(true);
                }
                invalidateOptionsMenu();
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int result = 1;
        Intent i;
        switch (item.getItemId())
        {
            case R.id.app_bar_add:
                i = new Intent(this, AddNote.class);
                startActivityForResult(i, result);
                break;
            case R.id.app_bar_setting:
                i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, result);
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
