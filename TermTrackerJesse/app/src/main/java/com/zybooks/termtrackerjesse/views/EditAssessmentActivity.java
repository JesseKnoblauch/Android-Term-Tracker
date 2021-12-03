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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zybooks.termtrackerjesse.Database.DataBaseHelper;
import com.zybooks.termtrackerjesse.R;
import com.zybooks.termtrackerjesse.models.Assessment;
import com.zybooks.termtrackerjesse.models.Course;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditAssessmentActivity extends AppCompatActivity {
    TextView editDateText, editTitleText;
    Button deleteButton, saveButton, dateButton;
    Spinner courseSpinner, typeSpinner;
    ArrayAdapter courseAdapter, typeAdapter;
    ArrayList<Course> courseList;
    ArrayList<String> typeList;
    int assessmentID;
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;
    Assessment selectedAssessment;
    Course selectedCourse;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTitleText = findViewById(R.id.editTitleText);
        editDateText = findViewById(R.id.editDateText);
        courseSpinner = findViewById(R.id.courseSpinner);
        typeSpinner = findViewById(R.id.typeSpinner);
        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);
        dateButton = findViewById(R.id.dateButton);
        dataBaseHelper = new DataBaseHelper(EditAssessmentActivity.this);

        Bundle extras = getIntent().getExtras();
        assessmentID = extras.getInt("ASSESSMENT_ID");
        selectedAssessment = dataBaseHelper.getOneAssessment(assessmentID);
        selectedCourse = dataBaseHelper.getOneCourse(selectedAssessment.getCourseID());

        // TYPE SPINNER
        ArrayList<String> typeList = new ArrayList<>();
        typeList.add("Objective Assessment");
        typeList.add("Performance Assessment");
        typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        // COURSE SPINNER
        courseList = dataBaseHelper.getAllCourses();
        courseAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courseList);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseAdapter);

        // POPULATING FIELDS
        if(assessmentID > 0) {
            setFields();
        } else {
            setTitle("Assessment Add");
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

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                startYear = calendar.get(Calendar.YEAR);
                startMonth = calendar.get(Calendar.MONTH);
                startDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditAssessmentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                editDateText.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                            }
                        }, startYear, startMonth, startDay);
                datePickerDialog.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Assessment assessment;
                String assessmentTitle = editTitleText.getText().toString();
                String assessmentDate = editDateText.getText().toString();
                String assessmentType = typeSpinner.getSelectedItem().toString();
                Course assessmentCourse = (Course) courseSpinner.getSelectedItem();
                try {
                    dataBaseHelper = new DataBaseHelper(EditAssessmentActivity.this);

                    if (assessmentID > 0) {
                        assessment = new Assessment(assessmentID, assessmentTitle, assessmentDate, assessmentType, assessmentCourse.getId());
                        boolean success = dataBaseHelper.updateAssessment(assessment);
                        if (success) {
                            Toast.makeText(EditAssessmentActivity.this, "Assessment Updated: " + assessmentTitle, Toast.LENGTH_SHORT).show();
                            onSuccess(selectedCourse.getId());
                        }
                    } else {
                        assessment = new Assessment(1, assessmentTitle, assessmentDate, assessmentType, assessmentCourse.getId());
                        int assessmentID = (int) dataBaseHelper.addAssessment(assessment);
                        if (assessmentID > 0) {
                            Toast.makeText(EditAssessmentActivity.this, "Assessment Added: " + assessmentTitle, Toast.LENGTH_SHORT).show();
                            onSuccess(assessmentID);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(EditAssessmentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(EditAssessmentActivity.this);

                boolean success = dataBaseHelper.deleteAssessment(assessmentID);

                if (success) {
                    Toast.makeText(EditAssessmentActivity.this, "Assessment Deleted: " + editTitleText.getText(), Toast.LENGTH_SHORT).show();
                    Intent backToTermListIntent = new Intent(EditAssessmentActivity.this, ListAssessmentActivity.class);
                    startActivity(backToTermListIntent);
                } else {
                    Toast.makeText(EditAssessmentActivity.this, "Failed to Delete Assessment: " + editTitleText.getText(), Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(EditAssessmentActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }

    public void setFields() {
        editTitleText.setText(selectedAssessment.getTitle());
        editDateText.setText(selectedAssessment.getDate());

        Course course = null;
        for(Course item : courseList) {
            if(item.getId() == selectedCourse.getId()) {
                course = item;
            }
        }

        int typePosition = typeAdapter.getPosition(selectedAssessment.getType());
        typeSpinner.setSelection(typePosition);
        int coursePosition = courseAdapter.getPosition(course);
        courseSpinner.setSelection(coursePosition);
    }

    public void onSuccess(int assessmentID) {
        Intent editTerm = new Intent(EditAssessmentActivity.this, DetailsAssessmentActivity.class);
        editTerm.putExtra("ASSESSMENT_ID", assessmentID);

        startActivity(editTerm);
    }
}