package com.example.project.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.DetailedScheme
import com.example.project.R
import com.example.project.models.SchemesTable

class SchemesAdapter(val list:ArrayList<SchemesTable>,val context:Context) :
      RecyclerView.Adapter<SchemesAdapter.viewHolder>(){

    open class viewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        var sname:TextView=itemView.findViewById(R.id.schemeName)
        var sdept:TextView=itemView.findViewById(R.id.schemeDept)
        var stype:TextView=itemView.findViewById(R.id.schemeType)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.signle_scheme,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val scheme:SchemesTable=list.get(position)

        holder.sname.text= scheme.scheme_name
        holder.sdept.text=scheme.Department
        holder.stype.append(scheme.scheme_type)

        holder.itemView.setOnClickListener {
            val i=Intent(context,DetailedScheme::class.java)
            i.putExtra("sname",scheme.scheme_name)
            i.putExtra("sdept",scheme.Department)
            i.putExtra("stype",scheme.scheme_type)
            i.putExtra("gender",scheme.gender)
            i.putExtra("income",scheme.income)
            i.putExtra("disability",scheme.disabilities)
            i.putExtra("caste",scheme.caste)
            i.putExtra("sinfo",scheme.scheme_info)
            i.putExtra("slage",scheme.lower_age)
            i.putExtra("suage",scheme.upper_age)

            context.startActivity(i)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}