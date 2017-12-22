package com.andywang.ulife.view.fragment.settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andywang.ulife.R;
import com.andywang.ulife.callback.ClearCacheCallBack;
import com.andywang.ulife.customview.PreferenceWithTip;
import com.andywang.ulife.entity.calendar.bean.Settings;
import com.andywang.ulife.ui.AboutActivity;
import com.andywang.ulife.util.cache.CacheManager;
import com.andywang.ulife.util.style.LanguageChangeManager;
import com.andywang.ulife.util.style.ThemeChangeManager;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;

/**
 * Created by parting_soul on 2016/10/18.
 * settings fragment
 */

public class SettingsPreferenceFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    private ListPreference mLanguagePreference;

    private ListPreference mFontPreference;

    private CheckBoxPreference mNightModePreference;

    private CheckBoxPreference mExitBeSurePreference;

    private ListPreference mThemeChangePreference;

    private CheckBoxPreference mNoPicModePreference;

    private PreferenceWithTip mClearAllCachePreference;

    private Preference mResetPreference;

    private Preference mAboutPreference;

    private Settings mSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(Settings.SETTINGS_XML_NAME);
        //添加对应的Settings布局
        addPreferencesFromResource(R.xml.settings);

        mSettings = Settings.newsInstance();

        mLanguagePreference = (ListPreference) findPreference(Settings.LANUAGE_KEY);
        mFontPreference = (ListPreference) findPreference(Settings.FONT_SIZE_KEY);
        mThemeChangePreference = (ListPreference) findPreference(Settings.THEME_CHANGE_KEY);
        mNightModePreference = (CheckBoxPreference) findPreference(Settings.IS_NIGHT_KEY);
        mExitBeSurePreference = (CheckBoxPreference) findPreference(Settings.BACK_BY_TWICE_KEY);
        mNoPicModePreference = (CheckBoxPreference) findPreference(Settings.NO_PICTURE_KEY);
        mClearAllCachePreference = (PreferenceWithTip) findPreference(Settings.CLEAR_ALL_CACHE);
        mResetPreference = (Preference) findPreference(Settings.RESET_KEY);
        mAboutPreference = (Preference) findPreference(Settings.ABOUT_KEY);

        if (mLanguagePreference != null && mFontPreference != null && mNightModePreference != null
                && mExitBeSurePreference != null && mNoPicModePreference != null
                && mClearAllCachePreference != null) {
            LogUtils.d(CommonInfo.TAG, "-->1111111111" + mClearAllCachePreference);
        }


        mClearAllCachePreference.setTip(CacheManager.getCacheSize());

        mNightModePreference.setChecked(Settings.is_night_mode);
        mNoPicModePreference.setChecked(Settings.is_no_picture_mode);
        mExitBeSurePreference.setChecked(Settings.is_back_by_twice);

        mLanguagePreference.setOnPreferenceChangeListener(this);
        mFontPreference.setOnPreferenceChangeListener(this);
        mThemeChangePreference.setOnPreferenceChangeListener(this);
        mNightModePreference.setOnPreferenceChangeListener(this);
        mResetPreference.setOnPreferenceClickListener(this);
        mNoPicModePreference.setOnPreferenceChangeListener(this);
        mExitBeSurePreference.setOnPreferenceChangeListener(this);
        mClearAllCachePreference.setOnPreferenceClickListener(this);
        mAboutPreference.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mLanguagePreference) {
            LogUtils.d(CommonInfo.TAG, "mLanguagePreference " + mLanguagePreference.getValue());
            mSettings.putString(Settings.LANUAGE_KEY, newValue.toString());
            Settings.isRefresh = true;
            LanguageChangeManager.changeLanguage();
            getActivity().recreate();
            return true;
        } else if (preference == mFontPreference) {
            LogUtils.d(CommonInfo.TAG, "mFontPreference " + mFontPreference.getValue());
            mSettings.putString(Settings.FONT_SIZE_KEY, newValue.toString());
            Settings.isRefresh = true;
            return true;
        } else if (preference == mThemeChangePreference) {
            mSettings.putString(Settings.THEME_CHANGE_KEY, newValue.toString());
            ThemeChangeManager.changeTitleTheme(getActivity());
            Settings.isRefresh = true;
            getActivity().recreate();
            return true;
        } else if (preference == mNightModePreference) {
//            LogUtils.d(CommonInfo.TAG, "mNightModePreference " + mNightModePreference.isChecked());
            Settings.is_night_mode = Boolean.parseBoolean(newValue.toString());
            mSettings.putBoolean(Settings.IS_NIGHT_KEY, Settings.is_night_mode);
            Settings.isRefresh = true;
            getActivity().recreate();
            return true;
        } else if (preference == mExitBeSurePreference) {
            //           LogUtils.d(CommonInfo.TAG, "mExitBeSurePreference " + mExitBeSurePreference.isChecked());
            Settings.is_back_by_twice = Boolean.parseBoolean(newValue.toString());
            mSettings.putBoolean(Settings.BACK_BY_TWICE_KEY, Settings.is_back_by_twice);
            return true;
        } else if (preference == mNoPicModePreference) {
//            LogUtils.d(CommonInfo.TAG, "mNoPicModePreference " + mNoPicModePreference.isChecked());
            Settings.is_no_picture_mode = Boolean.parseBoolean(newValue.toString());
            mSettings.putBoolean(Settings.NO_PICTURE_KEY, Settings.is_no_picture_mode);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        if (preference == mResetPreference) {
            LogUtils.d(CommonInfo.TAG, "mRestPreference ");
            showInitAllSettingsDialog();
            return true;
        } else if (preference == mClearAllCachePreference) {
            LogUtils.d(CommonInfo.TAG, "mClearPicCachePreference " + mClearAllCachePreference.isSelectable());
            showClearPicCacheDialog();
            return true;
        } else if (preference == mAboutPreference) {
            AboutActivity.startActivity(getActivity());
            return true;
        }
        return false;
    }

    /**
     * 显示重置所有设置的对话框
     */
    private void showInitAllSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.warning).setMessage(R.string.init_settings_msg)
                .setPositiveButton(R.string.certain, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSettings.initAllSettings();
                        Settings.isRefresh = true;
                        LanguageChangeManager.changeLanguage();
                        getActivity().recreate();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    /**
     * 显示清除缓存的对话框
     */
    public void showClearPicCacheDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.tip).setMessage(R.string.tip_content)
                .setPositiveButton(R.string.certain, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog progressDialog = showProgressDialog();
                        CacheManager.clearAllCache(new ClearCacheCallBack() {
                            @Override
                            public void finish(boolean isSuccess) {
                                progressDialog.dismiss();
                                checkClearState(isSuccess);
                            }
                        });
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    /**
     * 检查清除是否成功
     *
     * @param isSuccess
     */
    private void checkClearState(boolean isSuccess) {
        if (isSuccess) {
            Toast.makeText(getActivity(), getResources().getText(R.string.clear_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.clear_failed), Toast.LENGTH_SHORT).show();
        }
        mClearAllCachePreference.setTip(CacheManager.getCacheSize());
    }

    private ProgressDialog showProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle(R.string.tip);
        dialog.setMessage(getResources().getString(R.string.clearing));
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (Settings.is_night_mode) {
            view.setBackgroundResource(R.color.nightColorPrimary);
        }
        return view;
    }
}
