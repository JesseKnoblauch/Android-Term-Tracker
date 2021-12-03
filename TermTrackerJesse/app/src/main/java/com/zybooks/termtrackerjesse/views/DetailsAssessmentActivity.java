package com.zybooks.termtrackerjesse.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import com.zybooks.termtrackerjesse.models.Assessment;
import com.zybooks.termtrackerjesse.models.Course;
import com.zybooks.termtrackerjesse.receivers.AlertReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailsAssessmentActivity extends AppCompatActivity {
    Button editButton, reminderButton;
    TextView titleText, dateText, typeText, courseText;
    Assessment selectedAssessment;
    Course course;
    int assessmentID;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_assessment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editButton = findViewById(R.id.editButton);
        reminderButton = findViewById(R.id.reminderButton);
        titleText = findViewById(R.id.titleText);
        dateText = findViewById(R.id.dateText);
        typeText = findViewById(R.id.typeText);
        courseText = findViewById(R.id.courseText);
        dataBaseHelper = new DataBaseHelper(DetailsAssessmentActivity.this);

        Bundle extras = getIntent().getExtras();
        assessmentID = extras.getInt("ASSESSMENT_ID");
        selectedAssessment = dataBaseHelper.getOneAssessment(assessmentID);
        course = dataBaseHelper.getOneCourse(selectedAssessment.getCourseID());

        titleText.setText(selectedAssessment.getTitle());
        dateText.setText(selectedAssessment.getDate());
        typeText.setText(selectedAssessment.getType());
        courseText.setText(course.getTitle());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedAssessment.getId() > 0) {
                    Intent editAssessmentIntent = new Intent(DetailsAssessmentActivity.this, EditAssessmentActivity.class);
                    editAssessmentIntent.putExtra("ASSESSMENT_ID", selectedAssessment.getId());

                    startActivity(editAssessmentIntent);

                } else {
                    Toast.makeText(DetailsAssessmentActivity.this, "Error Finding Assessment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedAssessment.getDate() != null && selectedAssessment.getDate() != null) {
                    Toast.makeText(DetailsAssessmentActivity.this, "Assessment Date Reminder Set", Toast.LENGTH_SHORT).show();

                    String startTitle = "Assessment Reminder";
                    String startText = selectedAssessment.getTitle() + " is today.";

                    Intent startIntent = new Intent(DetailsAssessmentActivity.this, AlertReceiver.class);
                    startIntent.putExtra("NOTIFICATION_TITLE", startTitle);
                    startIntent.putExtra("NOTIFICATION_TEXT", startText);
                    PendingIntent startPendingIntent = PendingIntent.getBroadcast(DetailsAssessmentActivity.this, 2, startIntent, 0);

                    AlarmManager startAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    try {
                        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
                        //assessment alarm
                        Date assessmentDate = format.parse(selectedAssessment.getDate());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(assessmentDate);
                        startAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), startPendingIntent);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(DetailsAssessmentActivity.this, "Invalid date, cannot set reminder", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(DetailsAssessmentActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }
}