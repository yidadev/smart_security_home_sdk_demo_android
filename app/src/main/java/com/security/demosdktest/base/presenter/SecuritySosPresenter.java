package com.security.demosdktest.base.presenter;

import android.content.Context;

import com.security.demosdktest.base.app.Constant;
import com.security.demosdktest.base.view.ISecuritySosView;
import com.tuya.smart.android.base.utils.PreferencesUtil;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.optimus.sdk.TuyaOptimusSdk;
import com.tuya.smart.optimus.security.base.api.ITuyaSecurityBaseSdk;
import com.tuya.smart.optimus.security.base.api.bean.alarm.AlarmMessageBean;
import com.tuya.smart.optimus.security.base.api.bean.armed.AlarmResult;
import com.tuya.smart.optimus.security.base.api.bean.armed.CountDownResult;
import com.tuya.smart.optimus.security.base.api.bean.armed.HomeOnlineResult;
import com.tuya.smart.optimus.security.base.api.bean.armed.NormalResult;
import com.tuya.smart.optimus.security.base.api.bean.armed.PreModeResult;
import com.tuya.smart.optimus.security.base.api.bean.armed.RealModeResult;
import com.tuya.smart.optimus.security.base.api.iview.ITuyaProtocolListener;
import com.tuya.smart.optimus.security.base.api.iview.ITuyaSecurityAlarm;

import java.util.ArrayList;

public class SecuritySosPresenter extends BasePresenter implements ITuyaProtocolListener {

    private ISecuritySosView mView;
    private Context mContext;
    private ITuyaSecurityBaseSdk mSecurityBaseSdk;
    ITuyaSecurityAlarm iTuyaSecurityAlarm;

    public SecuritySosPresenter(Context context, ISecuritySosView view) {
        mContext = context;
        mView = view;
        Constant.HOME_ID = PreferencesUtil.getLong("homeId", Constant.HOME_ID);
        mSecurityBaseSdk = TuyaOptimusSdk.getManager(ITuyaSecurityBaseSdk.class);
        mSecurityBaseSdk.newGatewayInstance(Constant.HOME_ID).registerProtocolListener(this);
        iTuyaSecurityAlarm = mSecurityBaseSdk.newAlarmInstance(Constant.HOME_ID);
        getAlarmMessageListWithHomeId();
    }

    public void updateLocationAlarmStatusWithHomeId(Integer state) {
        iTuyaSecurityAlarm.updateLocationAlarmStatusWithHomeId(state, new ITuyaResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                mView.finishActivity();
            }

            @Override
            public void onError(String errorCode, String errorMessage) {

            }
        });
    }

    public void getAlarmMessageListWithHomeId() {
        iTuyaSecurityAlarm.getAlarmMessageListWithHomeId(new ITuyaResultCallback<ArrayList<AlarmMessageBean>>() {
            @Override
            public void onSuccess(ArrayList<AlarmMessageBean> result) {
                mView.updateMessage(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {

            }
        });
    }

    /**
     * 52通知处理Bypass回调
     *
     * @param result 回调参数
     */
    @Override
    public void onProcessBypassNotice(NormalResult result) {

    }

    /**
     * 52通知处理报警倒计时回调
     *
     * @param result 回调参数
     */
    @Override
    public void onProcessAlarmCountDownNotice(CountDownResult result) {

    }

    /**
     * 52通知处理防拆报警回调
     */
    @Override
    public void onProcessTamperAlarmNotice() {
        getAlarmMessageListWithHomeId();
    }

    /**
     * 52通知处理添加网关回调
     */
    @Override
    public void onProcessAddGatewayNotice() {

    }

    /**
     * 52通知处理新报警回调
     *
     * @param result 回调参数
     */
    @Override
    public void onProcessNewAlarmNotice(AlarmResult result) {
        getAlarmMessageListWithHomeId();
    }


    /**
     * 52通知处理更新报警消息回调
     */
    @Override
    public void onProcessUpdateAlarmNotice() {
        getAlarmMessageListWithHomeId();
    }

    /**
     * 52通知处理报警通知回调
     *
     * @param result 回调参数
     */
    @Override
    public void onProcessAlarmStateNotice(AlarmResult result) {
        final AlarmResult.DataBean data = result.getData();
        if (data.getGid().equals(Constant.HOME_ID + "")) {
            if (data.getState() == 0) {
                mView.finishActivity();
            }
        }
    }

    /**
     * 52通知处理报警声音通知回调
     *
     * @param result 回调参数
     */
    @Override
    public void onProcessAlarmVoiceNotice(AlarmResult result) {

    }

    /**
     * 52通知处理家庭在离线通知回调
     *
     * @param result 回调参数
     */
    @Override
    public void onProcessHomeOnlineNotice(HomeOnlineResult result) {

    }

    /**
     * 52通知处理布撤防预处理通知回调
     *
     * @param result 回调参数
     */
    @Override
    public void onProcessPreModeSwitchNotice(PreModeResult result) {

    }

    /**
     * 52通知处理布撤防通知回调
     *
     * @param result 回调参数
     */
    @Override
    public void onProcessRealModeSwitchNotice(RealModeResult result) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSecurityBaseSdk.newGatewayInstance(Constant.HOME_ID).unRegisterProtocolListener(this);
    }
}
