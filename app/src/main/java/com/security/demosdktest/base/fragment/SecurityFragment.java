package com.security.demosdktest.base.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.security.demosdktest.R;
import com.security.demosdktest.base.activity.SecurityIngoreActivity;
import com.security.demosdktest.base.activity.SecuritySettingActivity;
import com.security.demosdktest.base.activity.SosActivity;
import com.security.demosdktest.base.presenter.SecurityPresenter;
import com.security.demosdktest.base.utils.ProgressUtil;
import com.security.demosdktest.base.utils.ToastUtil;
import com.security.demosdktest.base.view.ISecurityView;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.optimus.security.base.api.bean.alarm.BypassDetailBean;
import com.tuya.smart.optimus.security.base.api.bean.armed.ArmModeStatus;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

public class SecurityFragment extends BaseFragment implements View.OnClickListener, ISecurityView {

    private volatile static SecurityFragment securityFragment;
    private String[] modes = new String[]{"Disarmed", "Stay", "Away"};
    private View mContentView;

    private TextView mTvStay;
    private TextView mTvDisarmed;
    private TextView mTvAway;
    private TextView mTvState;
    private TextView mTvHomeState;
    private TextView mTvCountTime;
    private TextView mTvAlarmCountTime;
    private TextView mTvAlarmState;
    private TextView mTvBypassDevices;

    private SecurityPresenter mPresenter;
    private StringBuilder bypassDevices;

    public static Fragment newInstance() {
        if (securityFragment == null) {
            synchronized (SecurityFragment.class) {
                if (securityFragment == null) {
                    securityFragment = new SecurityFragment();
                }
            }
        }
        return securityFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_security_list, container, false);
        initToolbar(mContentView);
        initMenu();
        mTvStay = mContentView.findViewById(R.id.tv_stay);
        mTvDisarmed = mContentView.findViewById(R.id.tv_disarmed);
        mTvAway = mContentView.findViewById(R.id.tv_away);
        mTvState = mContentView.findViewById(R.id.tv_state);
        mTvHomeState = mContentView.findViewById(R.id.tv_home_state);
        mTvCountTime = mContentView.findViewById(R.id.tv_home_counttime);
        mTvAlarmCountTime = mContentView.findViewById(R.id.tv_alarm_counttime);
        mTvAlarmState = mContentView.findViewById(R.id.tv_alarm_state);
        mTvBypassDevices = mContentView.findViewById(R.id.tv_ingore_device);
        mTvAway.setOnClickListener(this);
        mTvStay.setOnClickListener(this);
        mTvDisarmed.setOnClickListener(this);
        return mContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new SecurityPresenter(getActivity(), this);
        mPresenter.getHomeAlarmStateWithHomeId();
    }

    private void initMenu() {
        setTitle("Security");
        setMenu(R.menu.toolbar_security, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_sos:
                        mPresenter.startSOSAlarmWithHomeId();
                        break;
                    case R.id.action_setting:
                        startActivity(new Intent(getActivity(), SecuritySettingActivity.class));
                        break;
                    case R.id.action_geofence:
                    case R.id.action_event_recording:
                        ToastUtil.shortToast(getContext(), "will supprot soon");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_stay:
                mPresenter.queryIgnoreDeviceFromGateway(ArmModeStatus.STAYING);
                break;
            case R.id.tv_away:
                mPresenter.queryIgnoreDeviceFromGateway(ArmModeStatus.LEAVING);
                break;
            case R.id.tv_disarmed:
                mPresenter.switchHomeModeWithHomeId("0");
                break;
            default:
                break;
        }
    }

    @Override
    public void setState(int mode) {
        mTvState.setText("Family armed state：" + modes[mode]);
    }

    @Override
    public void setHomeState(boolean isOnline) {
        mTvHomeState.setText("Family online state: " + (isOnline ? "Online" : "Offline"));
    }

    @Override
    public void showProgressBar() {
        ProgressUtil.showLoading(getActivity(), "");
    }

    @Override
    public void hideProgressBar() {
        ProgressUtil.hideLoading();
    }

    @Override
    public void setCountDownTimeText(final Long time) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvCountTime.setText("Family armed count down: " + time + "s");
            }
        });
    }

    @Override
    public void setAlarmCountDownTimeText(Long time) {
        mTvAlarmCountTime.setText("Family alarm count down：" + time + "s");
    }

    @Override
    public void setAlarmState(boolean isAlarm) {
        mTvAlarmState.setText("Family alarm state：" + isAlarm);
        if (isAlarm) {
            getActivity().startActivity(new Intent(getActivity(), SosActivity.class));
        }
    }

    @Override
    public void gotoBypassActivity(String mode) {
        final Intent intent = new Intent(getActivity(), SecurityIngoreActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }

    @Override
    public void showIngoreDevices(ArrayList<BypassDetailBean> result) {
        bypassDevices = new StringBuilder();
        bypassDevices.append("bypass devices: ").append("\n");
        for (BypassDetailBean bypassDetailBean : result) {
            final List<String> bypassDeviceIds = bypassDetailBean.getBypassDeviceIds();
            for (String subDevIds : bypassDeviceIds) {
                DeviceBean deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(subDevIds);
                if (deviceBean != null) {
                    bypassDevices.append(deviceBean.getName()).append("\n");
                }
            }
        }
        mTvBypassDevices.setText(bypassDevices);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
