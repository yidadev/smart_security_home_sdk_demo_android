package com.security.demosdktest.base.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.security.demosdktest.R;
import com.tuya.smart.optimus.security.base.api.bean.mode.ModeSettingDeviceBean;

import java.util.ArrayList;
import java.util.List;

public class SecurityDeviceAdapter extends RecyclerView.Adapter<SecurityDeviceAdapter.SecurityDeviceViewModel> {

    private List<ModeSettingDeviceBean> mDeviceBeanList;

    public SecurityDeviceAdapter() {

    }

    public void setDeviceBeanList(ArrayList<ModeSettingDeviceBean> deviceBeanList) {
        mDeviceBeanList = deviceBeanList;
    }

    public List<ModeSettingDeviceBean> getDeviceBeanList() {
        return mDeviceBeanList;
    }

    @NonNull
    @Override
    public SecurityDeviceViewModel onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_security_device, viewGroup, false);
        return new SecurityDeviceViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SecurityDeviceViewModel securityDeviceViewModel, int i) {
        final ModeSettingDeviceBean modeSettingDeviceBean = mDeviceBeanList.get(i);
        securityDeviceViewModel.mTvName.setText(modeSettingDeviceBean.getName());
        securityDeviceViewModel.mIvSelect.setSelected(modeSettingDeviceBean.isSelected());
        securityDeviceViewModel.mIvSelect.setVisibility(modeSettingDeviceBean.isFixed() ? View.GONE : View.VISIBLE);
        final int position = i;
        securityDeviceViewModel.mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeviceBeanList.get(position).setSelected(!securityDeviceViewModel.mIvSelect.isSelected());
                securityDeviceViewModel.mIvSelect.setSelected(!securityDeviceViewModel.mIvSelect.isSelected());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDeviceBeanList == null ? 0 : mDeviceBeanList.size();
    }

    class SecurityDeviceViewModel extends RecyclerView.ViewHolder {

        private ImageView mIvSelect;
        private TextView mTvName;

        public SecurityDeviceViewModel(@NonNull View itemView) {
            super(itemView);
            mIvSelect = itemView.findViewById(R.id.iv_device_select);
            mTvName = itemView.findViewById(R.id.tv_device_name);
        }
    }
}
