package com.example.water_meter_reading_app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    Button btn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Home");
            actionBar.setDisplayHomeAsUpEnabled(true); // Hiển thị nút mũi tên quay lại
        }

        btn = (Button) findViewById(R.id.btnstart);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, IdentifyClock.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(Home.this, MainActivity.class));
        // Xử lý sự kiện khi nút mũi tên quay lại được nhấn
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed(); // Điều này giống như việc nhấn nút Back trên thiết bị
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}
