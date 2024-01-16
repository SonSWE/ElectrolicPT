package com.example.electrolicpt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.electrolicpt.ExerciseListActivity
import com.example.electrolicpt.R
import com.example.electrolicpt.databinding.ExerciseItemBinding
import com.example.electrolicpt.ObjectInfor.ExerciseInfo

class ExerciseAdapter(private val active: ExerciseListActivity,private val colection: List<ExerciseInfo>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ExerciseItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_item, parent, false)
        return ExerciseViewHolder(view)

    }

    override fun getItemCount() = colection.size

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.binding.apply {
            val colection = colection[position]
            tvLessonName.text = colection.exercise_Name
            tvLessonDetail.text = colection.description

            Glide.with(this.root).load(colection.thumbnail).into(ivThumbnail)

            cardExercise.setOnClickListener { v ->
                active.OpenTutorial(colection)
            }
        }


    }
}
