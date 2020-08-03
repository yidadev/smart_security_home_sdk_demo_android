package com.security.demosdktest.base.view;

import com.tuya.smart.optimus.security.base.api.bean.alarm.BypassDetailBean;

import java.util.ArrayList;

public interface ISecurityView {

    void setState(int mode);

    void setHomeState(boolean isOnline);

    void showProgressBar();

    void hideProgressBar();

    void setCountDownTimeText(Long time);

    void setAlarmCountDownTimeText(Long time);

    void setAlarmState(boolean isAlarm);

    void gotoBypassActivity(String mode);

    void showIngoreDevices(ArrayList<BypassDetailBean> result);
}
