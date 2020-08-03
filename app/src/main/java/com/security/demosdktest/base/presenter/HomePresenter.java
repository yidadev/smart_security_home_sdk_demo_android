package com.security.demosdktest.base.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;

import com.security.demosdktest.R;
import com.security.demosdktest.base.app.Constant;
import com.security.demosdktest.base.fragment.DeviceListFragment;
import com.security.demosdktest.base.fragment.PersonalCenterFragment;
import com.security.demosdktest.base.fragment.SceneFragment;
import com.security.demosdktest.base.fragment.SecurityFragment;
import com.security.demosdktest.base.utils.ActivityUtils;
import com.security.demosdktest.base.utils.CollectionUtils;
import com.security.demosdktest.base.utils.DialogUtil;
import com.security.demosdktest.base.view.IHomeView;
import com.security.demosdktest.config.AddDeviceTypeActivity;
import com.security.demosdktest.family.model.FamilyIndexModel;
import com.security.demosdktest.family.model.IFamilyIndexModel;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;

import java.util.List;

/**
 * Created by letian on 16/7/18.
 */
public class HomePresenter extends BasePresenter {

    public static final String TAG = "HomeKitPresenter";

    public static final String TAB_FRGMENT = "TAB_FRGMENT";

    private IHomeView mHomeView;
    protected Activity mActivity;

    public static final int TAB_MY_DEVICE = 0;
    public static final int TAB_PERSONAL_CENTER = 1;
    public static final int TAB_SCENE = 2;
    public static final int TAB_MY_SECURITY = 3;

    protected int mCurrentTab = -1;

    private IFamilyIndexModel mFamilyIndexModel;

    public HomePresenter(IHomeView homeView, Activity ctx) {
        mHomeView = homeView;
        mActivity = ctx;
        mFamilyIndexModel = new FamilyIndexModel(ctx);

    }

    public void checkFamilyCount() {
        mFamilyIndexModel.queryHomeList(new ITuyaGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> list) {
                if (null == mHomeView) {
                    return;
                }
                if (CollectionUtils.isEmpty(list)) {
                    Constant.finishActivity();
                    mHomeView.goToFamilyEmptyActivity();
                }
            }

            @Override
            public void onError(String s, String s1) {

            }
        });
    }

    //添加设备
    public void addDevice() {
        final WifiManager mWifiManager = (WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!mWifiManager.isWifiEnabled()) {
            DialogUtil.simpleConfirmDialog(mActivity, mActivity.getString(R.string.open_wifi), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            mWifiManager.setWifiEnabled(true);
                            gotoAddDevice();
                            break;
                    }
                }
            });
        } else {
            gotoAddDevice();
        }
    }

    //个人中心
    public void showPersonalCenterPage() {
        showTab(TAB_PERSONAL_CENTER);
    }

    //我的设备
    public void showMyDevicePage() {
        showTab(TAB_MY_DEVICE);
    }

    //安防
    public void showMySecurityPage() {
        showTab(TAB_MY_SECURITY);
    }

    //场景
    public void showScene() {
        showTab(TAB_SCENE);
    }

    public void gotoAddDevice() {
        ActivityUtils.gotoActivity(mActivity, AddDeviceTypeActivity.class, ActivityUtils.ANIMATE_SLIDE_TOP_FROM_BOTTOM, false);
    }

    public void showTab(int tab) {
        if (tab == mCurrentTab) {
            return;
        }

        mHomeView.offItem(mCurrentTab);

        mHomeView.onItem(tab);

        mCurrentTab = tab;
    }

    public int getFragmentCount() {
        return 4;
    }

    public Fragment getFragment(int type) {
        if (type == TAB_MY_DEVICE) {
            return DeviceListFragment.newInstance();
        } else if (type == TAB_PERSONAL_CENTER) {
            return PersonalCenterFragment.newInstance();
        } else if (type == TAB_SCENE) {
            return SceneFragment.newInstance();
        } else if (type == TAB_MY_SECURITY) {
            return SecurityFragment.newInstance();
        }
        return null;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public void setCurrentTab(int tab) {
        mCurrentTab = tab;
    }
}
