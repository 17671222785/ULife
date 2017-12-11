package com.andywang.ulife.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andywang.ulife.R;
import com.andywang.ulife.entity.calendar.cons.DPMode;
import com.andywang.ulife.entity.calendar.views.MonthView;
import com.andywang.ulife.entity.calendar.views.WeekView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by andyWang on 2017/10/14 0014.
 * 邮箱：393656489@qq.com
 */

public class CalendarFragment extends Fragment implements MonthView.OnDateChangeListener, MonthView.OnDatePickedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.month_calendar)
    MonthView monthView;
    @BindView(R.id.week_text)
    MyTextView weekText;
    @BindView(R.id.content_layout)
    LinearLayout contentLayout;
    @BindView(R.id.week_calendar)

    WeekView weekView;
    Unbinder unbinder;
    private Calendar now;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, null);
        unbinder = ButterKnife.bind(this, view);

        now = Calendar.getInstance();
        toolbar.setTitle(now.get(Calendar.YEAR) + "." + (now.get(Calendar.MONTH) + 1));
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        monthView.setDPMode(DPMode.SINGLE);
        monthView.setDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        monthView.setFestivalDisplay(true);
        monthView.setTodayDisplay(true);
        monthView.setOnDateChangeListener(this);
        monthView.setOnDatePickedListener(this);

        weekView.setDPMode(DPMode.SINGLE);
        weekView.setDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        weekView.setFestivalDisplay(true);
        weekView.setTodayDisplay(true);
        weekView.setOnDatePickedListener(this);
        for(int i = 0; i< 20; i++) {
            ContentItemViewAbs cia = new ContentItemViewAbs(getActivity());
            contentLayout.addView(cia);
        }
        return view;
    }

    @Override
    public void onDateChange(int year, int month) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar toolbar = activity.getSupportActionBar();
        if (null != toolbar)
            toolbar.setTitle(year + "." + month);
    }

    @Override
    public void onDatePicked(String date) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat format2 = new SimpleDateFormat("EEEE");
            Date chooseDate = format1.parse(date);
            weekText.setText(format2.format(chooseDate));
            if (date.equals(now.get(Calendar.YEAR) + "." + (now.get(Calendar.MONTH) + 1) + "." + now.get(Calendar.DAY_OF_MONTH))) {
                weekText.setVisibility(View.INVISIBLE);
            } else {
                weekText.setVisibility(View.VISIBLE);
            }
            contentLayout.removeAllViews();
            for(int i = 0; i< 2; i++) {
                ContentItemViewAbs cia = new ContentItemViewAbs(getActivity());
                contentLayout.addView(cia);
            }
            Toast.makeText(getActivity(), "" + date, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }
}
