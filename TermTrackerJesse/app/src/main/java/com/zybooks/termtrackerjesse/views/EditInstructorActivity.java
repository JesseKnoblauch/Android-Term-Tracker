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
import com.zybooks.termtrackerjesse.models.Instructor;

import java.util.ArrayList;

public class EditInstructorActivity extends AppCompatActivity {
    TextView editNameText, editPhoneText, editEmailText;
    Button deleteButton, saveButton;
    Spinner courseSpinner;
    ArrayAdapter courseAdapter;
    ArrayList<Course> courseList;
    int instructorID;
    Instructor selectedInstructor;
    Course selectedCourse;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_instructor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editNameText = findViewById(R.id.editNameText);
        editPhoneText = findViewById(R.id.editPhoneText);
        editEmailText = findViewById(R.id.editEmailText);
        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);
        courseSpinner = findViewById(R.id.courseSpinner);
        dataBaseHelper = new DataBaseHelper(EditInstructorActivity.this);

        Bundle extras = getIntent().getExtras();
        instructorID = extras.getInt("INSTRUCTOR_ID");
        selectedInstructor = dataBaseHelper.getOneInstructor(instructorID);
        selectedCourse = dataBaseHelper.getOneCourse(selectedInstructor.getCourseID());

        // COURSE SPINNER
        courseList = dataBaseHelper.getAllCourses();
        courseAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courseList);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseAdapter);

        // POPULATING FIELDS
        if(instructorID > 0) {
            setFields();
        } else {
            setTitle("Instructor Add");
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
                Instructor instructor;
                String instructorName = editNameText.getText().toString();
                String instructorPhone = editPhoneText.getText().toString();
                String instructorEmail = editEmailText.getText().toString();
                Course instructorCourse = (Course) courseSpinner.getSelectedItem();
                try {
                    dataBaseHelper = new DataBaseHelper(EditInstructorActivity.this);

                    if (instructorID > 0) {
                        instructor = new Instructor(instructorID, instructorName, instructorPhone, instructorEmail, instructorCourse.getId());
                        boolean success = dataBaseHelper.updateInstructor(instructor);
                        if (success) {
                            Toast.makeText(EditInstructorActivity.this, "Instructor Updated: " + instructorName, Toast.LENGTH_SHORT).show();
                            onSuccess(instructorCourse.getId());
                        }
                    } else {
                        instructor = new Instructor(1, instructorName, instructorPhone, instructorEmail, instructorCourse.getId());
                        int instructorID = (int) dataBaseHelper.addInstructor(instructor);
                        if (instructorID > 0) {
                            Toast.makeText(EditInstructorActivity.this, "Instructor Added: " + instructorName, Toast.LENGTH_SHORT).show();
                            onSuccess(instructorCourse.getId());
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(EditInstructorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper = new DataBaseHelper(EditInstructorActivity.this);

                boolean success = dataBaseHelper.deleteInstructor(selectedInstructor.getId());

                if (success) {
                    Toast.makeText(EditInstructorActivity.this, "Instructor Deleted: " + editNameText.getText(), Toast.LENGTH_SHORT).show();
                    Intent backToCourseIntent = new Intent(EditInstructorActivity.this, DetailsCourseActivity.class);
                    backToCourseIntent.putExtra("COURSE_ID", selectedInstructor.getCourseID());
                    startActivity(backToCourseIntent);
                } else {
                    Toast.makeText(EditInstructorActivity.this, "Failed to Delete Assessment: " + editNameText.getText(), Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(EditInstructorActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }

    public void setFields() {
        editNameText.setText(selectedInstructor.getName());
        editPhoneText.setText(selectedInstructor.getPhone());
        editEmailText.setText(selectedInstructor.getEmail());

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
        Intent editTerm = new Intent(EditInstructorActivity.this, DetailsCourseActivity.class);
        editTerm.putExtra("COURSE_ID", courseID);

        startActivity(editTerm);
    }
}