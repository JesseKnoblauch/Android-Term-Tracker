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
import android.widget.ListView;
import android.widget.Toast;

import com.zybooks.termtrackerjesse.Database.DataBaseHelper;
import com.zybooks.termtrackerjesse.R;
import com.zybooks.termtrackerjesse.adapter.AssessmentListAdapter;
import com.zybooks.termtrackerjesse.models.Assessment;
import com.zybooks.termtrackerjesse.utility.Utility;

import java.util.ArrayList;

public class ListAssessmentActivity extends AppCompatActivity {
    private ListView assessmentsListView;
    AssessmentListAdapter assessmentListAdapter;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        assessmentsListView = findViewById(R.id.assessments_list);
        dataBaseHelper = new DataBaseHelper(ListAssessmentActivity.this);

        ArrayList<Assessment> assessments = dataBaseHelper.getAllAssessments();
        assessmentListAdapter = new AssessmentListAdapter(this, R.layout.list_view_details, assessments);
        assessmentsListView.setAdapter(assessmentListAdapter);

        Utility.setListViewHeightBasedOnChildren(assessmentsListView);

        assessmentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Assessment selectedAssessment = (Assessment) adapterView.getItemAtPosition(position);
                if(selectedAssessment.getId() > 0) {

                    Intent detailsAssessment = new Intent(ListAssessmentActivity.this, DetailsAssessmentActivity.class);
                    detailsAssessment.putExtra("ASSESSMENT_ID", selectedAssessment.getId());

                    startActivity(detailsAssessment);

                } else {
                    Toast.makeText(ListAssessmentActivity.this, "Error Finding Assessment", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(ListAssessmentActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.floatingActionButton:
                Intent createIntent = new Intent(ListAssessmentActivity.this, EditAssessmentActivity.class);
                createIntent.putExtra("ASSESSMENT_ID", -1);
                startActivity(createIntent);
                break;
            default:
                break;
        }
    }
}