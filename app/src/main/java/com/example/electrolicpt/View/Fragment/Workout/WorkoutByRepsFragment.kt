package com.example.electrolicpt.View.Fragment.Workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.electrolicpt.R
import com.example.electrolicpt.WorkoutActivity
import com.google.android.material.button.MaterialButton

class WorkoutByRepsFragment : Fragment() {
    private lateinit var activity: WorkoutActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_workout_by_reps, container, false)
        activity = getActivity() as WorkoutActivity

        val tvNameExercise: TextView = rootView.findViewById(R.id.tvNameExercise)
        tvNameExercise.text = activity.Curent_Exercise.exercise_Name

        val tvDesc: TextView = rootView.findViewById(R.id.tvDesc)
        tvDesc.text = activity.Curent_Exercise.description

        val onNextBtnClick: View.OnClickListener = View.OnClickListener {
            //chuyển bài tập tiếp theo
            activity.NextExercise()
        }
        val btnDone = rootView.findViewById<MaterialButton>(R.id.btnDone)
        btnDone?.setOnClickListener(onNextBtnClick);

        val btnNext = rootView.findViewById<MaterialButton>(R.id.btnNext)
        btnNext?.setOnClickListener(onNextBtnClick);

        // Inflate the layout for this fragment
        return rootView
    }


}