package com.security.demosdktest.base.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.security.demosdktest.base.app.Constant;
import com.security.demosdktest.base.view.ISecurityIngoreView;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.api.ITuyaHomeDeviceStatusListener;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.optimus.sdk.TuyaOptimusSdk;
import com.tuya.smart.optimus.security.base.api.ITuyaSecurityBaseSdk;
import com.tuya.smart.optimus.security.base.api.bean.armed.ArmModeStatus;
import com.tuya.smart.optimus.security.base.api.iview.ITuyaGatewayListener;
import com.tuya.smart.optimus.security.base.api.iview.ITuyaSecurityArmed;
import com.tuya.smart.optimus.security.base.api.iview.ITuyaSecurityGateway;

import java.util.ArrayList;

public class SecurityIngorePresenter extends BasePresenter implements ITuyaGatewayListener, ITuyaHomeDeviceStatusListener {

    private ISecurityIngoreView mView;
    private Context mContext;
    private ITuyaSecurityBaseSdk mSecurityBaseSdk;
    ITuyaSecurityGateway iTuyaSecurityGateway;
    ITuyaSecurityArmed iTuyaSecurityArmed;


    public SecurityIngorePresenter(Context context, ISecurityIngoreView view) {
        mView = view;
        mContext = context;
        mSecurityBaseSdk = TuyaOptimusSdk.getManager(ITuyaSecurityBaseSdk.class);
        iTuyaSecurityGateway = mSecurityBaseSdk.newGatewayInstance(Constant.HOME_ID);
        iTuyaSecurityGateway.registerGatewayListener(this);
        iTuyaSecurityArmed = mSecurityBaseSdk.newArmedInstance(Constant.HOME_ID);
        TuyaHomeSdk.newHomeInstance(Constant.HOME_ID).registerHomeDeviceStatusListener(this);
    }

    public void queryIgnoreDeviceFromGateway(final ArmModeStatus mode) {
        iTuyaSecurityGateway.getLocationGateWayDeviceWithHomeId(new ITuyaResultCallback<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                for (int i = 0; i < result.size(); i++) {
                    iTuyaSecurityGateway.queryIgnoreDeviceFromGateway(result.get(i), mode, new ITuyaResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean result) {

                        }

                        @Override
                        public void onError(String errorCode, String errorMessage) {
                            mView.showToast(errorCode + "  " + errorMessage);
                        }
                    });
                }
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                mView.showToast(errorCode + "  " + errorMessage);
            }
        });
    }

    @Override
    public void onSecurityModeWillSwitchAfterDelay(int countDownTime) {

    }

    @Override
    public void didReceiveUnusualDevice(ArrayList<String> deviceIds) {

    }

    @Override
    public void onIgnoreDeviceFromGateway(JSONArray devices) {
        if (devices != null) {
            for (int i = 0; i < devices.size(); i++) {
                mView.setBypassText(devices.getString(i));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iTuyaSecurityGateway.unRegisterGatewayListener(this);
        TuyaHomeSdk.newHomeInstance(Constant.HOME_ID).unRegisterHomeDeviceStatusListener(this);
    }

    @Override
    public void onDeviceDpUpdate(String devId, String dpStr) {
        mView.refreshBypassText();
    }

    @Override
    public void onDeviceStatusChanged(String devId, boolean online) {

    }

    @Override
    public void onDeviceInfoUpdate(String devId) {

    }

    public void switchHomeModeWithHomeId(String mode) {
        iTuyaSecurityArmed.switchHomeModeWithHomeId(mode, new ITuyaResultCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mView.finishActivity();
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                mView.showToast(errorCode + "  " + errorMessage);
            }
        });
    }
}
