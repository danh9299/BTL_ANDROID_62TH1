package com.example.btl_android_62th1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
public class ActivityLogin extends AppCompatActivity {
    private EditText txtUsername, txtPassword;
    private Button btnAccessLogin;
    private UserDatabase databaseHelper;

    private Button btnForgot;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);

        btnAccessLogin = findViewById(R.id.btnAccessLogin);

        databaseHelper = new UserDatabase(this);
        btnForgot = findViewById(R.id.btnForgot);
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPasswordIntent = new Intent(ActivityLogin.this, ActivityForgotPassword.class);
                startActivity(forgotPasswordIntent);
            }
        });

        btnAccessLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if (validateInput(username, password)) {
                    if (login(username, password)) {
                        Toast.makeText(ActivityLogin.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        // Tiến hành xử lý sau khi đăng nhập thành công
                    } else {
                        Toast.makeText(ActivityLogin.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivityLogin.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInput(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    private boolean login(String username, String password) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query("users", null, selection, selectionArgs, null, null, null);
        boolean logInSuccessful = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return logInSuccessful;
    }}
