
package com.example.electrolicpt.adapter


import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.electrolicpt.R
import com.example.electrolicpt.ObjectInfor.MealMorningInfo



class MeaStorageAdapter(private val mListFoodMeal: List<MealMorningInfo>?,
                        private val onDeleteClickListener: OnDeleteClickListener) :
    RecyclerView.Adapter<MeaStorageAdapter.MeaStorageHolder>() {

    interface OnDeleteClickListener {
        fun onDeleteClick(id: String)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaStorageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.storage_time_food, parent, false)
        return MeaStorageHolder(view)
    }

    override fun onBindViewHolder(holder: MeaStorageHolder, position: Int) {
        val food = mListFoodMeal!![position] ?: return
        holder.bind(food)

    }


    override fun getItemCount(): Int {
        return mListFoodMeal?.size ?: 0
    }

    inner class MeaStorageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNameFoodStorage: TextView = itemView.findViewById(R.id.nameFood_storage)
        private val tvNumKcalStorage: TextView = itemView.findViewById(R.id.numKcal_storage)
        private val imgFoodStorage: ImageView = itemView.findViewById(R.id.imgfood_storage)
        private val amountStorage: TextView = itemView.findViewById(R.id.amount_storage)
        private val deleteStorage: ImageView = itemView.findViewById(R.id.deleteStorage)
        private val idFood: TextView = itemView.findViewById(R.id.idFood)

        fun bind(food: MealMorningInfo) {
            idFood.text = food.id.toString()
            tvNameFoodStorage.text = food.name_food
            tvNumKcalStorage.text = food.totalKcal.toString()
            amountStorage.text = Editable.Factory.getInstance().newEditable(food.quantity.toString())

            if (!food.img_food.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(food.img_food)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgFoodStorage)
            }

            // Bắt sự kiện nhấn nút delete trong ViewHolder
            deleteStorage.setOnClickListener {
                // Gọi phương thức xóa với ID
                onDeleteClickListener.onDeleteClick(idFood.text.toString())
            }
        }
    }





}