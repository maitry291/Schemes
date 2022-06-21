package com.example.project.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.project.R

class Adapter(val list: ArrayList<String>, val context: Context) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_layout, parent, false)

        view.findViewById<TextView>(R.id.spinner_tv).text = list[position]

        return view
    }
}