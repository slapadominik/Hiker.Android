package com.hiker.presentation.trips.tabViews.upcomingTrips.addTrip

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.hiker.domain.entities.Mountain

class MountainArrayAdapter(context: Context,
                           @LayoutRes private val layoutResource: Int,
                           private val mountains: List<Mountain>)
    : ArrayAdapter<Mountain>(context, layoutResource, mountains),
    Filterable{

    private var filterableMountains = mountains

    override fun getCount(): Int {
        return filterableMountains.size
    }

    override fun getItem(p0: Int): Mountain? {
        return filterableMountains.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        // Or just return p0
        return filterableMountains.get(p0).id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = "${filterableMountains[position].name}, ${filterableMountains[position].metersAboveSeaLevel} m n.p.m."
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                filterableMountains = filterResults.values as List<Mountain>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty()){
                        mountains
                }
                else
                    mountains.filter {
                        it.name.toLowerCase().contains(queryString)
                    }
                return filterResults
            }
        }
    }
}