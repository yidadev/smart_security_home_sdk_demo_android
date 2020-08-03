package com.security.demosdktest.base.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.security.demosdktest.R;
import com.security.demosdktest.base.app.Constant;
import com.security.demosdktest.base.presenter.SecurityIngorePresenter;
import com.security.demosdktest.base.view.ISecurityIngoreView;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.optimus.security.base.api.bean.armed.ArmModeStatus;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

public class SecurityIngoreActivity extends BaseActivity implements ISecurityIngoreView, View.OnClickListener {

    private TextView mTvIngoreDevice;
    private TextView mTvIngoreArmed;
    private SecurityIngorePresenter mPresenter;
    String mode;
    StringBuilder ingoreContent = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_ingore);
        initIntent();
        mTvIngoreDevice = findViewById(R.id.tv_ingore_device);
        mTvIngoreArmed = findViewById(R.id.tv_ingore_armed);
        mTvIngoreArmed.setOnClickListener(this);
        initPresenter();
    }

    private void initIntent() {
        mode = getIntent().getStringExtra("mode");
    }

    private void initPresenter() {
        mPresenter = new SecurityIngorePresenter(this, this);
        if (!TextUtils.isEmpty(mode)) {
            mPresenter.queryIgnoreDeviceFromGateway(ArmModeStatus.valueOfMode(mode));
        }
    }

    @Override
    public void setBypassText(String text) {
        final List<DeviceBean> homeDeviceList = TuyaHomeSdk.getDataInstance().getHomeDeviceList(Constant.HOME_ID);
        DeviceBean deviceBean = null;
        if (homeDeviceList != null) {
            for (DeviceBean device : homeDeviceList) {
                if (text.equals(device.getNodeId())) {
                    deviceBean = device;
                    break;
                }
            }
        }
        if (deviceBean != null) {
            ingoreContent.append(deviceBean.getName()).append("\n");
        }
        mTvIngoreDevice.setText(ingoreContent.toString());
    }

    @Override
    public void refreshBypassText() {
        if (!TextUtils.isEmpty(mode)) {
            mPresenter.queryIgnoreDeviceFromGateway(ArmModeStatus.valueOfMode(mode));
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ingore_armed:
                mPresenter.switchHomeModeWithHomeId(mode);
                break;
            default:
                break;
        }
    }
}
