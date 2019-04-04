package com.iw.thieftrackerv3.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.iw.thieftrackerv3.R

class AdapterFilters(val context: Context, val interfaceFilters: InterfaceFilters): RecyclerView.Adapter<AdapterFilters.ViewHolder>() {
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindType(context)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater
            .from(context)
            .inflate(R.layout.cell_filter, p0, false)
        return ViewHolderA(view)

    }

    override fun getItemCount(): Int {
        return 12
    }


    abstract inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bindType(context: Context)
    }

    inner class ViewHolderA(itemView: View) : ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        override fun bindType(context: Context) {
            itemView!!.setOnClickListener{
                interfaceFilters.Filter("Prueba")
            }


        }
    }

    interface InterfaceFilters {
        fun Filter(filter:String)

    }

}