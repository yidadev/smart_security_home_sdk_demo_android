package com.security.demosdktest.family.item;

import com.security.demosdktest.R;
import com.security.demosdktest.family.recyclerview.item.BaseHead;
import com.security.demosdktest.family.recyclerview.item.BaseViewHolder;

public class FamilyInfoMemberHead extends BaseHead<String> {

    public static final int MEMBER_VIEW_TYPE=110;

    public FamilyInfoMemberHead() {
        super("");
    }

    @Override
    public int getViewType() {
        return MEMBER_VIEW_TYPE;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycler_family_member_head;
    }

    @Override
    public void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionHeadPosition) {

    }

    @Override
    public void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionHeadPosition) {

    }
}
