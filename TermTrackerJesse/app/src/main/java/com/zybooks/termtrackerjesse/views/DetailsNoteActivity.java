package com.zybooks.termtrackerjesse.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zybooks.termtrackerjesse.Database.DataBaseHelper;
import com.zybooks.termtrackerjesse.R;
import com.zybooks.termtrackerjesse.models.Course;
import com.zybooks.termtrackerjesse.models.Note;

public class DetailsNoteActivity extends AppCompatActivity {
    MenuItem actionShare;
    TextView titleText, textTextView, courseText;
    Button editButton, shareButton;
    Note selectedNote;
    Course selectedCourse;
    int noteID;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        titleText = findViewById(R.id.titleText);
        textTextView = findViewById(R.id.textTextView);
        courseText = findViewById(R.id.courseText);
        editButton = findViewById(R.id.editButton);
        shareButton = findViewById(R.id.shareButton);
        actionShare = findViewById(R.id.action_share);

        Bundle extras = getIntent().getExtras();
        noteID = extras.getInt("NOTE_ID");

        dataBaseHelper = new DataBaseHelper(DetailsNoteActivity.this);
        selectedNote = dataBaseHelper.getOneNote(noteID);
        selectedCourse = dataBaseHelper.getOneCourse(selectedNote.getCourseID());

        titleText.setText(selectedNote.getTitle());
        textTextView.setText(selectedNote.getText());
        courseText.setText(selectedCourse.getTitle());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedNote.getId() > 0) {
                    Intent editNote = new Intent(DetailsNoteActivity.this, EditNoteActivity.class);
                    editNote.putExtra("NOTE_ID", selectedNote.getId());

                    startActivity(editNote);

                } else {
                    Toast.makeText(DetailsNoteActivity.this, "Error Finding Note", Toast.LENGTH_SHORT).show();
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedNote.getId() > 0) {
                    Intent editNote = new Intent(DetailsNoteActivity.this, ShareNoteActivity.class);
                    editNote.putExtra("NOTE_ID", selectedNote.getId());

                    startActivity(editNote);

                } else {
                    Toast.makeText(DetailsNoteActivity.this, "Error Finding Note", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);
        MenuItem share = menu.findItem(R.id.action_share);
        MenuItem home = menu.findItem(R.id.action_home);

        share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(DetailsNoteActivity.this, ShareNoteActivity.class);
                intent.putExtra("NOTE_ID", selectedNote.getId());
                startActivity(intent);
                return false;
            }
        });

        home.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(DetailsNoteActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }
}