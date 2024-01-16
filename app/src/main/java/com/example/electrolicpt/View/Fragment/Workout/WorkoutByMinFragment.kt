package com.example.electrolicpt.View.Fragment.Workout

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.electrolicpt.R
import com.example.electrolicpt.WorkoutActivity
import com.google.android.material.button.MaterialButton
import java.util.Locale

class WorkoutByMinFragment() : Fragment() {
    private var isDone = false

    private var mTextViewCountDown: TextView? = null
    private var mButtonStartPause: MaterialButton? = null

    private var mCountDownTimer: CountDownTimer? = null

    private var isTimerRunning = false

    private var mTimeLeftInSeconds: Long = 0
    private var mTimeLeftInMillis: Long = 0
    private var mEndTime: Long = 0

    private lateinit var activity: WorkoutActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_workout_by_min, container, false)
        activity = getActivity() as WorkoutActivity
//        //init data
        val tvNameExercise: TextView = rootView.findViewById(R.id.tvNameExercise)

        tvNameExercise.text = activity.Curent_Exercise.exercise_Name
        mTimeLeftInSeconds = activity.Curent_Exercise.time?.toLong() ?: 0

        mTimeLeftInMillis = mTimeLeftInSeconds * 1000
        mTextViewCountDown = rootView.findViewById<TextView>(R.id.tvClockDown)
        mButtonStartPause = rootView.findViewById<MaterialButton>(R.id.btnStartStop)

        mTextViewCountDown?.text = secondsToMinutesAndSeconds(mTimeLeftInSeconds)
        mButtonStartPause?.setOnClickListener(View.OnClickListener {
            if(isDone){
                //chuyển bài tập tiếp theo
                activity.NextExercise()
            }else{
                if (isTimerRunning) {
                    pauseTimer()
                } else {
                    startTimer()
                }
            }

        })

        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            activity.finish()
        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->

        }

        val btnPre: MaterialButton = rootView.findViewById<MaterialButton>(R.id.btnPrevious)
        btnPre.setOnClickListener(View.OnClickListener {

            val builder = AlertDialog.Builder(activity)

            builder.setTitle("Cảnh báo")
            builder.setMessage("Nếu bạn thoát bây giờ thì dữ liệu tập luyện sẽ bị xóa")
            builder.setPositiveButton("Đồng ý", positiveButtonClick)
            builder.setNegativeButton("Không", negativeButtonClick)

            val alertDialog = builder.create()
            alertDialog.show()

        })

        val btnNext: MaterialButton = rootView.findViewById<MaterialButton>(R.id.btnNext)
        btnNext.setOnClickListener(View.OnClickListener {
            if (!isDone) {
                val builder = AlertDialog.Builder(activity)

                builder.setTitle("Thông báo")
                builder.setMessage("Bạn phải hoàn thành bài tập này trước khi chuyển sang bài tập tiếp theo")
                builder.setPositiveButton("Đồng ý", negativeButtonClick)

                val alertDialog = builder.create()
                alertDialog.show()
            }else{
                activity.NextExercise()
            }
        })
        // Inflate the layout for this fragment
        return rootView
    }

    fun secondsToMinutesAndSeconds(seconds: Long): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    private fun startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis * 1000
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                isTimerRunning = false
            }
        }.start()
        mButtonStartPause?.setIconResource(R.drawable.startico)
        mButtonStartPause?.text = "Tạm dừng"
        isTimerRunning = true
    }

    private fun pauseTimer() {
        mCountDownTimer!!.cancel()
        mButtonStartPause?.setIconResource(R.drawable.stopico)
        mButtonStartPause?.text = "Tiếp tục"
        isTimerRunning = false
    }

    private fun updateCountDownText() {
        val minutes = (mTimeLeftInMillis / 1000).toInt() / 60
        val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        mTextViewCountDown?.text = timeLeftFormatted

        if(seconds == 0){
            mButtonStartPause?.setIconResource(R.drawable.check)
            mButtonStartPause?.text = "Xong"
            isDone = true
        }
    }

}