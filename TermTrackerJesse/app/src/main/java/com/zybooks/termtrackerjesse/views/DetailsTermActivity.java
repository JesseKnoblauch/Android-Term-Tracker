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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zybooks.termtrackerjesse.Database.DataBaseHelper;
import com.zybooks.termtrackerjesse.R;
import com.zybooks.termtrackerjesse.adapter.CourseListAdapter;
import com.zybooks.termtrackerjesse.models.Course;
import com.zybooks.termtrackerjesse.models.Term;
import com.zybooks.termtrackerjesse.utility.Utility;

import java.util.ArrayList;

public class DetailsTermActivity extends AppCompatActivity {
    TextView titleText, startText, endText;
    Button editButton;
    ListView coursesListView;
    Term selectedTerm;
    int termID;
    CourseListAdapter courseListAdapter;
    DataBaseHelper dataBaseHelper;
    ArrayList<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_term);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        titleText = findViewById(R.id.titleText);
        startText = findViewById(R.id.startText);
        endText = findViewById(R.id.endText);
        editButton = findViewById(R.id.editButton);
        coursesListView = findViewById(R.id.coursesListView);

        Bundle extras = getIntent().getExtras();
        termID = extras.getInt("TERM_ID");

        dataBaseHelper = new DataBaseHelper(DetailsTermActivity.this);
        selectedTerm = dataBaseHelper.getOneTerm(termID);

        titleText.setText(selectedTerm.getTitle());
        startText.setText(selectedTerm.getStartDate());
        endText.setText(selectedTerm.getEndDate());

        courses = dataBaseHelper.getCoursesFromTerm(termID);
        courseListAdapter = new CourseListAdapter(DetailsTermActivity.this, R.layout.list_view_details, courses);
        coursesListView.setAdapter(courseListAdapter);

        Utility.setListViewHeightBasedOnChildren(coursesListView);

        coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Course selectedCourse = (Course) adapterView.getItemAtPosition(position);
                if(selectedCourse.getId() > 0) {

                    Intent editCourse = new Intent(DetailsTermActivity.this, DetailsCourseActivity.class);
                    editCourse.putExtra("COURSE_ID", selectedCourse.getId());

                    startActivity(editCourse);

                } else {
                    Toast.makeText(DetailsTermActivity.this, "Error Finding Term", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedTerm.getId() > 0) {
                    Intent editTerm = new Intent(DetailsTermActivity.this, EditTermActivity.class);
                    editTerm.putExtra("TERM_ID", selectedTerm.getId());

                    startActivity(editTerm);

                } else {
                    Toast.makeText(DetailsTermActivity.this, "Error Finding Term", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(DetailsTermActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }
}