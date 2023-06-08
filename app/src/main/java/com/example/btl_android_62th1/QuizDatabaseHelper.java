package com.example.btl_android_62th1;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuizDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "quiz.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_QUESTIONS_TABLE =
            "CREATE TABLE questions (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "question_text TEXT, option1 TEXT, option2 TEXT, option3 TEXT, correct_answer INTEGER)";

    private static final String SQL_DELETE_QUESTIONS_TABLE =
            "DROP TABLE IF EXISTS questions";

    public QuizDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_QUESTIONS_TABLE);
        onCreate(db);
    }
}
