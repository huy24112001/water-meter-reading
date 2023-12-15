package com.example.water_meter_reading_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

public class IdentifyClock extends AppCompatActivity {
    private static final String TAG = "QRCodeScanner";

    WaterMeterInfo waterMeterInfo = new WaterMeterInfo();
    Button btnContinue ;
    TextView text ;
    Button btnScanner ;

    EditText edtID ;
    EditText edtDate ;
    EditText edtLocation ;
    EditText edtSupplier ;
    EditText edtData ;


    public static class WaterMeterInfo implements Parcelable {

        public String id ;
        public String installationDate ;
        public String installationLocation ;

        public String provider ;

        public String operationalDataDate ;

        public String waterMeter  ;

        public WaterMeterInfo() {
            // Không có đối số cần được truyền vào đây
        }
        protected WaterMeterInfo(Parcel in) {
            id = in.readString();
            installationDate = in.readString();
            installationLocation = in.readString();
            provider = in.readString();
            operationalDataDate = in.readString();
            waterMeter = in.readString();
        }

        public static final Creator<WaterMeterInfo> CREATOR = new Creator<WaterMeterInfo>() {
            @Override
            public WaterMeterInfo createFromParcel(Parcel in) {
                return new WaterMeterInfo(in);
            }

            @Override
            public WaterMeterInfo[] newArray(int size) {
                return new WaterMeterInfo[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(installationDate);
            parcel.writeString(installationLocation);
            parcel.writeString(provider);
            parcel.writeString(operationalDataDate);
            parcel.writeString(waterMeter);
        }
    }
    public static WaterMeterInfo scanQRCode(Context context, Bitmap bitmap) {

        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(IdentifyClock.this, Home.class));
        // Xử lý sự kiện khi nút mũi tên quay lại được nhấn
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed(); // Điều này giống như việc nhấn nút Back trên thiết bị
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifyclock);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Scan Device");
            actionBar.setDisplayHomeAsUpEnabled(true); // Hiển thị nút mũi tên quay lại
        }


        btnScanner = (Button) findViewById(R.id.btnScannerQR);
        text = findViewById(R.id.tvwelcome);
        edtID = findViewById(R.id.edtID);
        edtDate = findViewById(R.id.edtDate);
        edtLocation = findViewById(R.id.edtLocation);
        edtSupplier = findViewById(R.id.edtSupplier);
        edtData = findViewById(R.id.edtData);

        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(IdentifyClock.this) ;
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scanner QR");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });

        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IdentifyClock.this, GetWaterMeter.class);
                intent.putExtra("waterMeterInfo",  waterMeterInfo);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null){
            String content = intentResult.getContents();
            if(content != null){
                String[] lines = content.split("\n");
                // Tạo đối tượng WaterMeterInfo và thiết lập thông tin

                for (String line : lines) {
                    if (line.startsWith("ID Đồng Hồ:")) {
                        waterMeterInfo.id = line.substring("ID Đồng Hồ:".length()).trim();
                    } else if (line.startsWith("Ngày Lắp Đặt:")) {
                        waterMeterInfo.installationDate = line.substring("Ngày Lắp Đặt:".length()).trim();
                    } else if (line.startsWith("Vị Trí Lắp Đặt:")) {
                        waterMeterInfo.installationLocation = line.substring("Vị Trí Lắp Đặt:".length()).trim();
                    } else if (line.startsWith("Nhà Cung Cấp:")) {
                        waterMeterInfo.provider = line.substring("Nhà Cung Cấp:".length()).trim();
                    } else if (line.startsWith("Dữ Liệu Vận Hành:")) {
                        waterMeterInfo.operationalDataDate = line.substring("Dữ Liệu Vận Hành:".length()).trim();
                    }
                }

                edtID.setText(waterMeterInfo.id);
                edtDate.setText(waterMeterInfo.installationDate);
                edtLocation.setText(waterMeterInfo.installationLocation);
                edtData.setText(waterMeterInfo.operationalDataDate);
                edtSupplier.setText(waterMeterInfo.provider);
                waterMeterInfo.waterMeter = "123";
            }
            else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
