package com.security.demosdktest.base.view;

import com.tuya.smart.optimus.security.base.api.bean.alarm.AlarmMessageBean;

import java.util.ArrayList;

public interface ISecuritySosView {

        void finishActivity();

        void updateMessage(ArrayList<AlarmMessageBean> result);
}
