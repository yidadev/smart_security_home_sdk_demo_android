package com.security.demosdktest.base.presenter;

import android.content.Context;

import com.security.demosdktest.base.app.Constant;
import com.security.demosdktest.base.view.ISecurityArmedTimeSettingView;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.optimus.sdk.TuyaOptimusSdk;
import com.tuya.smart.optimus.security.base.api.ITuyaSecurityBaseSdk;
import com.tuya.smart.optimus.security.base.api.bean.mode.DelayDateBean;
import com.tuya.smart.optimus.security.base.api.iview.ITuyaSecurityModeSetting;

import java.util.ArrayList;

public class SecurityArmedTimeSettingPresenter extends BasePresenter {

    private ISecurityArmedTimeSettingView mView;
    private Context mContext;
    private ITuyaSecurityBaseSdk mSecurityBaseSdk;
    ITuyaSecurityModeSetting iTuyaSecurityModeSetting;

    public SecurityArmedTimeSettingPresenter(Context context, ISecurityArmedTimeSettingView view) {
        mContext = context;
        mView = view;
        mSecurityBaseSdk = TuyaOptimusSdk.getManager(ITuyaSecurityBaseSdk.class);
        iTuyaSecurityModeSetting = mSecurityBaseSdk.newModeSettingInstance(Constant.HOME_ID);
    }

    public void getAllModeDelayRuleWithHomeId() {
        iTuyaSecurityModeSetting.getAllModeDelayRuleWithHomeId(new ITuyaResultCallback<ArrayList<DelayDateBean>>() {
            @Override
            public void onSuccess(ArrayList<DelayDateBean> result) {
                mView.setDelayTimeTime(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                mView.showToast(errorCode + "  " + errorMessage);
            }
        });
    }

    public void saveDelayTimeWithDelayRule(String mode, int enableTime, int alarmDelay) {
        iTuyaSecurityModeSetting.saveDelayTimeWithDelayRule(mode, enableTime, alarmDelay, new ITuyaResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                mView.finishActivity();
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                mView.showToast(errorCode + "  " + errorMessage);
            }
        });
    }
}
