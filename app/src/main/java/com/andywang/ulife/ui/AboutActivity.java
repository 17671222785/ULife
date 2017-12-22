package com.andywang.ulife.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.andywang.ulife.R;
import com.andywang.ulife.util.style.LanguageChangeManager;
import com.andywang.ulife.util.style.ThemeChangeManager;
import com.andywang.ulife.view.fragment.about.AboutFragment;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton mBackView;
    private LinearLayout mNotificationHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeManager.changeThemeMode(this);
        LanguageChangeManager.changeLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mNotificationHead = findViewById(R.id.notify);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mNotificationHead.setVisibility(View.VISIBLE);
        }
        mBackView = findViewById(R.id.back_forward);
        getFragmentManager().beginTransaction().add(R.id.about_fragment, new AboutFragment()).commit();
        mBackView.setOnClickListener(this);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
