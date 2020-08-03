package com.security.demosdktest.base.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.security.demosdktest.R;
import com.security.demosdktest.base.presenter.SecurityArmedTimeSettingPresenter;
import com.security.demosdktest.base.view.ISecurityArmedTimeSettingView;
import com.tuya.smart.optimus.security.base.api.bean.mode.DelayDateBean;

import java.util.ArrayList;

public class SecurityArmedTimeSettingActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, ISecurityArmedTimeSettingView {

    private static final int[] time = {0, 30, 60, 90, 120, 150, 180};

    private static final String[] timeText = {"0s", "30s", "60s", "90s", "120s", "150s", "180s"};
    private Spinner mSpinnerArmedDelay;
    private Spinner mSpinnerAlarmDelay;
    private ArrayAdapter<String> adapter;
    private SecurityArmedTimeSettingPresenter mPresenter;
    private String mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_arm_time_setting);
        initIntent();
        initToolbar();
        initMenu();
        mSpinnerAlarmDelay = findViewById(R.id.spinner_alarm_delay);
        mSpinnerArmedDelay = findViewById(R.id.spinner_armed_delay);
        adapter = new ArrayAdapter<>(this, R.layout.activity_spinner_item, timeText);
        mSpinnerAlarmDelay.setAdapter(adapter);
        mSpinnerAlarmDelay.setOnItemSelectedListener(this);
        mSpinnerArmedDelay.setAdapter(adapter);
        mSpinnerArmedDelay.setOnItemSelectedListener(this);
        initPresenter();
    }

    private void initMenu() {
        setTitle("1".equals(mMode) ? "Stay" : "Away");
        setDisplayHomeAsUpEnabled();
        setMenu(R.menu.toolbar_save, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mPresenter.saveDelayTimeWithDelayRule(mMode, time[mSpinnerArmedDelay.getSelectedItemPosition()], time[mSpinnerAlarmDelay.getSelectedItemPosition()]);
                return false;
            }
        });
    }

    private void initIntent() {
        mMode = getIntent().getStringExtra("mode");
    }

    private void initPresenter() {
        mPresenter = new SecurityArmedTimeSettingPresenter(this, this);
        mPresenter.getAllModeDelayRuleWithHomeId();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_alarm_delay:
                break;
            case R.id.spinner_armed_delay:
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void setDelayTimeTime(ArrayList<DelayDateBean> result) {
        for (DelayDateBean delayDateBean : result) {
            if (mMode.equals(delayDateBean.getMode())) {
                final int enableDelayTime = delayDateBean.getEnableDelayTime();
                final int alarmDelayTime = delayDateBean.getAlarmDelayTime();
                for (int i = 0; i < time.length; i++) {
                    if (time[i] == enableDelayTime) {
                        mSpinnerArmedDelay.setSelection(i);
                    }
                    if (time[i] == alarmDelayTime) {
                        mSpinnerAlarmDelay.setSelection(i);
                    }
                }
            }
        }
    }
}
