package com.example.electrolicpt.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.electrolicpt.R
import com.example.electrolicpt.DateWorkoutListActivity
import com.example.electrolicpt.databinding.LessonPlanItemBinding
import com.example.electrolicpt.ObjectInfor.LessonInfo


class LessonPlanAdapter(private val colection: List<LessonInfo>) :
    RecyclerView.Adapter<LessonPlanAdapter.LessonPlanViewHolder>() {

    inner class LessonPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = LessonPlanItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonPlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lesson_plan_item, parent, false)
        return LessonPlanViewHolder(view)

    }

    override fun getItemCount() = colection.size

    override fun onBindViewHolder(holder: LessonPlanViewHolder, position: Int) {
        holder.binding.apply {
            val colection = colection[position]
            tvLessonName.text = colection.name
            tvLessonDesc.text = colection.description
            Glide.with(this.root).load(colection.thumbnail).into(ivLessonThumbnail)

            btnStartLesson.setOnClickListener { v ->
                val intent = Intent(v.context,  DateWorkoutListActivity::class.java)
                intent.putExtra("lesson", colection)
                v.context.startActivity(intent)
            }
        }


    }
}