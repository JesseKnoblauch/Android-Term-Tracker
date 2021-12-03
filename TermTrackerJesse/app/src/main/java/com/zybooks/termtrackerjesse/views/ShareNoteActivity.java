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
import android.widget.EditText;
import android.widget.TextView;

import com.zybooks.termtrackerjesse.Database.DataBaseHelper;
import com.zybooks.termtrackerjesse.R;
import com.zybooks.termtrackerjesse.models.Course;
import com.zybooks.termtrackerjesse.models.Note;

public class ShareNoteActivity extends AppCompatActivity {
    Button shareButton;
    EditText editText;
    TextView titleText, textTextView, courseText;
    int noteID;
    Note selectedNote;
    Course selectedCourse;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editText = findViewById(R.id.editText);
        shareButton = findViewById(R.id.shareButton);
        titleText = findViewById(R.id.titleText);
        textTextView = findViewById(R.id.textTextView);
        courseText = findViewById(R.id.courseText);
        dataBaseHelper = new DataBaseHelper(ShareNoteActivity.this);

        Bundle extras = getIntent().getExtras();
        noteID = extras.getInt("NOTE_ID");

        selectedNote = dataBaseHelper.getOneNote(noteID);
        selectedCourse = dataBaseHelper.getOneCourse(selectedNote.getCourseID());

        titleText.setText(selectedNote.getTitle());
        textTextView.setText(selectedNote.getText());
        courseText.setText(selectedCourse.getTitle());

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/html");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, editText.getText().toString());
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, selectedNote.getTitle());
                emailIntent.putExtra(Intent.EXTRA_TEXT, selectedNote.getText());

                startActivity(Intent.createChooser(emailIntent, "Send Email"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem home = menu.findItem(R.id.action_home);

        home.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(ShareNoteActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }
}