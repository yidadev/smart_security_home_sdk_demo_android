package com.security.demosdktest.base.presenter;

import android.app.Activity;
import android.content.Context;

import com.security.demosdktest.base.activity.PersonalInfoActivity;
import com.security.demosdktest.base.utils.ActivityUtils;
import com.security.demosdktest.personal.IPersonalCenterModel;
import com.security.demosdktest.personal.IPersonalCenterView;
import com.security.demosdktest.personal.PersonalCenterModel;
import com.security.demosdktest.personal.PersonalInfoEvent;
import com.security.demosdktest.personal.PersonalInfoEventModel;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.sdk.TuyaSdk;


/**
 * Created by letian on 15/6/1.
 */
public class PersonalCenterFragmentPresenter extends BasePresenter implements PersonalInfoEvent {
    private static final String TAG = "PersonalCenterFragmentPresenter";
    private final Context mContext;
    private final IPersonalCenterView mView;
    private final IPersonalCenterModel mModel;


    public PersonalCenterFragmentPresenter(Context context, IPersonalCenterView view) {
        mContext = context;
        mView = view;
        mModel = new PersonalCenterModel(context, mHandler);
        TuyaSdk.getEventBus().register(this);
    }


    public void setPersonalInfo() {
        mView.setNickName(mModel.getNickName());
        mView.setUserName(mModel.getUserName());
    }

    public void gotoPersonalInfoActivity() {
        ActivityUtils.gotoActivity((Activity) mContext, PersonalInfoActivity.class, ActivityUtils.ANIMATE_FORWARD, false);
    }

    @Override
    public void onEvent(PersonalInfoEventModel event) {
        mView.setNickName(mModel.getNickName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mModel.onDestroy();
        TuyaSdk.getEventBus().unregister(this);
    }


}
