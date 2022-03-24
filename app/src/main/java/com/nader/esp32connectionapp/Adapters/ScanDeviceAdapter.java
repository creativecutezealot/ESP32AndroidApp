package com.nader.esp32connectionapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nader.esp32connectionapp.Models.DeviceModal;
import com.nader.esp32connectionapp.R;

import java.util.ArrayList;

public class ScanDeviceAdapter extends RecyclerView.Adapter<ScanDeviceAdapter.ViewHolder>{
    private ArrayList<DeviceModal> deviceList;
    private LayoutInflater mInflater;
    private ScanDeviceAdapter.ItemClickListener mClickListener;
    private Context mContext;

    public ScanDeviceAdapter(Context context, ArrayList<DeviceModal> deviceList){
        this.deviceList = deviceList;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public ScanDeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_rc_scan_device, parent, false);
        return new ScanDeviceAdapter.ViewHolder(view);
    }
    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ScanDeviceAdapter.ViewHolder holder, int position) {
        DeviceModal groupItem = deviceList.get(position);
        holder.deviceName.setText(groupItem.getDeviceName());
        holder.deviceAddress.setText(groupItem.getDeviceAddress());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return deviceList.size();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView deviceName;
        TextView deviceAddress;


        ViewHolder(View itemView) {
            super(itemView);
            deviceName= itemView.findViewById(R.id.dv_name);
            deviceAddress = itemView.findViewById(R.id.dv_address);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
    // convenience method for getting data at click position
    public DeviceModal getItem(int id) {
        return deviceList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ScanDeviceAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position );

    }
}