package com.aeiric.thumb;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.aeiric.thumb.lib.OnThumbLoadedListener;
import com.aeiric.thumb.lib.ThumbFragment;

import static com.aeiric.thumb.lib.ThumbFragment.EXTRA_PATH;

public class ThumbActivity extends AppCompatActivity {

    ThumbFragment mThumbFragment;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumb);
        tintStatusBar();
        initToolbar();
        showFragment();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("视频封面");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void tintStatusBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.thumb_bg));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_thumb:
                mThumbFragment.createThumbFile(new OnThumbLoadedListener() {
                    @Override
                    public void onThumbLoaded(String path) {
                        Toast.makeText(ThumbActivity.this, "封面截取成功：" + path, Toast.LENGTH_LONG).show();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void showFragment() {
        String videoPath = getIntent().getStringExtra("video_path");
        mThumbFragment = new ThumbFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PATH, videoPath);
        mThumbFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fv_content, mThumbFragment, "manuscriptEditFragment")
                .commitAllowingStateLoss();
    }

}
