package com.example.electrolicpt.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.electrolicpt.ExerciseListActivity
import com.example.electrolicpt.R
import com.example.electrolicpt.databinding.LessonByMuscleItemBinding
import com.example.electrolicpt.ObjectInfor.LessonInfo


class LessonByMuscleAdapter(private val colection: List<LessonInfo>) :
    RecyclerView.Adapter<LessonByMuscleAdapter.LessonByMuscleViewHolder>() {

    inner class LessonByMuscleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = LessonByMuscleItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonByMuscleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lesson_by_muscle_item, parent, false)
        return LessonByMuscleViewHolder(view)

    }

    override fun getItemCount() = colection.size

    override fun onBindViewHolder(holder: LessonByMuscleViewHolder, position: Int) {
        holder.binding.apply {
            val colection = colection[position]
            tvLessonName.text = colection.name
            tvLessonDesc.text = colection.description
            Glide.with(this.root).load(colection.thumbnail).into(ivLessonThumbnail)

            cardLesson.setOnClickListener { v ->
                val intent = Intent(v.context, ExerciseListActivity::class.java)
                intent.putExtra("lesson", colection)
                v.context.startActivity(intent)
            }
        }


    }
}
