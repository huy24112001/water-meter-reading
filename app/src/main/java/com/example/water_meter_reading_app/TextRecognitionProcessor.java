package com.example.water_meter_reading_app;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.media.Image;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/** Processor for the text detector demo. */
public class TextRecognitionProcessor {

    private static final String TAG = "TextRecProcessor";

    private final TextRecognizer textRecognizer;

    public TextRecognitionProcessor(TextRecognizer textRecognizer) {
        this.textRecognizer = textRecognizer;
    }

    // Phương thức này nhận một đối tượng Bitmap và chuyển đổi nó thành InputImage trước khi xử lý văn bản.
    public Task<Text> processBitmapImage(Bitmap bitmap) {
        InputImage image = createInputImageFromBitmap(bitmap);
        return detectInImage(image);
    }

    // Phương thức này nhận một đối tượng Image và chuyển đổi nó thành InputImage trước khi xử lý văn bản.
    public Task<Text> processMediaImage(Image mediaImage, int rotationDegrees) {
        InputImage image = createInputImageFromMediaImage(mediaImage, rotationDegrees);
        return detectInImage(image);
    }

    // Phương thức này nhận một URI file và chuyển đổi thành InputImage trước khi xử lý văn bản.
    public Task<Text> processFileUri(String fileUri) throws IOException {
        InputImage image = createInputImageFromFileUri(fileUri);
        return detectInImage(image);
    }

    private InputImage createInputImageFromBitmap(Bitmap bitmap) {
        // Chuyển đổi đối tượng Bitmap thành mảng byte.
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // Tạo InputImage từ mảng byte và gọi detectInImage.
        return InputImage.fromByteArray(
                byteArray,
                bitmap.getWidth(),
                bitmap.getHeight(),
                0,  // Độ xoay của ảnh. Bạn có thể điều chỉnh nếu cần thiết.
                InputImage.IMAGE_FORMAT_NV21  // Hoặc IMAGE_FORMAT_YV12 tùy thuộc vào định dạng ảnh.
        );
    }

    private InputImage createInputImageFromMediaImage(Image mediaImage, int rotationDegrees) {
        return InputImage.fromMediaImage(mediaImage, rotationDegrees);
    }

    private InputImage createInputImageFromFileUri(String fileUri) throws IOException {
        // Thực hiện xử lý để lấy đối tượng InputImage từ file URI.
        // (Có thể cần sử dụng Intent.ACTION_GET_CONTENT để chọn ảnh từ thư viện)
        // ...

        // Tạm thời trả về một đối tượng InputImage giả định.
        return InputImage.fromFilePath(/*context*/ null, Uri.parse(fileUri));
    }

    protected Task<Text> detectInImage(InputImage image) {
        return textRecognizer.process(image);
    }
}
