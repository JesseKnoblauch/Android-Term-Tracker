package com.zybooks.termtrackerjesse.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.zybooks.termtrackerjesse.models.Assessment;
import com.zybooks.termtrackerjesse.models.Course;
import com.zybooks.termtrackerjesse.models.Instructor;
import com.zybooks.termtrackerjesse.models.Note;
import com.zybooks.termtrackerjesse.models.Term;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper implements DBSchemas {
    private static final String TAG = "DataBaseHelper";
    SQLiteDatabase db;

    public DataBaseHelper(@Nullable Context context) {
        super(context, "TermTracker.db", null, 2);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COURSE_TABLE_CREATE);
        db.execSQL(TERM_TABLE_CREATE);
        db.execSQL(ASSESSMENT_TABLE_CREATE);
        db.execSQL(NOTE_TABLE_CREATE);
        db.execSQL(INSTRUCTOR_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /*
    * TERM DB METHODS
    */

    public long addTerm(Term newTerm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TERM_TITLE, newTerm.getTitle());
        cv.put(TERM_START_DATE, newTerm.getStartDate());
        cv.put(TERM_END_DATE, newTerm.getEndDate());

        long insert = db.insert(TERM_TABLE, null, cv);

        if(insert > 0) {
            db.close();
            return insert;
        } else {
            db.close();
            return insert;
        }
    }

    public Term getOneTerm(int queryID) {
        Term term;
        String queryString = "SELECT * FROM " + TERM_TABLE + " WHERE " + TERM_ID + " = " + queryID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String start = cursor.getString(2);
            String end = cursor.getString(3);
            term = new Term(id, title, start, end);
        } else {
            int id = -1;
            String title = "not found";
            String start = "not found";
            String end = "not found";
            term = new Term(id, title, start, end);
        }

        return term;
    }

    public ArrayList<Term> getAllTerms() {
        ArrayList<Term> termList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TERM_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String start = cursor.getString(2);
                String end = cursor.getString(3);
                Term term = new Term(id, title, start, end);
                termList.add(term);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return termList;
    }

    public boolean updateTerm(Term term) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TERM_TITLE, term.getTitle());
        cv.put(TERM_START_DATE, term.getStartDate());
        cv.put(TERM_END_DATE, term.getEndDate());

        int rowsUpdated = db.update(TERM_TABLE, cv, TERM_ID + " = ?", new String[] { Integer.toString(term.getId()) });

        return rowsUpdated > 0;
    }

    public boolean deleteTerm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(TERM_TABLE, TERM_ID + " = ?", new String[] { String.valueOf(id) });

        if(rowsDeleted > 0) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Course> getCoursesFromTerm(int queryID) {
        ArrayList<Course> courseList = new ArrayList<>();

        String queryString = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COURSE_TERM_ID + " = " + queryID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String start = cursor.getString(2);
                String end = cursor.getString(3);
                String status = cursor.getString(4);
                int termID = cursor.getInt(5);
                Course course = new Course(id, title, start, end, status, termID);
                courseList.add(course);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return courseList;
    }

    /*
     * COURSE DB METHODS
     */

    public long addCourse(Course newCourse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COURSE_TITLE, newCourse.getTitle());
        cv.put(COURSE_START_DATE, newCourse.getStartDate());
        cv.put(COURSE_END_DATE, newCourse.getEndDate());
        cv.put(COURSE_STATUS, newCourse.getStatus());
        cv.put(COURSE_TERM_ID, newCourse.getTermID());

        long insert = db.insert(COURSE_TABLE, null, cv);

        if(insert > 0) {
            db.close();
            return insert;
        } else {
            db.close();
            return insert;
        }
    }

    public Course getOneCourse(int queryID) {
        Course course;
        String queryString = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COURSE_ID + " = " + queryID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String start = cursor.getString(2);
            String end = cursor.getString(3);
            String status = cursor.getString(4);
            int term = cursor.getInt(5);
            course = new Course(id, title, start, end, status, term);
        } else {
            int id = -1;
            String title = "not found";
            String start = "not found";
            String end = "not found";
            String status = "not found";
            int term = -1;
            course = new Course(id, title, start, end, status, term);
        }

        return course;
    }

    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> courseList = new ArrayList<>();

        String queryString = "SELECT * FROM " + COURSE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String start = cursor.getString(2);
                String end = cursor.getString(3);
                String status = cursor.getString(4);
                int term = cursor.getInt(5);
                Course course = new Course(id, title, start, end, status, term);
                courseList.add(course);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return courseList;
    }

    public boolean updateCourse(Course newCourse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COURSE_TITLE, newCourse.getTitle());
        cv.put(COURSE_START_DATE, newCourse.getStartDate());
        cv.put(COURSE_END_DATE, newCourse.getEndDate());
        cv.put(COURSE_STATUS, newCourse.getStatus());
        cv.put(COURSE_TERM_ID, newCourse.getTermID());

        int rowsUpdated = db.update(COURSE_TABLE, cv, COURSE_ID + " = ?", new String[] { Integer.toString(newCourse.getId()) });

        return rowsUpdated > 0;
    }

    public boolean deleteCourse(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(COURSE_TABLE, COURSE_ID + " = ?", new String[] { String.valueOf(id) });

        if(rowsDeleted > 0) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Assessment> getAssessmentsFromCourse(int queryID) {
        ArrayList<Assessment> assessmentList = new ArrayList<>();

        String queryString = "SELECT * FROM " + ASSESSMENT_TABLE + " WHERE " + ASSESSMENT_COURSE_ID + " = " + queryID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String date = cursor.getString(2);
                String type = cursor.getString(3);
                int courseID = cursor.getInt(4);
                Assessment assessment = new Assessment(id, title, date, type, courseID);
                assessmentList.add(assessment);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return assessmentList;
    }

    public ArrayList<Instructor> getInstructorsFromCourse(int queryID) {
        ArrayList<Instructor> instructorList = new ArrayList<>();

        String queryString = "SELECT * FROM " + INSTRUCTOR_TABLE + " WHERE " + INSTRUCTOR_COURSE_ID + " = " + queryID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                String email = cursor.getString(3);
                int courseID = cursor.getInt(4);
                Instructor instructor = new Instructor(id, name, phone, email, courseID);
                instructorList.add(instructor);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return instructorList;
    }

    public ArrayList<Note> getNotesFromCourse(int queryID) {
        ArrayList<Note> noteList = new ArrayList<>();

        String queryString = "SELECT * FROM " + NOTE_TABLE + " WHERE " + NOTE_COURSE_ID + " = " + queryID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String text = cursor.getString(2);
                int courseID = cursor.getInt(3);
                Note note = new Note(id, title, text, courseID);
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return noteList;
    }

    /*
     * ASSESSMENT DB METHODS
     */

    public long addAssessment(Assessment newAssessment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ASSESSMENT_TITLE, newAssessment.getTitle());
        cv.put(ASSESSMENT_DATE, newAssessment.getDate());
        cv.put(ASSESSMENT_TYPE, newAssessment.getType());
        cv.put(ASSESSMENT_COURSE_ID, newAssessment.getCourseID());

        long insert = db.insert(ASSESSMENT_TABLE, null, cv);

        if(insert > 0) {
            db.close();
            return insert;
        } else {
            db.close();
            return insert;
        }
    }

    public Assessment getOneAssessment(int queryID) {
        Assessment assessment;
        String queryString = "SELECT * FROM " + ASSESSMENT_TABLE + " WHERE " + ASSESSMENT_ID + " = " + queryID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String date = cursor.getString(2);
            String type = cursor.getString(3);
            int course = cursor.getInt(4);
            assessment = new Assessment(id, title, date, type, course);
        } else {
            int id = -1;
            String title = "not found";
            String date = "not found";
            String type = "not found";
            int course = -1;
            assessment = new Assessment(id, title, date, type, course);
        }

        return assessment;
    }

    public ArrayList<Assessment> getAllAssessments() {
        ArrayList<Assessment> assessmentList = new ArrayList<>();

        String queryString = "SELECT * FROM " + ASSESSMENT_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String date = cursor.getString(2);
                String type = cursor.getString(3);
                int course = cursor.getInt(4);
                Assessment assessment = new Assessment(id, title, date, type, course);

                assessmentList.add(assessment);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return assessmentList;
    }

    public boolean updateAssessment(Assessment newAssessment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(ASSESSMENT_TITLE, newAssessment.getTitle());
        cv.put(ASSESSMENT_DATE, newAssessment.getDate());
        cv.put(ASSESSMENT_TYPE, newAssessment.getType());
        cv.put(ASSESSMENT_COURSE_ID, newAssessment.getCourseID());

        int rowsUpdated = db.update(ASSESSMENT_TABLE, cv, ASSESSMENT_ID + " = ?", new String[] { Integer.toString(newAssessment.getId()) });

        return rowsUpdated > 0;
    }

    public boolean deleteAssessment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(ASSESSMENT_TABLE, ASSESSMENT_ID + " = ?", new String[] { String.valueOf(id) });

        if(rowsDeleted > 0) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * NOTE DB METHODS
     */

    public long addNote(Note newNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(NOTE_TITLE, newNote.getTitle());
        cv.put(NOTE_TEXT, newNote.getText());
        cv.put(NOTE_COURSE_ID, newNote.getCourseID());

        long insert = db.insert(NOTE_TABLE, null, cv);

        if(insert > 0) {
            db.close();
            return insert;
        } else {
            db.close();
            return insert;
        }
    }

    public Note getOneNote(int queryID) {
        Note note;
        String queryString = "SELECT * FROM " + NOTE_TABLE + " WHERE " + NOTE_ID + " = " + queryID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String text = cursor.getString(2);
            int course = cursor.getInt(3);
            note = new Note(id, title, text, course);
        } else {
            int id = -1;
            String title = "not found";
            String text = "not found";
            int course = -1;
            note = new Note(id, title, text, course);
        }

        return note;
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> noteList = new ArrayList<>();

        String queryString = "SELECT * FROM " + NOTE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String text = cursor.getString(2);
                int course = cursor.getInt(3);
                Note note = new Note(id, title, text, course);

                noteList.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return noteList;
    }

    public boolean updateNote(Note newNote) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(NOTE_TITLE, newNote.getTitle());
        cv.put(NOTE_TEXT, newNote.getText());
        cv.put(NOTE_COURSE_ID, newNote.getCourseID());

        int rowsUpdated = db.update(NOTE_TABLE, cv, NOTE_ID + " = ?", new String[] { Integer.toString(newNote.getId()) });

        return rowsUpdated > 0;
    }

    public boolean deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(NOTE_TABLE, NOTE_ID + " = ?", new String[] { String.valueOf(id) });

        if(rowsDeleted > 0) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * INSTRUCTOR DB METHODS
     */

    public long addInstructor(Instructor newInstructor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(INSTRUCTOR_NAME, newInstructor.getName());
        cv.put(INSTRUCTOR_PHONE, newInstructor.getPhone());
        cv.put(INSTRUCTOR_EMAIL, newInstructor.getEmail());
        cv.put(INSTRUCTOR_COURSE_ID, newInstructor.getCourseID());

        long insert = db.insert(INSTRUCTOR_TABLE, null, cv);

        if(insert > 0) {
            db.close();
            return insert;
        } else {
            db.close();
            return insert;
        }
    }

    public Instructor getOneInstructor(int queryID) {
        Instructor instructor;
        String queryString = "SELECT * FROM " + INSTRUCTOR_TABLE + " WHERE " + INSTRUCTOR_ID + " = " + queryID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String phone = cursor.getString(2);
            String email = cursor.getString(3);
            int course = cursor.getInt(4);
            instructor = new Instructor(id, name, phone, email, course);
        } else {
            int id = -1;
            String name = "not found";
            String phone = "not found";
            String email = "not found";
            int course = -1;
            instructor = new Instructor(id, name, phone, email, course);
        }

        return instructor;
    }

    public ArrayList<Instructor> getAllInstructors() {
        ArrayList<Instructor> instructorList = new ArrayList<>();

        String queryString = "SELECT * FROM " + INSTRUCTOR_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                String email = cursor.getString(3);
                int course = cursor.getInt(4);
                Instructor instructor = new Instructor(id, name, phone, email, course);

                instructorList.add(instructor);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return instructorList;
    }

    public boolean updateInstructor(Instructor newInstructor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(INSTRUCTOR_NAME, newInstructor.getName());
        cv.put(INSTRUCTOR_PHONE, newInstructor.getPhone());
        cv.put(INSTRUCTOR_EMAIL, newInstructor.getEmail());
        cv.put(INSTRUCTOR_COURSE_ID, newInstructor.getCourseID());

        int rowsUpdated = db.update(INSTRUCTOR_TABLE, cv, INSTRUCTOR_ID + " = ?", new String[] { Integer.toString(newInstructor.getId()) });

        return rowsUpdated > 0;
    }

    public boolean deleteInstructor(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(INSTRUCTOR_TABLE, INSTRUCTOR_ID + " = ?", new String[] { String.valueOf(id) });

        if(rowsDeleted > 0) {
            return true;
        } else {
            return false;
        }
    }
}
