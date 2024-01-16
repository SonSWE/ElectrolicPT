package com.example.electrolicpt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.electrolicpt.R
import com.example.electrolicpt.ObjectInfor.MealMorningInfo


class MealMenuAdapter(private val mListFoodMor: List<MealMorningInfo>?) :
    RecyclerView.Adapter<MealMenuAdapter.MealMenuholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealMenuholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_morning, parent, false)
        return MealMenuholder(view)
    }

    override fun onBindViewHolder(holder: MealMenuholder, position: Int) {
        val food = mListFoodMor!![position] ?: return
       holder.amoutfoodMor.text = food.quantity.toString()
       holder.foodMor.text = food.name_food
        holder.kcalfoodMor.text = food.totalKcal.toString()

    }

    override fun getItemCount(): Int {
        return mListFoodMor?.size ?: 0
    }

    inner class MealMenuholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        for (food in mListFoodMor.orEmpty()) {
            totalKcal += food.totalKcal
        }


        return totalKcal
    }
}