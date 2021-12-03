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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zybooks.termtrackerjesse.Database.DataBaseHelper;
import com.zybooks.termtrackerjesse.R;
import com.zybooks.termtrackerjesse.adapter.AssessmentListAdapter;
import com.zybooks.termtrackerjesse.adapter.InstructorListAdapter;
import com.zybooks.termtrackerjesse.adapter.NoteListAdapter;
import com.zybooks.termtrackerjesse.models.Assessment;
import com.zybooks.termtrackerjesse.models.Course;
import com.zybooks.termtrackerjesse.models.Instructor;
import com.zybooks.termtrackerjesse.models.Note;
import com.zybooks.termtrackerjesse.models.Term;
import com.zybooks.termtrackerjesse.receivers.AlertReceiver;
import com.zybooks.termtrackerjesse.utility.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DetailsCourseActivity extends AppCompatActivity {
    Button editButton, addAssessmentButton, addNoteButton, addInstructorButton, reminderButton;
    TextView titleText, startText, endText, statusText, termText;
    ListView assessmentsListView, notesListView, instructorsListView;
    int courseID;
    Course selectedCourse;
    Term term;
    AssessmentListAdapter assessmentListAdapter;
    NoteListAdapter noteListAdapter;
    InstructorListAdapter instructorListAdapter;
    ArrayList<Assessment> assessments;
    ArrayList<Note> notes;
    ArrayList<Instructor> instructors;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editButton = findViewById(R.id.editButton);
        reminderButton = findViewById(R.id.reminderButton);
        addAssessmentButton = findViewById(R.id.addAssessmentButton);
        addNoteButton = findViewById(R.id.addNoteButton);
        addInstructorButton = findViewById(R.id.addInstructorButton);
        titleText = findViewById(R.id.titleText);
        startText = findViewById(R.id.startText);
        endText = findViewById(R.id.endText);
        statusText = findViewById(R.id.statusText);
        termText = findViewById(R.id.termText);
        assessmentsListView = findViewById(R.id.assessmentsListView);
        notesListView = findViewById(R.id.notesListView);
        instructorsListView = findViewById(R.id.instructorsListView);
        dataBaseHelper = new DataBaseHelper(DetailsCourseActivity.this);

        Bundle extras = getIntent().getExtras();
        courseID = extras.getInt("COURSE_ID");
        selectedCourse = dataBaseHelper.getOneCourse(courseID);
        term = dataBaseHelper.getOneTerm(selectedCourse.getTermID());

        titleText.setText(selectedCourse.getTitle());
        startText.setText(selectedCourse.getStartDate());
        endText.setText(selectedCourse.getEndDate());
        statusText.setText(selectedCourse.getStatus());
        termText.setText(term.getTitle());

        assessments = dataBaseHelper.getAssessmentsFromCourse(courseID);
        assessmentListAdapter = new AssessmentListAdapter(DetailsCourseActivity.this, R.layout.list_view_details, assessments);
        assessmentsListView.setAdapter(assessmentListAdapter);

        notes = dataBaseHelper.getNotesFromCourse(courseID);
        noteListAdapter = new NoteListAdapter(DetailsCourseActivity.this, R.layout.list_view_details, notes);
        notesListView.setAdapter(noteListAdapter);

        instructors = dataBaseHelper.getInstructorsFromCourse(courseID);
        instructorListAdapter = new InstructorListAdapter(DetailsCourseActivity.this, R.layout.list_view_details, instructors);
        instructorsListView.setAdapter(instructorListAdapter);

        Utility.setListViewHeightBasedOnChildren(assessmentsListView);
        Utility.setListViewHeightBasedOnChildren(notesListView);
        Utility.setListViewHeightBasedOnChildren(instructorsListView);

        assessmentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Assessment selectedAssessment = (Assessment) adapterView.getItemAtPosition(position);
                if(selectedAssessment.getId() > 0) {

                    Intent editCourse = new Intent(DetailsCourseActivity.this, DetailsAssessmentActivity.class);
                    editCourse.putExtra("ASSESSMENT_ID", selectedAssessment.getId());

                    startActivity(editCourse);

                } else {
                    Toast.makeText(DetailsCourseActivity.this, "Error Finding Term", Toast.LENGTH_SHORT).show();
                }
            }
        });

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Note selectedNote = (Note) adapterView.getItemAtPosition(position);
                if(selectedNote.getId() > 0) {

                    Intent editCourse = new Intent(DetailsCourseActivity.this, DetailsNoteActivity.class);
                    editCourse.putExtra("NOTE_ID", selectedNote.getId());

                    startActivity(editCourse);

                } else {
                    Toast.makeText(DetailsCourseActivity.this, "Error Finding Note", Toast.LENGTH_LONG).show();
                }
            }
        });

        instructorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Instructor selectedInstructor = (Instructor) adapterView.getItemAtPosition(position);
                if(selectedInstructor.getId() > 0) {

                    Intent editInstructor = new Intent(DetailsCourseActivity.this, EditInstructorActivity.class);
                    editInstructor.putExtra("INSTRUCTOR_ID", selectedInstructor.getId());

                    startActivity(editInstructor);

                } else {
                    Toast.makeText(DetailsCourseActivity.this, "Error Finding Instructor", Toast.LENGTH_LONG).show();
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedCourse.getId() > 0) {
                    Intent editCourseIntent = new Intent(DetailsCourseActivity.this, EditCourseActivity.class);
                    editCourseIntent.putExtra("COURSE_ID", selectedCourse.getId());

                    startActivity(editCourseIntent);

                } else {
                    Toast.makeText(DetailsCourseActivity.this, "Error Finding Course", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedCourse.getId() > 0) {
                    Intent editAssessmentActivity = new Intent(DetailsCourseActivity.this, EditAssessmentActivity.class);
                    editAssessmentActivity.putExtra("ASSESSMENT_ID", -1);

                    startActivity(editAssessmentActivity);

                } else {
                    Toast.makeText(DetailsCourseActivity.this, "Error Finding Course", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedCourse.getId() > 0) {
                    Intent editNoteActivity = new Intent(DetailsCourseActivity.this, EditNoteActivity.class);
                    editNoteActivity.putExtra("NOTE_ID", -1);

                    startActivity(editNoteActivity);

                } else {
                    Toast.makeText(DetailsCourseActivity.this, "Error Finding Note", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addInstructorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedCourse.getId() > 0) {
                    Intent editInstructorActivity = new Intent(DetailsCourseActivity.this, EditInstructorActivity.class);
                    editInstructorActivity.putExtra("INSTRUCTOR_ID", -1);

                    startActivity(editInstructorActivity);

                } else {
                    Toast.makeText(DetailsCourseActivity.this, "Error Finding Instructor", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedCourse.getStartDate() != null && selectedCourse.getEndDate() != null) {
                    Toast.makeText(DetailsCourseActivity.this, "Start and End Reminders Set", Toast.LENGTH_SHORT).show();

                    String startTitle = "Course Reminder";
                    String startText = selectedCourse.getTitle() + " starts today.";

                    Intent startIntent = new Intent(DetailsCourseActivity.this, AlertReceiver.class);
                    startIntent.putExtra("NOTIFICATION_TITLE", startTitle);
                    startIntent.putExtra("NOTIFICATION_TEXT", startText);
                    PendingIntent startPendingIntent = PendingIntent.getBroadcast(DetailsCourseActivity.this, 0, startIntent, 0);

                    AlarmManager startAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    String endTitle = "Course Reminder";
                    String endText = selectedCourse.getTitle() + " ends today.";

                    Intent endIntent = new Intent(DetailsCourseActivity.this, AlertReceiver.class);
                    endIntent.putExtra("NOTIFICATION_TITLE", endTitle);
                    endIntent.putExtra("NOTIFICATION_TEXT", endText);
                    PendingIntent endPendingIntent = PendingIntent.getBroadcast(DetailsCourseActivity.this, 1, endIntent, 0);

                    AlarmManager endAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    try {
                        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");

                        Date startDate = format.parse(selectedCourse.getStartDate());
                        Calendar startCalendar = Calendar.getInstance();
                        startCalendar.setTime(startDate);
                        startAlarmManager.set(AlarmManager.RTC_WAKEUP, startCalendar.getTimeInMillis(), startPendingIntent);

                        Date endDate = format.parse(selectedCourse.getEndDate());
                        Calendar endCalendar = Calendar.getInstance();
                        endCalendar.setTime(endDate);
                        endAlarmManager.set(AlarmManager.RTC_WAKEUP, endCalendar.getTimeInMillis(), endPendingIntent);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(DetailsCourseActivity.this, "Invalid start or end date, cannot set reminder", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(DetailsCourseActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }
}