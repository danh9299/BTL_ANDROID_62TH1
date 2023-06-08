package com.example.btl_android_62th1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AdapterView;
import java.util.ArrayList;
import java.util.List;



public class QuestionManagementActivity extends AppCompatActivity {
    private ListView questionListView;
    private ArrayAdapter<Question> questionAdapter;
    private EditText etQuestionText;
    private EditText etOption1;
    private EditText etOption2;
    private EditText etOption3;
    private EditText etCorrectAnswer;
    private Button btnAddQuestion;
    private Button btnUpdateQuestion;
    private Button btnDeleteQuestion;
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
        btnDeleteQuestion = findViewById(R.id.btnDeleteQuestion);
        btnUpdateQuestion = findViewById(R.id.btnUpdateQuestion);
        questionListView = findViewById(R.id.questionListView);
        loadQuestionList();

        questionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Xử lý khi một câu hỏi được chọn trong ListView
                Question question = (Question) parent.getItemAtPosition(position);
                populateFieldsWithQuestion(question);
            }
        });

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

        btnUpdateQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuestion();
            }
        });

        btnDeleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteQuestion();
            }
        });
    }

    private void loadQuestionList() {
        List<Question> questionList = new ArrayList<>();

        // Thực hiện truy vấn dữ liệu từ cơ sở dữ liệu
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("questions", null, null, null, null, null, null);

        // Kiểm tra xem có câu hỏi nào không
        if (cursor.getCount() > 0) {
            // Đọc dữ liệu từ con trỏ và thêm vào danh sách câu hỏi
            while (cursor.moveToNext()) {
                // Đọc thông tin câu hỏi từ con trỏ và tạo đối tượng Question
                String questionText = cursor.getString(cursor.getColumnIndex("question_text"));
                String option1 = cursor.getString(cursor.getColumnIndex("option1"));
                String option2 = cursor.getString(cursor.getColumnIndex("option2"));
                String option3 = cursor.getString(cursor.getColumnIndex("option3"));
                int correctAnswer = cursor.getInt(cursor.getColumnIndex("correct_answer"));

                Question question = new Question(questionText, option1, option2, option3, correctAnswer);
                questionList.add(question);
            }
        }

        // Đóng con trỏ và cơ sở dữ liệu
        cursor.close();
        db.close();

        // Tạo adapter và gán danh sách câu hỏi vào adapter
        questionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, questionList);

        // Gán adapter cho ListView để hiển thị danh sách câu hỏi
        questionListView.setAdapter(questionAdapter);
    }

    private void deleteQuestion(){
        String questionText = etQuestionText.getText().toString().trim();

        if (TextUtils.isEmpty(questionText)) {
            Toast.makeText(this, "Vui lòng chọn câu hỏi để xóa.", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete("questions", "question_text = ?", new String[]{questionText});

        if (rowsAffected > 0) {
            Toast.makeText(this, "Xóa câu hỏi thành công.", Toast.LENGTH_SHORT).show();
            questionAdapter.clear();
            loadQuestionList();
            clearFields();
        } else {
            Toast.makeText(this, "Xóa câu hỏi thất bại.", Toast.LENGTH_SHORT).show();
        }

    }
    private void updateQuestion(){
        try {
            String questionText = etQuestionText.getText().toString().trim();
            String option1 = etOption1.getText().toString().trim();
            String option2 = etOption2.getText().toString().trim();
            String option3 = etOption3.getText().toString().trim();

            if (TextUtils.isEmpty(questionText) || TextUtils.isEmpty(option1) ||
                    TextUtils.isEmpty(option2) || TextUtils.isEmpty(option3)) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            int correctAnswer = Integer.parseInt(etCorrectAnswer.getText().toString().trim());

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("question_text", questionText);
            values.put("option1", option1);
            values.put("option2", option2);
            values.put("option3", option3);
            values.put("correct_answer", correctAnswer);

            int rowsAffected = db.update("questions", values, "question_text = ?", new String[]{questionText});

            if (rowsAffected > 0) {
                Toast.makeText(this, "Cập nhật câu hỏi mới thành công.", Toast.LENGTH_SHORT).show();
                questionAdapter.clear();
                loadQuestionList();
                clearFields();
            } else {
                Toast.makeText(this, "Cập nhật thất bại.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Đáp án phải là dạng số 1, 2, hoặc 3.", Toast.LENGTH_SHORT).show();
        }

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
            Toast.makeText(this, "Thêm mới thành công.", Toast.LENGTH_SHORT).show();
            // Cập nhật toàn bộ danh sách
            questionAdapter.clear();
            loadQuestionList();

            clearFields();
        } else {
            Toast.makeText(this, "Thêm mới thất bại.", Toast.LENGTH_SHORT).show();
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
    private void populateFieldsWithQuestion(Question question) {
        etQuestionText.setText(question.getQuestionText());
        etOption1.setText(question.getOption1());
        etOption2.setText(question.getOption2());
        etOption3.setText(question.getOption3());
        etCorrectAnswer.setText(String.valueOf(question.getCorrectAnswer()));
    }

private void clearAllQuestions(){
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    int rowsDeleted = db.delete("questions", null, null);
    if (rowsDeleted > 0) {
        Toast.makeText(this, "Đã xóa " + rowsDeleted + " câu hỏi", Toast.LENGTH_SHORT).show();
        questionAdapter.clear();
    } else {
        Toast.makeText(this, "Không có câu hỏi để xóa", Toast.LENGTH_SHORT).show();
    }
}}