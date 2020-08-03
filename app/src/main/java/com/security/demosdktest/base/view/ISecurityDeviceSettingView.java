package com.security.demosdktest.base.view;

import com.tuya.smart.optimus.security.base.api.bean.mode.ModeSettingDeviceBean;

import java.util.ArrayList;

public interface ISecurityDeviceSettingView {

    void setDeviceSecurity(ArrayList<ModeSettingDeviceBean> list);

    void showToast(String message);

    void finishActivity();
}
