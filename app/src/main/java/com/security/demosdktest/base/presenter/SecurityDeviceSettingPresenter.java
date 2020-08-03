package com.security.demosdktest.base.presenter;

import android.content.Context;

import com.security.demosdktest.R;
import com.security.demosdktest.base.app.Constant;
import com.security.demosdktest.base.view.ISecurityDeviceSettingView;
import com.tuya.smart.android.base.utils.PreferencesUtil;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.optimus.sdk.TuyaOptimusSdk;
import com.tuya.smart.optimus.security.base.api.ITuyaSecurityBaseSdk;
import com.tuya.smart.optimus.security.base.api.bean.mode.ModeSettingDeviceBean;
import com.tuya.smart.optimus.security.base.api.bean.mode.SaveModeSettingBean;
import com.tuya.smart.optimus.security.base.api.iview.ITuyaSecurityModeSetting;

import java.util.ArrayList;

public class SecurityDeviceSettingPresenter extends BasePresenter {

    ISecurityDeviceSettingView mView;
    private Context mContext;
    private ITuyaSecurityBaseSdk mSecurityBaseSdk;
    private ITuyaSecurityModeSetting iTuyaSecurityModeSetting;


    public SecurityDeviceSettingPresenter(Context context, ISecurityDeviceSettingView view) {
        mContext = context;
        mView = view;
        Constant.HOME_ID = PreferencesUtil.getLong("homeId", Constant.HOME_ID);
        mSecurityBaseSdk = TuyaOptimusSdk.getManager(ITuyaSecurityBaseSdk.class);
        iTuyaSecurityModeSetting = mSecurityBaseSdk.newModeSettingInstance(Constant.HOME_ID);
    }

    public void getSecurityDevice(String mode) {
        iTuyaSecurityModeSetting.getDeviceListWithMode(mode, new ITuyaResultCallback<ArrayList<ModeSettingDeviceBean>>() {
            @Override
            public void onSuccess(ArrayList<ModeSettingDeviceBean> result) {
                mView.setDeviceSecurity(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                mView.showToast(errorMessage);
            }
        });
    }

    public void save(String mode, ArrayList<SaveModeSettingBean> data) {
        iTuyaSecurityModeSetting.saveDeviceListWithMode(mode, data, new ITuyaResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                mView.showToast(mContext.getString(R.string.success));
                mView.finishActivity();
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                mView.showToast(errorMessage);
            }
        });
    }

}
