package com.aeiric.thumb;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_GET_FILE_PATH = 1;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 1;
    private String mVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button select = findViewById(R.id.btn_select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
                } else {
                    getFilePath();
                }
            }
        });

        Button thumb = findViewById(R.id.btn_thumb);
        thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mVideoPath)) {
                    Toast.makeText(MainActivity.this, "请选择视频文件！", Toast.LENGTH_SHORT).show();
                    return;
                }
                intentTo();
            }
        });

    }

    private void intentTo() {
        Intent intent = new Intent(MainActivity.this, ThumbActivity.class);
        intent.putExtra("video_path", mVideoPath);
        startActivity(intent);
    }

    private void getFilePath() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "选择视频文件"), MainActivity.REQUEST_CODE_GET_FILE_PATH);
        } else {
            new AlertDialog.Builder(this).setTitle("未找到文件管理器")
                    .setMessage("请安装文件管理器以选择文件")
                    .setPositiveButton("确定", null)
                    .show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mVideoPath = PathResolver.getPath(this, data.getData());
            intentTo();
        }
    }

}
