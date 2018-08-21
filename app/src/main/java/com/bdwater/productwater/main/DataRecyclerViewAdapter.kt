package com.bdwater.productwater.main

import android.graphics.Color
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.*
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.bdwater.productwater.CustomApplication
import com.bdwater.productwater.MainActivity
import com.bdwater.productwater.R
import com.bdwater.productwater.model.data.DataTag
import com.bdwater.productwater.model.data.Site
import org.jetbrains.annotations.NotNull

/**
 * Created by hegang on 2018/8/17.
 */
class DataRecyclerViewAdapter: Adapter<DataRecyclerViewAdapter.DataItemView>() {
    companion object {
        const val LAYOUT = R.layout.data_item
        const val SUB_LAYOUT = R.layout.data_sub_item
        const val ROW = 3
        const val COLUMN = 3
    }
    var sites = ArrayList<Site>()
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataRecyclerViewAdapter.DataItemView {
        var inflater = LayoutInflater.from(parent!!.context)
        var view = inflater.inflate(LAYOUT, parent, false)
        return DataItemView(view);
    }

    override fun getItemCount(): Int {
        return sites.size;
    }

    override fun onBindViewHolder(holder: DataRecyclerViewAdapter.DataItemView?, position: Int) {
        if(holder == null) return;
        if(this.sites[position].dataTags.isEmpty()) return;
        var entry = this.sites[position];
        var rowCount = entry.dataTags.size / ROW
        if(entry.dataTags.size % COLUMN > 0)
            rowCount++
        holder.title.text = entry.name
        holder.gridLayout.rowCount = rowCount
        var holderCount = rowCount * COLUMN
        var inflater = LayoutInflater.from(holder.itemView.context)
        holder.gridLayout.measure(0, 0)
        val screenWidth = holder.itemView.resources.displayMetrics.widthPixels
        val width = screenWidth / 3 - 1
        for (index in 0..(holderCount - 1)) {
            var layoutParams = GridLayout.LayoutParams()
            var col = index % COLUMN
            var row = index / COLUMN

            layoutParams.rowSpec = GridLayout.spec(row, 1.0f)
            layoutParams.columnSpec = GridLayout.spec(col, 1.0f)
            layoutParams.width = width
            if((col + 1) == COLUMN)
                layoutParams.width = screenWidth - (width + 1) * (COLUMN - 1)
            var view = inflater.inflate(SUB_LAYOUT, null)
            var name = ""
            var value = ""
            if(index < entry.dataTags.size) {
                name = entry.dataTags[index].name
                if (name.startsWith(entry.name))
                    name = name.substring(entry.name.length)
                value = entry.dataTags[index].value
            }
            else
                view.setBackgroundColor(view.resources.getColor(R.color.colorBody))
            view.findViewById<TextView>(R.id.valueTextView).text = value
            view.findViewById<TextView>(R.id.titleTextView).text = name
            //layoutParams.setGravity(Gravity.FILL_HORIZONTAL)
            layoutParams.setGravity(Gravity.FILL_VERTICAL)
            layoutParams.setMargins(if(col > 0) 1 else 0, 0, 0, 1)
            holder.gridLayout.addView(view, layoutParams)

//            Log.e("Adapter", "%s: row=%s, col=%s".format(index, row, col))
        }
    }

    class DataItemView(itemView: View) : ViewHolder(itemView) {
        private var titleLayout = itemView.findViewById<LinearLayout>(R.id.titleLayout)
        var title = itemView.findViewById<TextView>(R.id.titleTextView)
        var gridLayout = itemView.findViewById<GridLayout>(R.id.gridLayout)
        init {
            this.gridLayout.columnCount = COLUMN
            this.titleLayout.setOnClickListener {
                var expanded = it.tag == null
                this.gridLayout.visibility = if(expanded) View.GONE else View.VISIBLE
                it.tag = if(expanded) true else null
            }
        }
    }
}