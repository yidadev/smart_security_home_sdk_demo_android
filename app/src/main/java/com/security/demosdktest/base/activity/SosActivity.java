package com.security.demosdktest.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.security.demosdktest.R;
import com.security.demosdktest.base.presenter.SecuritySosPresenter;
import com.security.demosdktest.base.view.ISecuritySosView;
import com.security.demosdktest.base.view.ISecurityView;
import com.tuya.smart.optimus.security.base.api.bean.alarm.AlarmMessageBean;

import java.util.ArrayList;

public class SosActivity extends AppCompatActivity implements View.OnClickListener, ISecuritySosView {

    private TextView mTvCancelAlarm;
    private TextView mTvDisarmed;
    private SecuritySosPresenter mPresenter;
    private TextView mTvSosContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_sos);
        mTvCancelAlarm = findViewById(R.id.tv_cancel_alarm);
        mTvDisarmed = findViewById(R.id.tv_disarmed);
        mTvSosContent = findViewById(R.id.tv_sos_content);
        mTvCancelAlarm.setOnClickListener(this);
        mTvDisarmed.setOnClickListener(this);
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new SecuritySosPresenter(this, this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel_alarm:
                mPresenter.updateLocationAlarmStatusWithHomeId(0);
                break;
            case R.id.tv_disarmed:
                mPresenter.updateLocationAlarmStatusWithHomeId(1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void updateMessage(ArrayList<AlarmMessageBean> result) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            sb.append(result.get(i).getTypeDesc()).append("\n");
        }
        mTvSosContent.setText(sb.toString());
    }
}
