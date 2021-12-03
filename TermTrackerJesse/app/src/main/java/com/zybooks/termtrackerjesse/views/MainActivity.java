package com.zybooks.termtrackerjesse.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zybooks.termtrackerjesse.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button termButton, courseButton, assessmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        termButton = findViewById(R.id.terms_button);
        courseButton = findViewById(R.id.courses_button);
        assessmentButton = findViewById(R.id.assessments_button);
        termButton.setOnClickListener(this);
        courseButton.setOnClickListener(this);
        assessmentButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.terms_button:
                Intent termIntent = new Intent(MainActivity.this, ListTermsActivity.class);
                startActivity(termIntent);
                break;
            case R.id.courses_button:
                Intent courseButton = new Intent(MainActivity.this, ListCourseActivity.class);
                startActivity(courseButton);
                break;
            case R.id.assessments_button:
                Intent assessmentButton = new Intent(MainActivity.this, ListAssessmentActivity.class);
                startActivity(assessmentButton);
                break;
            default:
                break;
        }
    }
}