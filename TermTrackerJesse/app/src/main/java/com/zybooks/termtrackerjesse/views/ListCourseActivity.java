package com.zybooks.termtrackerjesse.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zybooks.termtrackerjesse.Database.DataBaseHelper;
import com.zybooks.termtrackerjesse.R;
import com.zybooks.termtrackerjesse.adapter.CourseListAdapter;
import com.zybooks.termtrackerjesse.models.Course;
import com.zybooks.termtrackerjesse.utility.Utility;

import java.util.ArrayList;

public class ListCourseActivity extends AppCompatActivity {
    ListView coursesListView;
    CourseListAdapter courseListAdapter;

    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        coursesListView = findViewById(R.id.courses_list);

        dataBaseHelper = new DataBaseHelper(ListCourseActivity.this);

        ArrayList<Course> courses = dataBaseHelper.getAllCourses();
        courseListAdapter = new CourseListAdapter(ListCourseActivity.this, R.layout.list_view_details, courses);
        coursesListView.setAdapter(courseListAdapter);

        Utility.setListViewHeightBasedOnChildren(coursesListView);

        coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Course selectedCourse = (Course) adapterView.getItemAtPosition(position);
                if(selectedCourse.getId() > 0) {

                    Intent detailsCourse = new Intent(ListCourseActivity.this, DetailsCourseActivity.class);
                    detailsCourse.putExtra("COURSE_ID", selectedCourse.getId());

                    startActivity(detailsCourse);

                } else {
                    Toast.makeText(ListCourseActivity.this, "Error Finding Term", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(ListCourseActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.floatingActionButton:
                Intent createIntent = new Intent(ListCourseActivity.this, EditCourseActivity.class);
                createIntent.putExtra("COURSE_ID", -1);
                startActivity(createIntent);
                break;
            default:
                break;
        }
    }
}