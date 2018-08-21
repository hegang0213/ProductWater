package com.bdwater.productwater.asset.service

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bdwater.productwater.R
import com.bdwater.productwater.model.asset.Service
/**
 * Created by hegang on 2018/8/16.
 */
class ServiceRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    var services = ArrayList<Service>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.service_card_item, null)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ServiceViewHolder
        val service = getItem(position)
        viewHolder.titleTextView.text = service.title
        viewHolder.serviceDateTextView.text = service.serviceDate
        viewHolder.contentTextView.text = service.content
        viewHolder.bigRepairIndexTextView.text = Integer.toString(service.bigRepairIndex)
        viewHolder.typeNameTextView.text = service.typeName
        viewHolder.bigRepairIndexTextView.visibility = if (service.type === 0) View.INVISIBLE else View.VISIBLE
    }

    private fun getItem(position: Int): Service {
        return this.services[position]
    }

    override fun getItemCount(): Int {
        return this.services.size
    }

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView
        var serviceDateTextView: TextView
        var contentTextView: TextView
        var bigRepairIndexTextView: TextView
        var typeNameTextView: TextView

        init {

            titleTextView = itemView.findViewById(R.id.titleTextView) as TextView
            serviceDateTextView = itemView.findViewById(R.id.serviceDateTextView) as TextView
            contentTextView = itemView.findViewById(R.id.contentTextView) as TextView
            bigRepairIndexTextView = itemView.findViewById(R.id.bigRepairIndexTextView) as TextView
            typeNameTextView = itemView.findViewById(R.id.typeNameTextView) as TextView
        }
    }
}