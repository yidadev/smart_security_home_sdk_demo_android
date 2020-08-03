package com.security.demosdktest.base.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONArray;
import com.security.demosdktest.base.app.Constant;
import com.security.demosdktest.base.view.ISecurityView;
import com.tuya.smart.android.base.utils.PreferencesUtil;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.optimus.sdk.TuyaOptimusSdk;
import com.tuya.smart.optimus.security.base.api.ITuyaSecurityBaseSdk;
import com.tuya.smart.optimus.security.base.api.bean.alarm.BypassDetailBean;
import com.tuya.smart.optimus.security.base.api.bean.alarm.SosAlarmType;
import com.tuya.smart.optimus.security.base.api.bean.armed.AlarmResult;
import com.tuya.smart.optimus.security.base.api.bean.armed.ArmModeStatus;
import com.tuya.smart.optimus.security.base.api.bean.armed.CountDownResult;
import com.tuya.smart.optimus.security.base.api.bean.armed.HomeInfoBean;
import com.tuya.smart.optimus.security.base.api.bean.armed.HomeModeGetBean;
import com.tuya.smart.optimus.security.base.api.bean.armed.HomeOnlineResult;
import com.tuya.smart.optimus.security.base.api.bean.armed.NormalResult;
import com.tuya.smart.optimus.security.base.api.bean.armed.PreModeResult;
import com.tuya.smart.optimus.security.base.api.bean.armed.RealModeResult;
import com.tuya.smart.optimus.security.base.api.iview.ITuyaGatewayListener;
import com.tuya.smart.optimus.security.base.api.iview.ITuyaProtocolListener;
import com.tuya.smart.optimus.security.base.api.iview.ITuyaSecurityArmed;
import com.tuya.smart.optimus.security.base.api.iview.ITuyaSecurityGateway;

import java.util.ArrayList;

public class SecurityPresenter extends BasePresenter implements ITuyaProtocolListener, ITuyaGatewayListener {
    private ISecurityView mView;
    private Context mContext;
    private ITuyaSecurityBaseSdk mSecurityBaseSdk;
    ITuyaSecurityArmed iTuyaSecurityArmed;
    ITuyaSecurityGateway iTuyaSecurityGateway;
    private int mQueryCount;
    private String mMode;
    private int ingoreDevicesCount = 0;
    private boolean isTimeOut = false;
    Long ingoreTimeout;
    final Handler handler = new Handler();

    public SecurityPresenter(Context context, ISecurityView view) {
        mView = view;
        mContext = context;
        Constant.HOME_ID = PreferencesUtil.getLong("homeId", Constant.HOME_ID);
        mSecurityBaseSdk = TuyaOptimusSdk.getManager(ITuyaSecurityBaseSdk.class);
        iTuyaSecurityGateway = mSecurityBaseSdk.newGatewayInstance(Constant.HOME_ID);
        iTuyaSecurityGateway.registerGatewayListener(this);
        iTuyaSecurityGateway.registerProtocolListener(this);
        iTuyaSecurityArmed = mSecurityBaseSdk.newArmedInstance(Constant.HOME_ID);
    }

    public void getHomeAlarmStateWithHomeId() {
        iTuyaSecurityArmed.getHomeAlarmStateWithHomeId(new ITuyaResultCallback<HomeInfoBean>() {
            @Override
            public void onSuccess(HomeInfoBean result) {
                mView.setHomeState(result.getOnlineState() == 1);
                mView.setAlarmCountDownTimeText(result.getAlarmCountdown() >= 0 ? result.getAlarmCountdown() : 0L);
                if (result.getAlarmState() == 1) {
                    if (result.getAlarmCountdown() == 0) {
                        mView.setAlarmState(true);
                    } else {
                        mView.setAlarmState(false);
                    }
                } else {
                    mView.setAlarmState(false);
                    if (result.getOnlineState() == 1) {
                        getHomeModeWithHomeId();
                    }
                }
            }

            @Override
            public void onError(String errorCode, String errorMessage) {

            }
        });

    }

