package com.security.demosdktest.family.item;

import com.security.demosdktest.R;
import com.security.demosdktest.family.recyclerview.item.BaseHead;
import com.security.demosdktest.family.recyclerview.item.BaseViewHolder;

public class FamilyInfoSpaceHead extends BaseHead<String> {

    public FamilyInfoSpaceHead() {
        super("");
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recyler_family_info_space_head;
    }

    @Override
    public void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionHeadPosition) {

    }

    @Override
    public void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionHeadPosition) {

    }
}
