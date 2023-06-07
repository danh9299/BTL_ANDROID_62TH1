package com.example.btl_android_62th1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;


public class ActivityForgotPassword extends AppCompatActivity {
    private Button btnForgotSend;
    private EditText editTextUsername, editTextEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnForgotSend = findViewById(R.id.btnForgotSend);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextUsername = findViewById(R.id.editTextUsername);
        btnForgotSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();

                if (isUsernameExists(username)) {
                    // Gửi mật khẩu qua email
                    sendPasswordToEmail(email);

                } else {
                    Toast.makeText(ActivityForgotPassword.this, "Không tồn tại username này", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private boolean isUsernameExists(String username) {
        UserDatabase dbHelper = new UserDatabase(ActivityForgotPassword.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String selection = "username = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query("users", null, selection, selectionArgs, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return exists;
    }
    private void sendPasswordToEmail(String email) {
        Toast.makeText(ActivityForgotPassword.this, "Đã gửi mật khẩu tới email " + email, Toast.LENGTH_SHORT).show();
    }
}
