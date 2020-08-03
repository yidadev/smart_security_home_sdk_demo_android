package com.security.demosdktest.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.security.demosdktest.R;
import com.security.demosdktest.base.adapter.SecurityDeviceAdapter;
import com.security.demosdktest.base.presenter.SecurityDeviceSettingPresenter;
import com.security.demosdktest.base.view.ISecurityDeviceSettingView;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.optimus.security.base.api.bean.mode.ModeSettingDeviceBean;
import com.tuya.smart.optimus.security.base.api.bean.mode.SaveModeSettingBean;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecurityDeviceSettingActivity extends BaseActivity implements ISecurityDeviceSettingView {

    private RecyclerView mRvSecurityDevice;
    private SecurityDeviceAdapter mAdapter;
    private SecurityDeviceSettingPresenter mPresenter;
    String mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_security_setting);
        initIntent();
        initToolbar();
        initMenu();
        mRvSecurityDevice = findViewById(R.id.rv_security_device);
        mAdapter = new SecurityDeviceAdapter();
        mRvSecurityDevice.setLayoutManager(new LinearLayoutManager(this));
        mRvSecurityDevice.setAdapter(mAdapter);
        initPresenter();
    }

    private void initMenu() {
        setTitle("1".equals(mode) ? "Stay" : "Away");
        setDisplayHomeAsUpEnabled();
        setMenu(R.menu.toolbar_save, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_menu_save:
                        ArrayList<SaveModeSettingBean> saveModeSettingBeans = new ArrayList<>();
                        Map<String, ArrayList<String>> result = new HashMap<>();
                        final List<ModeSettingDeviceBean> deviceBeanList = mAdapter.getDeviceBeanList();
                        for (int i = 0; i < deviceBeanList.size(); i++) {
                            final ModeSettingDeviceBean modeSettingDeviceBean = deviceBeanList.get(i);
                            if (modeSettingDeviceBean.isSelected() && !modeSettingDeviceBean.isFixed()) {
                                final DeviceBean deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(modeSettingDeviceBean.getDeviceId());
                                if (modeSettingDeviceBean.isSub()) {
                                    ArrayList<String> subDeviceIds = result.get(deviceBean.getParentId());
                                    if (subDeviceIds != null) {
                                        subDeviceIds.add(deviceBean.getDevId());
                                    } else {
                                        subDeviceIds = new ArrayList<>();
                                        subDeviceIds.add(deviceBean.getDevId());
                                        result.put(deviceBean.getParentId(), subDeviceIds);
                                    }
                                } else {
                                    if (!result.keySet().contains(modeSettingDeviceBean.getDeviceId())) {
                                        result.put(modeSettingDeviceBean.getDeviceId(), new ArrayList<String>());
                                    }
                                }
                            }
                        }
                        for (Map.Entry<String, ArrayList<String>> entry : result.entrySet()) {
                            SaveModeSettingBean saveModeSettingBean = new SaveModeSettingBean();
                            saveModeSettingBean.setGatewayId(entry.getKey());
                            saveModeSettingBean.setDeviceIds(entry.getValue());
                            saveModeSettingBeans.add(saveModeSettingBean);
                        }
                        mPresenter.save(mode, saveModeSettingBeans);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initIntent() {
        mode = getIntent().getStringExtra("mode");
    }

    private void initPresenter() {
        mPresenter = new SecurityDeviceSettingPresenter(this, this);
        mPresenter.getSecurityDevice(mode);
    }


    @Override
    public void setDeviceSecurity(ArrayList<ModeSettingDeviceBean> list) {
        mAdapter.setDeviceBeanList(list);
        mAdapter.notifyDataSetChanged();
    }
}
