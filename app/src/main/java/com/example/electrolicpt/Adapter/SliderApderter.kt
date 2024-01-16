package com.example.electrolicpt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.electrolicpt.R
import com.example.electrolicpt.databinding.SliderItemContainerBinding

class SliderApderter(private val viewPage: ViewPager2, private val colection: ArrayList<Int>) :
    RecyclerView.Adapter<SliderApderter.SliderViewHolder>() {

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = SliderItemContainerBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slider_item_container, parent, false)
        return SliderViewHolder(view)

    }

    override fun getItemCount() = colection.size

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.binding.apply {
            val colection = colection[position]
            imgSilder.setImageResource(colection)
        }


    }
}