package com.zybooks.termtrackerjesse.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.zybooks.termtrackerjesse.Database.DataBaseHelper;
import com.zybooks.termtrackerjesse.R;
import com.zybooks.termtrackerjesse.models.Assessment;
import com.zybooks.termtrackerjesse.models.Course;
import com.zybooks.termtrackerjesse.models.Term;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditCourseActivity extends AppCompatActivity {
    EditText editTitleText, editStartText, editEndText;
    Button saveButton, deleteButton, startButton, endButton;
    Spinner statusSpinner, termSpinner;
    Course selectedCourse;
    Term selectedTerm;
    List<Term> termList;
    ArrayAdapter<Term> termStatusAdapter;
    ArrayAdapter<String> statusDataAdapter;
    int courseID;
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        statusSpinner = findViewById(R.id.statusSpinner);
        startButton = findViewById(R.id.startButton);
        endButton = findViewById(R.id.endButton);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        editStartText = findViewById(R.id.editStartText);
        editTitleText = findViewById(R.id.editTitleText);
        editEndText = findViewById(R.id.editEndText);
        termSpinner = findViewById(R.id.termSpinner);
        dataBaseHelper = new DataBaseHelper(EditCourseActivity.this);

        Bundle extras = getIntent().getExtras();
        courseID = extras.getInt("COURSE_ID");
        selectedCourse = dataBaseHelper.getOneCourse(courseID);
        selectedTerm = dataBaseHelper.getOneTerm(selectedCourse.getTermID());

        // STATUS SPINNER
        List<String> status = new ArrayList<>();
        status.add("Plan to Take");
        status.add("Dropped");
        status.add("In Progress");
        status.add("Completed");
        statusDataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, status);
        statusDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusDataAdapter);

        // TERM SPINNER
        termList = dataBaseHelper.getAllTerms();
        termStatusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, termList);
        termStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termSpinner.setAdapter(termStatusAdapter);

        // FILLING FIELDS IF SELECTEDCOURSE EXISTS
        if(courseID > 0) {
            setFields();
        } else {
            setTitle("Course Add");
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

                Course newCourse;

                String newCourseTitle = editTitleText.getText().toString();
                String newCourseStart = editStartText.getText().toString();
                String newCourseEnd = editEndText.getText().toString();
                String newCourseStatus = String.valueOf(statusSpinner.getSelectedItem());
                Term newCourseTerm = (Term) termSpinner.getSelectedItem();

                try {
                    dataBaseHelper = new DataBaseHelper(EditCourseActivity.this);

                    if (courseID > 0) {
                        newCourse = new Course(courseID, newCourseTitle, newCourseStart, newCourseEnd, newCourseStatus, newCourseTerm.getId());
                        boolean success = dataBaseHelper.updateCourse(newCourse);
                        if (success) {
                            Toast.makeText(EditCourseActivity.this, "Course Updated: " + newCourseTitle, Toast.LENGTH_SHORT).show();
                            onSuccess(selectedCourse.getId());
                        }
                    } else {
                        newCourse = new Course(1, newCourseTitle, newCourseStart, newCourseEnd, newCourseStatus, newCourseTerm.getId());
                        int courseID = (int) dataBaseHelper.addCourse(newCourse);
                        if (courseID > 0) {
                            Toast.makeText(EditCourseActivity.this, "Course Added: " + newCourseTitle, Toast.LENGTH_SHORT).show();
                            onSuccess(courseID);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(EditCourseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                startYear = calendar.get(Calendar.YEAR);
                startMonth = calendar.get(Calendar.MONTH);
                startDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditCourseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                editStartText.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                            }
                        }, startYear, startMonth, startDay);
                datePickerDialog.show();
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                endYear = calendar.get(Calendar.YEAR);
                endMonth = calendar.get(Calendar.MONTH);
                endDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditCourseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                editEndText.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                            }
                        }, endYear, endMonth, endDay);
                datePickerDialog.show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(EditCourseActivity.this);

                if(!hasAssessment(courseID)) {
                    boolean success = dataBaseHelper.deleteCourse(courseID);
                    if (success) {
                        Toast.makeText(EditCourseActivity.this, "Course Deleted: " + editTitleText.getText(), Toast.LENGTH_SHORT).show();
                        Intent backToTermListIntent = new Intent(EditCourseActivity.this, ListTermsActivity.class);
                        startActivity(backToTermListIntent);
                    } else {
                        Toast.makeText(EditCourseActivity.this, "Failed to Delete Course: " + editTitleText.getText(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditCourseActivity.this, "Cannot delete course with attached assessments. Please delete course's assessments first.", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(EditCourseActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }

    private boolean hasAssessment(int courseID) {
        ArrayList<Assessment> assessments = dataBaseHelper.getAssessmentsFromCourse(courseID);
        if(assessments.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setFields() {
        editTitleText.setText(selectedCourse.getTitle());
        editStartText.setText(selectedCourse.getStartDate());
        editEndText.setText(selectedCourse.getEndDate());

        Term term = null;
        for(Term item : termList) {
            if(item.getId() == selectedTerm.getId()) {
                term = item;
            }
        }

        int statusPosition = statusDataAdapter.getPosition(selectedCourse.getStatus());
        int termPosition = termStatusAdapter.getPosition(term);
        statusSpinner.setSelection(statusPosition);
        termSpinner.setSelection(termPosition);
    }

    public void onSuccess(int courseID) {
        Intent editTerm = new Intent(EditCourseActivity.this, DetailsCourseActivity.class);
        editTerm.putExtra("COURSE_ID", courseID);

        startActivity(editTerm);
    }
}