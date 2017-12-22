package com.andywang.ulife.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andywang.ulife.R;
import com.andywang.ulife.util.style.LanguageChangeManager;
import com.andywang.ulife.util.style.ThemeChangeManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplicationBriefActivity extends AppCompatActivity {

    @BindView(R.id.notify)
    LinearLayout mNotificationHead;
    @BindView(R.id.application_content)
    TextView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeManager.changeThemeMode(this);
        LanguageChangeManager.changeLanguage();
        setContentView(R.layout.activity_application_brief);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mNotificationHead.setVisibility(View.VISIBLE);
        }
        mContent.setText(Html.fromHtml(readTXTFileFromAssets(this)));
    }

    /***
     * 从asserts目录读取文件
     *
     * @param context
     * @return
     */
    public static String readTXTFileFromAssets(Context context) {
        InputStream in = null;
        StringBuilder builder = new StringBuilder();
        try {
            in = context.getAssets().open("ApplicationBrief.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String result = null;
            while ((result = reader.readLine()) != null) {
                builder.append(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    in = null;
                }
            }
        }
        return builder.toString();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ApplicationBriefActivity.class);
        context.startActivity(intent);
    }
}
