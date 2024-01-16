package com.example.electrolicpt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.electrolicpt.R
import com.example.electrolicpt.ObjectInfor.MealLunchInfo


class MealMenuLunchAdapter(private val mListFoodLun: List<MealLunchInfo>?) :
    RecyclerView.Adapter<MealMenuLunchAdapter.MealMenuLunchholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealMenuLunchholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_morning, parent, false)
        return MealMenuLunchholder(view)
    }

    override fun onBindViewHolder(holder: MealMenuLunchholder, position: Int) {
        val food = mListFoodLun!![position] ?: return
        holder.amoutfoodMor.text = food.quantity.toString()
        holder.foodMor.text = food.name_food
        holder.kcalfoodMor.text = food.totalKcal.toString()

    }

    override fun getItemCount(): Int {
        return mListFoodLun?.size ?: 0
    }

    inner class MealMenuLunchholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amoutfoodMor: TextView
        val foodMor: TextView
        val kcalfoodMor: TextView


        init {
            amoutfoodMor = itemView.findViewById(R.id.amoutfoodMor)
            foodMor = itemView.findViewById(R.id.foodMor)
            kcalfoodMor = itemView.findViewById(R.id.kcalfoodMor)

        }



    }

    fun calculateTotalKcal(): Int {
        var totalKcal = 0

        for (food in mListFoodLun.orEmpty()) {
            totalKcal += food.totalKcal
        }


        return totalKcal
    }
}