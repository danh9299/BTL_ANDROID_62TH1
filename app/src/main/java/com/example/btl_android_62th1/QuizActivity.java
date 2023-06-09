package com.example.btl_android_62th1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestionText;
    private RadioButton rbOption1;
    private RadioButton rbOption2;
    private RadioButton rbOption3;
    private RadioGroup rgOptions;
    private Button btnNextQuestion;

    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questions = getQuestionsFromDatabase();
        Collections.shuffle(questions);

        currentQuestionIndex = 0;
        score = 0;

        tvQuestionText = findViewById(R.id.tvQuestionText);
        rbOption1 = findViewById(R.id.rbOption1);
        rbOption2 = findViewById(R.id.rbOption2);
        rbOption3 = findViewById(R.id.rbOption3);
        rgOptions = findViewById(R.id.rgOptions);
        btnNextQuestion = findViewById(R.id.btnNextQuestion);

        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        showQuestion();
    }

    private void showQuestion() {
        Collections.shuffle(questions); // Trộn thứ tự câu hỏi
        Question currentQuestion = questions.get(currentQuestionIndex);

        tvQuestionText.setText(currentQuestion.getQuestionText());
        rbOption1.setText(currentQuestion.getOption1());
        rbOption2.setText(currentQuestion.getOption2());
        rbOption3.setText(currentQuestion.getOption3());

        rgOptions.clearCheck();
    }

    private void checkAnswer() {
        int selectedOptionId = rgOptions.getCheckedRadioButtonId();

        if (selectedOptionId == -1) {
            Toast.makeText(this, "Bạn phải chọn đáp án đã.", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedOption = findViewById(selectedOptionId);
        int selectedAnswer = rgOptions.indexOfChild(selectedOption) + 1;

        Question currentQuestion = questions.get(currentQuestionIndex);

        if (selectedAnswer == currentQuestion.getCorrectAnswer()) {
            score++;
        }

        currentQuestionIndex++;

        if (currentQuestionIndex < questions.size()) {
            showQuestion();
        } else {
            showQuizResult();
        }
    }

    private void showQuizResult() {
        float percentage = (float) score / questions.size() * 100;
        String result = "Bài thi kết thúc\nĐiểm của bạn là: " + score + "/" + questions.size() + " (" + percentage + "%)";

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();
    }

    private List<Question> getQuestionsFromDatabase() {
        List<Question> questions = new ArrayList<>();

        SQLiteDatabase db = new QuizDatabaseHelper(this).getReadableDatabase();

        String[] projection = {
                "question_text",
                "option1",
                "option2",
                "option3",
                "correct_answer"
        };

        Cursor cursor = db.query(
                "questions",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String questionText = cursor.getString(cursor.getColumnIndexOrThrow("question_text"));
            String option1 = cursor.getString(cursor.getColumnIndexOrThrow("option1"));
            String option2 = cursor.getString(cursor.getColumnIndexOrThrow("option2"));
            String option3 = cursor.getString(cursor.getColumnIndexOrThrow("option3"));
            int correctAnswer = cursor.getInt(cursor.getColumnIndexOrThrow("correct_answer"));

            Question question = new Question(questionText, option1, option2, option3, correctAnswer);
            questions.add(question);
        }

        cursor.close();

        return questions;
    }
}
