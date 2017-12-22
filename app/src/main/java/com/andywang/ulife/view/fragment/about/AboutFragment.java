package com.andywang.ulife.view.fragment.about;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andywang.ulife.R;
import com.andywang.ulife.entity.calendar.bean.About;
import com.andywang.ulife.entity.calendar.bean.Settings;
import com.andywang.ulife.ui.ApplicationBriefActivity;
import com.andywang.ulife.util.support.VersionManager;

/**
 * Created by parting_soul on 2016/10/29.
 */

public class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private Preference mAppicationBriefPreference;

    private Preference mCurrentVersionPreference;

    private Preference mSharePreference;

    private Preference mAuthorGithubPreference;

    private Preference mMailboxPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(About.ABOUT_XML_NAME);
        //添加对应的About布局
        addPreferencesFromResource(R.xml.about);

        mAppicationBriefPreference = findPreference(About.APPLICATION_BRIEF_KEY);
        mCurrentVersionPreference = findPreference(About.CURRENT_VERSION_KEY);
        mSharePreference = findPreference(About.SHARE_KEY);
        mAuthorGithubPreference = findPreference(About.AUTHOR_GITHUB_KEY);
        mMailboxPreference = findPreference(About.MAILBOX_KEY);

        mCurrentVersionPreference.setSummary(getResources().getString(R.string.app_name) + " " + "Version " +
                VersionManager.getVersionName(getActivity()) +
                VersionManager.getVersionCode(getActivity()));

        mAppicationBriefPreference.setOnPreferenceClickListener(this);
        mCurrentVersionPreference.setOnPreferenceClickListener(this);
        mSharePreference.setOnPreferenceClickListener(this);
        mAuthorGithubPreference.setOnPreferenceClickListener(this);
        mMailboxPreference.setOnPreferenceClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (Settings.is_night_mode) {
            view.setBackgroundResource(R.color.nightColorPrimary);
        }
        return view;
    }

    public static void copy(String content, Context mContext) {
        ClipboardManager manager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("text", content);
        manager.setPrimaryClip(data);
        Toast.makeText(mContext, R.string.message_clipboard, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mAppicationBriefPreference) {
            ApplicationBriefActivity.startActivity(getActivity());
            return true;
        } else if (preference == mSharePreference) {
            shareMsg();
            return true;
        } else if (preference == mAuthorGithubPreference || preference == mMailboxPreference) {
            copy((String) preference.getSummary(), getActivity());
            return true;
        }
        return false;
    }

    /**
     * 分享app到应用
     */
    public void shareMsg() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        //标题
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享" + getResources().getString(R.string.app_name));
        //分享的内容
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_content));
        //分享的类型
        intent.setType("text/plain");
        //验证是否有应用响应
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
