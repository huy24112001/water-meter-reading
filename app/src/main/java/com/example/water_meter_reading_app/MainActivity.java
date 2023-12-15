package com.example.water_meter_reading_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnStart;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sử dụng Handler để chờ 3 giây
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Chuyển sang màn hình đăng nhập sau 3 giây
                startActivity(new Intent(MainActivity.this, Login.class));
                finish(); // Đóng màn hình chính để người dùng không thể quay lại nó bằng nút "Back"
            }
        }, 2000); // 3000 milliseconds = 3 giây

//        btnStart = (Button) findViewById(R.id.btnstart);
//        btnStart.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent i = new Intent(getApplicationContext(),Login.class);
//                startActivity(i);
//            }
//        });

    }
}