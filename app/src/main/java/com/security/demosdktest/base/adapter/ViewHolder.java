package com.security.demosdktest.base.adapter;

import android.view.View;

/**
 * Created by mikeshou on 15/5/11.
 */
public abstract class ViewHolder<T> {

    protected View contentView;

    public ViewHolder(View contentView) {
        this.contentView = contentView;
    }

    public abstract void updateData(T data);

    public View getContentView() {
        return contentView;
    }
}
