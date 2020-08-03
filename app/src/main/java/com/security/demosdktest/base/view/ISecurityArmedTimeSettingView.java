package com.security.demosdktest.base.view;

import com.tuya.smart.optimus.security.base.api.bean.mode.DelayDateBean;

import java.util.ArrayList;

public interface ISecurityArmedTimeSettingView {

    void setDelayTimeTime(ArrayList<DelayDateBean> result);

    void finishActivity();

    void showToast(String msg);
}