    public void startSOSAlarmWithHomeId() {
        iTuyaSecurityArmed.startSOSAlarmWithHomeId(SosAlarmType.SOS_FIRE, new ITuyaResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {

            }

            @Override
            public void onError(String errorCode, String errorMessage) {

            }
        });
    }

    private void getHomeModeWithHomeId() {
        iTuyaSecurityArmed.getHomeModeWithHomeId(new ITuyaResultCallback<HomeModeGetBean>() {
            @Override
            public void onSuccess(HomeModeGetBean result) {
                mView.setState(Integer.parseInt(result.getMode()));
                if (result.getMode().equals("0")) {
                    mView.setAlarmState(false);
                    mView.setAlarmCountDownTimeText(0L);
                    mView.setCountDownTimeText(0L);
                }
                mHandler.removeCallbacksAndMessages(null);
                countDownTime(result.getEnterTime());
            }

            @Override
            public void onError(String errorCode, String errorMessage) {

            }
        });
    }

    private void countDownTime(long enterTime) {
        Long remainingTime = enterTime - System.currentTimeMillis();
        if (remainingTime <= 0) {
            mHandler.removeCallbacksAndMessages(null);
            mView.setCountDownTimeText(0L);
        } else {
            final CountDownTimeRunnable r = new CountDownTimeRunnable(remainingTime / 1000, mHandler, mView);
            mHandler.post(r);
        }
    }

    public void queryIgnoreDeviceFromGateway(final ArmModeStatus mode) {
        mMode = mode.getValue();
        iTuyaSecurityGateway.getLocationGateWayDeviceWithHomeId(new ITuyaResultCallback<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                if (result != null) {
                    mQueryCount = result.size();
                    ingoreDevicesCount = 0;
                    if (mQueryCount <= 0) {

                    } else {
                        for (int i = 0; i < result.size(); i++) {
                            iTuyaSecurityGateway.queryIgnoreDeviceFromGateway(result.get(i), mode, new ITuyaResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean result) {
                                    isTimeOut = false;
                                    ingoreTimeout = 3L;
                                    final Runnable r = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (ingoreTimeout > 0L) {
                                                handler.postDelayed(this, 1000);
                                            } else {
                                                isTimeOut = true;
                                                handler.removeCallbacksAndMessages(null);
                                                switchHomeModeWithHomeId(mMode);
                                            }
                                            ingoreTimeout -= 1;
                                        }
                                    };
                                    handler.post(r);
                                }

                                @Override
                                public void onError(String errorCode, String errorMessage) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onError(String errorCode, String errorMessage) {

            }
        });
    }

    public void switchHomeModeWithHomeId(String mode) {
        if ("0".equals(mode)) {
            ingoreDevicesCount = 0;
        }
        iTuyaSecurityArmed.switchHomeModeWithHomeId(mode, new ITuyaResultCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mHandler.removeCallbacksAndMessages(null);
                mView.setCountDownTimeText(0L);
                mView.showProgressBar();
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                mView.showProgressBar();
            }
        });
    }

    public void queryLocationBypssDevicesWithHomeId() {
        iTuyaSecurityArmed.queryLocationBypssDevicesWithHomeId(new ITuyaResultCallback<ArrayList<BypassDetailBean>>() {
            @Override
            public void onSuccess(ArrayList<BypassDetailBean> result) {
                mView.showIngoreDevices(result);
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
//        final NormalResult.DataBean data = result.getData();
//        if (!"0".equals(data.getType())) {
        queryLocationBypssDevicesWithHomeId();
//        } else {
//        }
    }

    /**
     * 52通知处理报警倒计时回调
     *
     * @param result 回调参数
     */
    @Override
    public void onProcessAlarmCountDownNotice(CountDownResult result) {
        getHomeAlarmStateWithHomeId();
    }

    /**
     * 52通知处理防拆报警回调
     */
    @Override
    public void onProcessTamperAlarmNotice() {
        mView.setAlarmState(true);
//        mView.setAlarmCountDownTimeText(0L);
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
        mView.setAlarmState(true);
//        getGateWayState();
    }

    /**
     * 52通知处理更新报警消息回调
     */
    @Override
    public void onProcessUpdateAlarmNotice() {
//        getGateWayState();
    }

    /**
     * 52通知处理报警通知回调
     *
     * @param result 回调参数
     */
    @Override
    public void onProcessAlarmStateNotice(AlarmResult result) {
        final AlarmResult.DataBean data = result.getData();
        if ((Constant.HOME_ID + "").equals(data.getGid())) {
            mView.setAlarmState(data.getState() == 1);
            if (data.getState() == 0) {
                getHomeModeWithHomeId();
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
        mView.setHomeState(result.getData().getOnlineState() == 1);
    }

    /**
     * 52通知处理布撤防预处理通知回调
     *
     * @param result 回调参数
     */
    @Override
    public void onProcessPreModeSwitchNotice(PreModeResult result) {
        final PreModeResult.DataBean data = result.getData();
        if ((Constant.HOME_ID + "").equals(data.getHomeId())) {
            countDownTime(data.getEnterTime());
        }
        mView.hideProgressBar();
    }

    /**
     * 52通知处理布撤防通知回调
     *
     * @param result 回调参数
     */
    @Override
    public void onProcessRealModeSwitchNotice(RealModeResult result) {
        final RealModeResult.DataBean data = result.getData();
        if ((Constant.HOME_ID + "").equals(data.getHomeId())) {
            getHomeModeWithHomeId();
//            mView.setState(Integer.parseInt(data.getMode()));
            mView.hideProgressBar();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iTuyaSecurityArmed.onDestroy();
        mSecurityBaseSdk.newGatewayInstance(Constant.HOME_ID).unRegisterProtocolListener(this);
    }

    @Deprecated
    @Override
    public void onSecurityModeWillSwitchAfterDelay(int countDownTime) {

    }

    @Deprecated
    @Override
    public void didReceiveUnusualDevice(ArrayList<String> deviceIds) {

    }

    @Override
    public void onIgnoreDeviceFromGateway(JSONArray devices) {
        mQueryCount--;
        ingoreDevicesCount += devices.size();
        if (!isTimeOut) {
            if (mQueryCount == 0) {
                if (ingoreDevicesCount > 0) {
                    handler.removeCallbacksAndMessages(null);
                    mView.gotoBypassActivity(mMode);
                } else {
                    switchHomeModeWithHomeId(mMode);
                }
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return super.handleMessage(msg);
    }

    class CountDownTimeRunnable implements Runnable {

        private Long mTime;
        private Handler mHandler;
        private ISecurityView mView;

        public CountDownTimeRunnable(Long time, Handler handler) {
            mTime = time;
            mHandler = handler;
        }

        public CountDownTimeRunnable(Long time, Handler handler, ISecurityView view) {
            mTime = time;
            mHandler = handler;
            mView = view;
        }

        public Long getTime() {
            return mTime;
        }

        public void setTime(Long time) {
            mTime = time;
        }

        @Override
        public void run() {
            if (mTime > 0) {
                mHandler.postDelayed(this, 1000);
            } else {
                mHandler.removeCallbacksAndMessages(null);
            }
            if (mView != null) {
                mView.setCountDownTimeText(mTime);
            }
            mTime -= 1;
        }
    }

}

