package com.security.demosdktest.family.model;

import android.content.Context;

import com.security.demosdktest.family.FamilyManager;
import com.tuya.smart.android.mvp.model.BaseModel;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;

public class FamilyIndexModel extends BaseModel implements IFamilyIndexModel {

    public static final String TAG = FamilyIndexModel.class.getSimpleName();

    public FamilyIndexModel(Context ctx) {
        super(ctx);
    }


    @Override
    public void queryHomeList(ITuyaGetHomeListCallback callback) {
        FamilyManager.getInstance().getHomeList(callback);
    }

    @Override
    public void onDestroy() {

    }

}
