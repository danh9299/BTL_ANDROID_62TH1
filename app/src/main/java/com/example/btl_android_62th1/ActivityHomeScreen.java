package com.example.btl_android_62th1;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ActivityHomeScreen extends AppCompatActivity {

    private Button btnQuanLy,btnThi, btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        btnQuanLy = findViewById(R.id.btnQuanLy);
        btnThi = findViewById(R.id.btnThi);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(ActivityHomeScreen.this, "THOÁT thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityHomeScreen.this,MainActivity.class);
                startActivity(intent);

            }
        });


        btnQuanLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityHomeScreen.this,QuestionManagementActivity.class);
                startActivity(intent);

            }
        });

        btnThi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityHomeScreen.this,QuizActivity.class);
                startActivity(intent);
            }
        });
    }

}
