package com.example.water_meter_reading_app;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;



public class GetWaterMeter extends AppCompatActivity {

    Button btn;
    Button btnSend ;

    String recognizedText  = null;

    TextView txt ;
    ActivityResultLauncher<Intent> activityResultLauncher;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Water Meter");
            actionBar.setDisplayHomeAsUpEnabled(true); // Hiển thị nút mũi tên quay lại
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getwatermeter);
        txt = findViewById(R.id.edtWaterMeter);
        btn = (Button) findViewById(R.id.btnstart);
        btnSend = (Button) findViewById(R.id.btnSend);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(cameraIntent);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(recognizedText != null){

                   AlertDialog.Builder builder = new AlertDialog.Builder(GetWaterMeter.this);
                   builder.setTitle("Notification").setMessage("Are you sure you want to post data to the database ?")
                           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   displayOrSaveResult(recognizedText);
                               }
                           })
                           .setNegativeButton("No", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {

                               }
                           })
                           .show();
               }
               else {
                   Toast.makeText(GetWaterMeter.this, "Error Water Meter " , Toast.LENGTH_SHORT).show();
               }
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            // Nhận ảnh từ Intent
                            Bundle extras = result.getData().getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");

                            // Chuyển ảnh sang TextRecognizer
                            processImageWithTextRecognizer(imageBitmap);
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(GetWaterMeter.this, IdentifyClock.class));
        // Xử lý sự kiện khi nút mũi tên quay lại được nhấn
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed(); // Điều này giống như việc nhấn nút Back trên thiết bị
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    private void processImageWithTextRecognizer(Bitmap imageBitmap) {
        // Tạo một đối tượng TextRecognizer
        TextRecognizer textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        // Tạo một đối tượng InputImage từ đối tượng Bitmap
        InputImage inputImage = InputImage.fromBitmap(imageBitmap, 0);

        // Tạo một đối tượng TextRecognitionProcessor hoặc xử lý trực tiếp với TextRecognizer
        TextRecognitionProcessor textRecognitionProcessor = new TextRecognitionProcessor(textRecognizer);

        // Gọi phương thức detectInImage để nhận diện văn bản
        Task<Text> textRecognitionTask = textRecognitionProcessor.detectInImage(inputImage);

        // Xử lý kết quả
        textRecognitionTask.addOnCompleteListener(new OnCompleteListener<Text>() {
            @Override
            public void onComplete(@NonNull Task<Text> task) {
                if (task.isSuccessful()) {
                    Text text = task.getResult();
                    if (text != null) {
                        // Xử lý kết quả nhận diện văn bản ở đây
                         recognizedText = text.getText();
                         txt.setText(recognizedText);
                        // Hiển thị hoặc lưu kết quả theo nhu cầu của bạn
//                        displayOrSaveResult(recognizedText);
                    }
                } else {
                    // Xử lý lỗi nếu có
                    Exception e = task.getException();
                    e.printStackTrace();
                }
            }
        });
    }

    private void displayOrSaveResult(String recognizedText) {


        Intent intent = getIntent();
        if (intent != null) {
            IdentifyClock.WaterMeterInfo receivedWaterMeterInfo = (IdentifyClock.WaterMeterInfo) intent.getParcelableExtra("waterMeterInfo");
            receivedWaterMeterInfo.waterMeter = recognizedText ;
            if (receivedWaterMeterInfo != null) {
                firestore.collection("waterMeterInfo")
                        .add(receivedWaterMeterInfo)
                        .addOnSuccessListener(documentReference -> {
                            // Ghi dữ liệu thành công
                            String documentId = documentReference.getId();
                            CustomAlert.showAlert(GetWaterMeter.this, "Success","You have saved your data successfully");
                            // Thực hiện các thao tác sau khi ghi dữ liệu
                            // Ví dụ: Hiển thị thông báo, chuyển hướng,...
//                            startActivity(new Intent(GetWaterMeter.this, Home.class));
                        })
                        .addOnFailureListener(e -> {
                            CustomAlert.showAlert(GetWaterMeter.this, "Failed","Error occurred during sending");

                            Log.e("GetWater", "Lỗi: " + e.getMessage());
//                            startActivity(new Intent(GetWaterMeter.this, Home.class));

                        });

            }

        }
    }
}
