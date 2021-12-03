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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.zybooks.termtrackerjesse.Database.DataBaseHelper;
import com.zybooks.termtrackerjesse.R;
import com.zybooks.termtrackerjesse.models.Course;
import com.zybooks.termtrackerjesse.models.Term;

import java.util.ArrayList;
import java.util.Calendar;

public class EditTermActivity extends AppCompatActivity {
    EditText editTitleText, editStartText, editEndText;
    Button saveButton, deleteButton, startButton, endButton;
    Term selectedTerm;
    int termID;
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTitleText = findViewById(R.id.editTitleText);
        editStartText = findViewById(R.id.editStartText);
        editEndText = findViewById(R.id.editEndText);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        startButton = findViewById(R.id.startButton);
        endButton = findViewById(R.id.endButton);

        Bundle extras = getIntent().getExtras();
        termID = extras.getInt("TERM_ID");

        dataBaseHelper = new DataBaseHelper(EditTermActivity.this);
        selectedTerm = dataBaseHelper.getOneTerm(termID);

        if (selectedTerm.getId() > 0) {
            editTitleText.setText(selectedTerm.getTitle());
            editStartText.setText(selectedTerm.getStartDate());
            editEndText.setText(selectedTerm.getEndDate());
        } else {
            setTitle("Term Add");
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

                Term newTerm;

                String newTermTitle = editTitleText.getText().toString();
                String newTermStart = editStartText.getText().toString();
                String newTermEnd = editEndText.getText().toString();

                try {
                    dataBaseHelper = new DataBaseHelper(EditTermActivity.this);

                    if (selectedTerm.getId() > 0) {
                        newTerm = new Term(selectedTerm.getId(), newTermTitle, newTermStart, newTermEnd);
                        boolean success = dataBaseHelper.updateTerm(newTerm);
                        if (success) {
                            Toast.makeText(EditTermActivity.this, "Term Updated: " + newTermTitle, Toast.LENGTH_SHORT).show();
                            onSuccess(selectedTerm.getId());
                        }
                    } else {
                        newTerm = new Term(1, newTermTitle, newTermStart, newTermEnd);
                        int termID = (int) dataBaseHelper.addTerm(newTerm);
                        if (termID > 0) {
                            Toast.makeText(EditTermActivity.this, "Term Added: " + newTermTitle, Toast.LENGTH_SHORT).show();
                            onSuccess(termID);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(EditTermActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(EditTermActivity.this);
                if(!hasCourse(termID)) {
                    boolean success = dataBaseHelper.deleteTerm(termID);
                    if (success) {
                        Toast.makeText(EditTermActivity.this, "Term Deleted: " + editTitleText.getText(), Toast.LENGTH_SHORT).show();
                        Intent backToTermListIntent = new Intent(EditTermActivity.this, ListTermsActivity.class);
                        startActivity(backToTermListIntent);
                    } else {
                        Toast.makeText(EditTermActivity.this, "Failed to Delete Term: " + editTitleText.getText(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditTermActivity.this, "Cannot delete term with attached courses. Please delete term's courses first.", Toast.LENGTH_LONG).show();
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTermActivity.this,
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTermActivity.this,
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem home = menu.findItem(R.id.action_home);

        home.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(EditTermActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }

    private boolean hasCourse(int termID) {
        ArrayList<Course> courses = dataBaseHelper.getCoursesFromTerm(termID);
        if(courses.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void onSuccess(int termID) {
            Intent editTerm = new Intent(EditTermActivity.this, DetailsTermActivity.class);
            editTerm.putExtra("TERM_ID", termID);

            startActivity(editTerm);
    }
}