package com.security.demosdktest.family.item;

import android.text.TextUtils;
import android.widget.EditText;

import com.security.demosdktest.R;
import com.security.demosdktest.family.recyclerview.item.BaseItem;
import com.security.demosdktest.family.recyclerview.item.BaseViewHolder;

import butterknife.BindView;

public class FamilyAddEditNameItem extends BaseItem<String> {
    @BindView(R.id.recycler_family_add_edit)
    EditText editText;

    private static final int EDIT_VIEW_TYPE = 10;

    public FamilyAddEditNameItem() {
        super("");
    }

    @Override
    public int getViewType() {
        return EDIT_VIEW_TYPE;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recylcer_family_add_edit_name;
    }

    @Override
    public void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {

    }

    @Override
    public void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {
    }

    public String getEditText(){
        if (null==editText){
            return "";
        }
        String text = editText.getText().toString();
        if (TextUtils.isEmpty(text)){
            return "";
        }
        return text.trim();
    }
}
