package com.bdwater.productwater.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bdwater.productwater.R
import com.bdwater.productwater.model.asset.Device

/**
 * Created by hegang on 2018/8/8.
 */
class DeviceListRecyclerViewAdapter: RecyclerView.Adapter<DeviceListRecyclerViewAdapter.SimpleDeviceViewHolder>() {
    var devices: MutableList<Device> = mutableListOf();

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SimpleDeviceViewHolder {
        var inflater = LayoutInflater.from(parent!!.context);
        var view = inflater.inflate(R.layout.device_simple_card_item, null)
        return SimpleDeviceViewHolder(view);
    }

    override fun onBindViewHolder(holder: SimpleDeviceViewHolder?, position: Int) {
        if(holder is SimpleDeviceViewHolder) {
            var entry = this.devices[position];
            holder.name.text = entry.name;
            holder.type.text = entry.installDate
            holder.bigRepairCount.text = "-"
            holder.remainDays.text = "-"
            holder.address.text = entry.address
        }
    }

    override fun getItemCount(): Int {
        return this.devices.size;
    }
    fun getItem(position: Int) : Device {
        return this.devices[position];
    }

    class SimpleDeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var name: TextView
        var type: TextView
        var bigRepairCount: TextView
        var remainDays: TextView
        var exclamationIcon : ImageView
        var address : TextView
        init {
            image = itemView.findViewById(R.id.imageView)
            name = itemView.findViewById(R.id.nameTextView)
            type = itemView.findViewById(R.id.typeTextView)
            bigRepairCount = itemView.findViewById(R.id.bigRepairCountTextView)
            remainDays = itemView.findViewById(R.id.remainDaysTextView)
            exclamationIcon = itemView.findViewById(R.id.exclamationImageView)
            address = itemView.findViewById(R.id.addressTextView)
        }
    }
}