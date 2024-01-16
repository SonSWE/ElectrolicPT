package com.example.electrolicpt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.electrolicpt.DateWorkoutListActivity
import com.example.electrolicpt.R
import com.example.electrolicpt.databinding.DayItemBinding
import com.example.electrolicpt.ObjectInfor.DateInLessonInfo
import com.example.electrolicpt.ObjectInfor.LessonInfo


class DateAdapter(private val activity: DateWorkoutListActivity, private val colection: List<DateInLessonInfo>, private val  lesson: LessonInfo) :
    RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DayItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_item, parent, false)
        return DateViewHolder(view)

    }

    override fun getItemCount() = colection.size

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.binding.apply {
            val colection = colection[position]
            tvDay.text = colection.day.toString()
            if(colection.status == 0){
                //khóa
                cvDate.setBackgroundResource(R.drawable.card_disable)
            }else if(colection.status == 1){
                //đã tập
                cvDate.setBackgroundResource(R.drawable.card_done)
            }else if(colection.status == 2){
                //chưa tập
                cvDate.setBackgroundResource(R.drawable.card_active)
                cvDate.setOnClickListener {
                    activity.goExerciseList()
                }
            }
        }
    }
}