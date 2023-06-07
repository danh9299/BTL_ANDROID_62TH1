package com.example.btl_android_62th1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivitySignin extends AppCompatActivity {
    private EditText txtUsernameSignin , txtPasswordSignin, txtPassword2Signin;
    private Button btnAccessSignIn;
    private UserDatabase databaseHelper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        txtUsernameSignin = findViewById(R.id.txtUsernameSignin);
        txtPasswordSignin = findViewById(R.id.txtPasswordSignin);
        txtPassword2Signin = findViewById(R.id.txtPassword2Signin);
        btnAccessSignIn = findViewById(R.id.btnAccessSignin);

        databaseHelper = new UserDatabase(this);

        btnAccessSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsernameSignin.getText().toString().trim();
                String password = txtPasswordSignin.getText().toString().trim();
                String password2 = txtPassword2Signin.getText().toString().trim();
                if (password.equals(password2)){
                    if (validateInput(username, password)) {
                        if (signUp(username, password)) {
                            Toast.makeText(ActivitySignin.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            // Tiến hành xử lý sau khi đăng ký thành công
                        } else {
                            Toast.makeText(ActivitySignin.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ActivitySignin.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ActivitySignin.this, "Mật khẩu không khớp nhau!", Toast.LENGTH_SHORT).show();
                }

        }});
    }
    private boolean validateInput(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    private boolean signUp(String username, String password) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long result = db.insert("users", null, values);

        db.close();

        return result != -1;
    }
}