package com.example.btl_android_62th1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

public class QuestionManagementActivity extends AppCompatActivity {

    private EditText etQuestionText;
    private EditText etOption1;
    private EditText etOption2;
    private EditText etOption3;
    private EditText etCorrectAnswer;
    private Button btnAddQuestion;
    private Button btnDeleteAllQuestions;
    private QuizDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_management);

        dbHelper = new QuizDatabaseHelper(this);

        etQuestionText = findViewById(R.id.etQuestionText);
        etOption1 = findViewById(R.id.etOption1);
        etOption2 = findViewById(R.id.etOption2);
        etOption3 = findViewById(R.id.etOption3);
        etCorrectAnswer = findViewById(R.id.etCorrectAnswer);
        btnAddQuestion = findViewById(R.id.btnAddQuestion);
        btnDeleteAllQuestions = findViewById(R.id.btnDeleteAllQuestions);
        btnDeleteAllQuestions.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                clearAllQuestions();
            }
        });
        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestion();
            }
        });
    }

    private void addQuestion() {

        try {
        String questionText = etQuestionText.getText().toString().trim();
        String option1 = etOption1.getText().toString().trim();
        String option2 = etOption2.getText().toString().trim();
        String option3 = etOption3.getText().toString().trim();

        if (option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || etCorrectAnswer.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;}

        int correctAnswer = Integer.parseInt(etCorrectAnswer.getText().toString().trim());





        SQLiteDatabase db = dbHelper.getWritableDatabase();



        ContentValues values = new ContentValues();
        values.put("question_text", questionText);
        values.put("option1", option1);
        values.put("option2", option2);
        values.put("option3", option3);
        values.put("correct_answer", correctAnswer);

        long newRowId = db.insert("questions", null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Question added successfully.", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Failed to add question.", Toast.LENGTH_SHORT).show();
        }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Đáp án phải là dạng số 1, 2, hoặc 3.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void clearFields() {
        etQuestionText.setText("");
        etOption1.setText("");
        etOption2.setText("");
        etOption3.setText("");
        etCorrectAnswer.setText("");
    }

private void clearAllQuestions(){
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    int rowsDeleted = db.delete("questions", null, null);
    if (rowsDeleted > 0) {
        Toast.makeText(this, "Đã xóa " + rowsDeleted + " câu hỏi", Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(this, "Không có câu hỏi để xóa", Toast.LENGTH_SHORT).show();
    }
}}