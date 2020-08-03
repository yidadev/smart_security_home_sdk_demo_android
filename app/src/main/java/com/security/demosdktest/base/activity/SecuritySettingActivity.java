package com.security.demosdktest.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.security.demosdktest.R;

public class SecuritySettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvStay;
    private TextView mTvAway;
    private TextView mTvDelayStay;
    private TextView mTvDelayAway;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_setting);
        initToolbar();
        setTitle("Security Setting");
        setDisplayHomeAsUpEnabled();

        mTvStay = findViewById(R.id.tv_stay);
        mTvAway = findViewById(R.id.tv_away);
        mTvDelayStay = findViewById(R.id.tv_delay_stay);
        mTvDelayAway = findViewById(R.id.tv_delay_away);

        mTvStay.setOnClickListener(this);
        mTvAway.setOnClickListener(this);
        mTvDelayStay.setOnClickListener(this);
        mTvDelayAway.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_stay:
                final Intent intent = new Intent(this, SecurityDeviceSettingActivity.class);
                intent.putExtra("mode", "1");
                startActivity(intent);
                break;
            case R.id.tv_away:
                final Intent intent1 = new Intent(this, SecurityDeviceSettingActivity.class);
                intent1.putExtra("mode", "2");
                startActivity(intent1);
                break;
            case R.id.tv_delay_away:
                final Intent intent2 = new Intent(this, SecurityArmedTimeSettingActivity.class);
                intent2.putExtra("mode", "2");
                startActivity(intent2);
                break;
            case R.id.tv_delay_stay:
                final Intent intent3 = new Intent(this, SecurityArmedTimeSettingActivity.class);
                intent3.putExtra("mode", "1");
                startActivity(intent3);
                break;
            default:
                break;
        }
    }
}
