package com.zybooks.termtrackerjesse.Database;

public interface DBSchemas {

    // TERM SCHEMA

    public static final String TERM_TABLE = "TERM_TABLE";
    public static final String TERM_ID = "COLUMN_ID";
    public static final String TERM_TITLE = "COLUMN_TITLE";
    public static final String TERM_START_DATE = "COLUMN_START_DATE";
    public static final String TERM_END_DATE = "COLUMN_END_DATE";

    public static final String TERM_TABLE_CREATE = "CREATE TABLE " + TERM_TABLE + " (" + TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TERM_TITLE + " TEXT, " + TERM_START_DATE + " TEXT, " + TERM_END_DATE + " TEXT)";

    // COURSE SCHEMA

    public static final String COURSE_TABLE = "COURSE_TABLE";
    public static final String COURSE_ID = "COLUMN_ID";
    public static final String COURSE_TITLE = "COLUMN_TITLE";
    public static final String COURSE_START_DATE = "COLUMN_START_DATE";
    public static final String COURSE_END_DATE = "COLUMN_END_DATE";
    public static final String COURSE_STATUS = "COLUMN_STATUS";
    public static final String COURSE_TERM_ID = "COLUMN_TERM_ID";

    public static final String COURSE_TABLE_CREATE = "CREATE TABLE " + COURSE_TABLE + " (" + COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COURSE_TITLE + " TEXT, " +
            COURSE_START_DATE + " TEXT, " + COURSE_END_DATE + " TEXT, " + COURSE_STATUS + " TEXT, " + COURSE_TERM_ID + " INTEGER)";

    // ASSESSMENT SCHEMA

    public static final String ASSESSMENT_TABLE = "ASSESSMENT_TABLE";
    public static final String ASSESSMENT_ID = "COLUMN_ID";
    public static final String ASSESSMENT_TITLE = "COLUMN_TITLE";
    public static final String ASSESSMENT_DATE = "COLUMN_DATE";
    public static final String ASSESSMENT_TYPE = "COLUMN_TYPE";
    public static final String ASSESSMENT_COURSE_ID = "COLUMN_COURSE_ID";

    public static final String ASSESSMENT_TABLE_CREATE = "CREATE TABLE " + ASSESSMENT_TABLE + " (" + ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ASSESSMENT_TITLE + " TEXT, " +
            ASSESSMENT_DATE + " TEXT, " + ASSESSMENT_TYPE + " TEXT, " + ASSESSMENT_COURSE_ID + " INTEGER)";

    // NOTE SCHEMA

    public static final String NOTE_TABLE = "NOTE_TABLE";
    public static final String NOTE_ID = "COLUMN_ID";
    public static final String NOTE_TITLE = "COLUMN_TITLE";
    public static final String NOTE_TEXT = "COLUMN_TEXT";
    public static final String NOTE_COURSE_ID = "COLUMN_COURSE_ID";

    public static final String NOTE_TABLE_CREATE = "CREATE TABLE " + NOTE_TABLE + " (" + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE_TITLE + " TEXT, " +
            NOTE_TEXT + " TEXT, " + NOTE_COURSE_ID + " INTEGER)";

    // INSTRUCTOR SCHEMA

    public static final String INSTRUCTOR_TABLE = "INSTRUCTOR_TABLE";
    public static final String INSTRUCTOR_ID = "COLUMN_ID";
    public static final String INSTRUCTOR_NAME = "COLUMN_NAME";
    public static final String INSTRUCTOR_PHONE = "COLUMN_PHONE";
    public static final String INSTRUCTOR_EMAIL = "COLUMN_EMAIL";
    public static final String INSTRUCTOR_COURSE_ID = "COLUMN_COURSE_ID";

    public static final String INSTRUCTOR_TABLE_CREATE = "CREATE TABLE " + INSTRUCTOR_TABLE + " (" + INSTRUCTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + INSTRUCTOR_NAME + " TEXT, " +
            INSTRUCTOR_PHONE + " TEXT, " + INSTRUCTOR_EMAIL + " TEXT, " + INSTRUCTOR_COURSE_ID + " INTEGER)";
}