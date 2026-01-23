package com.dismal.devicetest;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestReportExporter {

    private static final String TAG = "TestReportExporter";
    private static final String FOLDER_NAME = "FactoryTestReports";

    private final Context context;

    public TestReportExporter(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Exports test results to a PDF.
     *
     * @return file path (legacy) or Downloads-relative path (Android 10+),
     *         or null on failure / missing permission.
     */
    public String exportResults(Button[] testBtns, int[] flags) {

        // ---- Permission check (ONLY Android 5–9) ----
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                Log.e(TAG, "WRITE_EXTERNAL_STORAGE not granted");
                return null;
            }
        }

        PdfDocument document = new PdfDocument();

        try {
            buildPdf(document, testBtns, flags);

            String fileName = "TestReport_" +
                    new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                            .format(new Date()) +
                    ".pdf";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return saveWithMediaStore(document, fileName);
            } else {
                return saveWithFileApi(document, fileName);
            }

        } catch (Exception e) {
            Log.e(TAG, "Export failed", e);
            return null;
        } finally {
            document.close();
        }
    }

    // --------------------------------------------------------
    // PDF CONTENT
    // --------------------------------------------------------

    private void buildPdf(PdfDocument document,
                          Button[] testBtns,
                          int[] flags) {

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        int x = 50;
        int y = 50;

        paint.setTextSize(24);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Device Test Report", x, y, paint);

        paint.setTextSize(14);
        paint.setTypeface(Typeface.DEFAULT);
        y += 40;

        canvas.drawText("Date: " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                        .format(new Date()), x, y, paint);
        y += 20;

        canvas.drawText("Model: " + Build.MODEL, x, y, paint);
        y += 20;

        canvas.drawText("Android: " + Build.VERSION.RELEASE, x, y, paint);
        y += 40;

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Test Name", x, y, paint);
        canvas.drawText("Result", x + 300, y, paint);
        y += 10;

        canvas.drawLine(x, y, x + 500, y, paint);
        y += 25;

        paint.setTypeface(Typeface.DEFAULT);

        for (int i = 0; i < testBtns.length; i++) {
            if (testBtns[i] == null ||
                    testBtns[i].getVisibility() != View.VISIBLE) {
                continue;
            }

            String testName = testBtns[i].getText().toString();
            String result = "NOT TESTED";
            int color = Color.DKGRAY;

            if (flags[i] == 1) {
                result = "PASS";
                color = Color.GREEN;
            } else if (flags[i] == 2) {
                result = "FAIL";
                color = Color.RED;
            }

            canvas.drawText(testName, x, y, paint);

            Paint resultPaint = new Paint(paint);
            resultPaint.setColor(color);
            canvas.drawText(result, x + 300, y, resultPaint);

            y += 20;

            if (y > 780) {
                document.finishPage(page);
                pageInfo = new PdfDocument.PageInfo.Builder(
                        595, 842, document.getPages().size() + 1).create();
                page = document.startPage(pageInfo);
                canvas = page.getCanvas();
                y = 50;
            }
        }

        document.finishPage(page);
    }

    // --------------------------------------------------------
    // ANDROID 10+ (API 29–36) — MediaStore
    // --------------------------------------------------------

    @RequiresApi(Build.VERSION_CODES.Q)
    private String saveWithMediaStore(PdfDocument document, String fileName)
            throws IOException {

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        values.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + "/" + FOLDER_NAME + "/"
        );
        values.put(MediaStore.MediaColumns.IS_PENDING, 1);

        Uri uri = context.getContentResolver().insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                values
        );

        if (uri == null) {
            throw new IOException("MediaStore insert returned null");
        }

        try (OutputStream os =
                     context.getContentResolver().openOutputStream(uri)) {
            if (os == null) {
                throw new IOException("Failed to open OutputStream");
            }
            document.writeTo(os);
        }

        values.clear();
        values.put(MediaStore.MediaColumns.IS_PENDING, 0);
        context.getContentResolver().update(uri, values, null, null);

        return "Downloads/" + FOLDER_NAME + "/" + fileName;
    }

    // --------------------------------------------------------
    // ANDROID 5–9 (API 21–28) — File API
    // --------------------------------------------------------

    private String saveWithFileApi(PdfDocument document, String fileName)
            throws IOException {

        File dir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS),
                FOLDER_NAME
        );

        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create directory");
        }

        File file = new File(dir, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            document.writeTo(fos);
        }

        return file.getAbsolutePath();
    }
}
