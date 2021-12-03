package com.zybooks.termtrackerjesse.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zybooks.termtrackerjesse.Database.DataBaseHelper;
import com.zybooks.termtrackerjesse.R;
import com.zybooks.termtrackerjesse.models.Course;
import com.zybooks.termtrackerjesse.models.Note;

import java.util.ArrayList;

public class EditNoteActivity extends AppCompatActivity {
    TextView editTitleText, editTextText;
    Button deleteButton, saveButton;
    Spinner courseSpinner;
    ArrayAdapter courseAdapter;
    ArrayList<Course> courseList;
    int noteID;
    Note selectedNote;
    Course selectedCourse;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTitleText = findViewById(R.id.editTitleText);
        editTextText = findViewById(R.id.editTextText);
        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);
        courseSpinner = findViewById(R.id.courseSpinner);
        dataBaseHelper = new DataBaseHelper(EditNoteActivity.this);

        Bundle extras = getIntent().getExtras();
        noteID = extras.getInt("NOTE_ID");
        selectedNote = dataBaseHelper.getOneNote(noteID);
        selectedCourse = dataBaseHelper.getOneCourse(selectedNote.getCourseID());

        // COURSE SPINNER
        courseList = dataBaseHelper.getAllCourses();
        courseAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courseList);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseAdapter);

        // POPULATING FIELDS
        if(noteID > 0) {
            setFields();
        } else {
            setTitle("Note Add");
            View deleteButton = findViewById(R.id.deleteButton);
            ((ViewGroup) deleteButton.getParent()).removeView(deleteButton);
            ConstraintLayout constraintLayout = findViewById(R.id.editContainer);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.saveButton, ConstraintSet.START, R.id.editContainer, ConstraintSet.START, 0);
            constraintSet.connect(R.id.saveButton, ConstraintSet.END, R.id.editContainer, ConstraintSet.END, 0);
            constraintSet.connect(R.id.saveButton, ConstraintSet.TOP, R.id.tableLayout, ConstraintSet.BOTTOM, 0);
            constraintSet.applyTo(constraintLayout);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note;
                String noteTitle = editTitleText.getText().toString();
                String noteText = editTextText.getText().toString();
                Course noteCourse = (Course) courseSpinner.getSelectedItem();
                try {
                    dataBaseHelper = new DataBaseHelper(EditNoteActivity.this);

                    if (noteID > 0) {
                        note = new Note(noteID, noteTitle, noteText, noteCourse.getId());
                        System.out.println(note);
                        boolean success = dataBaseHelper.updateNote(note);
                        if (success) {
                            Toast.makeText(EditNoteActivity.this, "Assessment Updated: " + noteTitle, Toast.LENGTH_SHORT).show();
                            onSuccess(noteCourse.getId());
                        }
                    } else {
                        note = new Note(1, noteTitle, noteText, noteCourse.getId());
                        int noteID = (int) dataBaseHelper.addNote(note);
                        if (noteID > 0) {
                            Toast.makeText(EditNoteActivity.this, "Assessment Added: " + noteTitle, Toast.LENGTH_SHORT).show();
                            onSuccess(noteCourse.getId());
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(EditNoteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper = new DataBaseHelper(EditNoteActivity.this);

                boolean success = dataBaseHelper.deleteNote(selectedNote.getId());

                if (success) {
                    Toast.makeText(EditNoteActivity.this, "Note Deleted: " + editTextText.getText(), Toast.LENGTH_SHORT).show();
                    Intent backToCourseIntent = new Intent(EditNoteActivity.this, DetailsCourseActivity.class);
                    backToCourseIntent.putExtra("COURSE_ID", selectedNote.getCourseID());
                    startActivity(backToCourseIntent);
                } else {
                    Toast.makeText(EditNoteActivity.this, "Failed to Delete Note: " + editTextText.getText(), Toast.LENGTH_SHORT).show();
                }
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
                Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }

    public void setFields() {
        editTitleText.setText(selectedNote.getTitle());
        editTextText.setText(selectedNote.getText());

        Course course = null;
        for(Course item : courseList) {
            if(item.getId() == selectedCourse.getId()) {
                course = item;
            }
        }

        int coursePosition = courseAdapter.getPosition(course);
        courseSpinner.setSelection(coursePosition);
    }

    public void onSuccess(int courseID) {
        Intent editTerm = new Intent(EditNoteActivity.this, DetailsCourseActivity.class);
        editTerm.putExtra("COURSE_ID", courseID);

        startActivity(editTerm);
    }
}