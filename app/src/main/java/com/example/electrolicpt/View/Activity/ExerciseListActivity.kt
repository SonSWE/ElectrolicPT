package com.example.electrolicpt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.electrolicpt.adapter.ExerciseAdapter
import com.example.electrolicpt.ObjectInfor.ExerciseLessonInfo
import com.example.electrolicpt.ObjectInfor.ExerciseInfo
import com.example.electrolicpt.ObjectInfor.LessonInfo
import com.example.electrolicpt.ObjectInfor.ResultInfo
import com.example.electrolicpt.ObjectInfor.UserInfo
import com.example.electrolicpt.Utils.UserShareReferentHelper
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.io.Serializable

class ExerciseListActivity : AppCompatActivity() {
    lateinit var rvExercise: RecyclerView
    lateinit var exerciseAdapter: ExerciseAdapter
    var _lstExercise: MutableList<ExerciseInfo> = ArrayList<ExerciseInfo>()

    var _result: ResultInfo = ResultInfo()
    var _current_exercise_index: Int = 0

    lateinit var database: DatabaseReference

    lateinit var lesson: LessonInfo

    lateinit var userLogin: UserInfo

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    lateinit var youTubePlayerView: YouTubePlayerView
    lateinit var fullscreenViewContainer: FrameLayout

    private lateinit var youTubePlayer: YouTubePlayer
    private var isFullscreen = false
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullscreen) {
                // if the player is in fullscreen, exit fullscreen
                youTubePlayer.toggleFullscreen()
            } else if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                CloseTutorial()
            } else {
                finish()
            }
        }
    }
    var curent_tutorial_index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)
        try {
            database = FirebaseDatabase.getInstance().reference
            userLogin = UserShareReferentHelper().getUser(this)

            lesson = intent.getSerializableExtra("lesson") as LessonInfo

            val ivThumbnail: ImageView = findViewById(R.id.ivThumbnail)
            val tvName: TextView = findViewById(R.id.tvLessonName)
            val tvDesc: TextView = findViewById(R.id.tvDesc)
            tvName.text = lesson.name
            tvDesc.text = lesson.description
            if (lesson.thumbnail != null && lesson.thumbnail != "") {
                Glide.with(this).load(lesson.thumbnail).into(ivThumbnail)
            }

            rvExercise = findViewById(R.id.rvExercise)
            LoadData()

            val bottomSheet = findViewById<LinearLayout>(R.id.bottom_sheet)
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            val btnClose = findViewById<MaterialButton>(R.id.btnClose)
            btnClose.setOnClickListener(View.OnClickListener {
                CloseTutorial()
            })


            val btnPreviousTut = findViewById<MaterialButton>(R.id.btnPreviousTut)
            btnPreviousTut.setOnClickListener(View.OnClickListener {
                if (curent_tutorial_index > 0) {
                    curent_tutorial_index -= 1
                    OpenTutorial(_lstExercise[curent_tutorial_index])
                }
            })

            val btnNextTut = findViewById<MaterialButton>(R.id.btnNextTut)
            btnNextTut.setOnClickListener(View.OnClickListener {
                if (curent_tutorial_index >= 0 && curent_tutorial_index < _lstExercise.count() - 1) {
                    curent_tutorial_index += 1
                    OpenTutorial(_lstExercise[curent_tutorial_index])
                }
            })

            onBackPressedDispatcher.addCallback(onBackPressedCallback)


            //<editor-fold desc="youtubeView">
            youTubePlayerView = findViewById<YouTubePlayerView>(R.id.video_tutorial)
            fullscreenViewContainer = findViewById<FrameLayout>(R.id.full_screen_view_container)

            val iFramePlayerOptions = IFramePlayerOptions.Builder()
                .controls(1)
                .fullscreen(1) // enable full screen button
                .build()

            // we need to initialize manually in order to pass IFramePlayerOptions to the player
            youTubePlayerView.enableAutomaticInitialization = false

            youTubePlayerView.addFullscreenListener(object : FullscreenListener {
                override fun onEnterFullscreen(
                    fullscreenView: View,
                    exitFullscreen: () -> Unit
                ) {
                    isFullscreen = true

                    // the video will continue playing in fullscreenView
                    youTubePlayerView.visibility = View.GONE
                    fullscreenViewContainer.visibility = View.VISIBLE
                    fullscreenViewContainer.addView(fullscreenView)

                    // optionally request landscape orientation
                    //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }

                override fun onExitFullscreen() {
                    isFullscreen = false

                    // the video will continue playing in the player
                    youTubePlayerView.visibility = View.VISIBLE
                    fullscreenViewContainer.visibility = View.GONE
                    fullscreenViewContainer.removeAllViews()

                    //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            })

            youTubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    this@ExerciseListActivity.youTubePlayer = youTubePlayer
                }
            }, iFramePlayerOptions)
            lifecycle.addObserver(youTubePlayerView)
            //</editor-fold>

        } catch (e: Exception) {
            println(e.message)
        }
    }


    fun LoadVideo(idVideo: String) {
        try {
            if (youTubePlayer != null) {
                youTubePlayer.cueVideo(idVideo, 0f)
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun OpenTutorial(exercise: ExerciseInfo) {
        val tvName = findViewById<TextView>(R.id.tvName)
        tvName.text = exercise.exercise_Name
        val tvTurorial = findViewById<TextView>(R.id.tvTurorial)
        tvTurorial.text = exercise.tutorial


        exercise.video_Id?.let { LoadVideo(it) }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        curent_tutorial_index =
            _lstExercise.indexOfFirst { x -> x.exercise_Id == exercise?.exercise_Id }
                ?: -1

        val tvNumTut = findViewById<TextView>(R.id.tvNumTut)
        tvNumTut.text =
            (curent_tutorial_index + 1).toString() + "/" + _lstExercise.count()
                .toString()
    }

    fun CloseTutorial() {
        curent_tutorial_index = 0
        youTubePlayer.pause()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun LoadData() {
        try {
            exerciseAdapter = ExerciseAdapter(this, _lstExercise)
            rvExercise.adapter = exerciseAdapter

            val myRef = database.child("Workout")
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.child("Result").children) {
                        val model = dataSnapshot.getValue<ResultInfo>()
                        if (model != null) {
                            if (lesson.lesson_Type == 1) {
                                //lấy kết quả tập hiện tạo theo ngày tập từ giáo trình
                                if (model.lesson_Id == lesson.lesson_Id && model.current_Date == lesson.current_Date) {
                                    _result = model
                                }
                            } else {
                                //lấy kết quả tập từ bài tập theo nhóm cơ
                                if (model.lesson_Id == lesson.lesson_Id) {
                                    _result = model
                                }
                            }
                        }
                    }

                    //danh sách id bài tập theo giáo án
                    val lstExerciseLesson: ArrayList<ExerciseLessonInfo> =
                        ArrayList<ExerciseLessonInfo>()
                    for (dataSnapshot in snapshot.child("Exercise_Lesson").children) {
                        val model = dataSnapshot.getValue<ExerciseLessonInfo>()
                        if (model != null)
                            if (lesson.lesson_Type == 1) {
                                if (model.lesson_Id == lesson.lesson_Id && model.date_Id == lesson.current_Date) {
                                    lstExerciseLesson.add(model)
                                }
                            } else {
                                if (model.lesson_Id == lesson.lesson_Id) {
                                    lstExerciseLesson.add(model)
                                }
                            }

                    }

                    //danh sách bài tập
                    _lstExercise!!.clear()

                    for (dataSnapshot in snapshot.child("Exercise").children) {
                        val model = dataSnapshot.getValue<ExerciseInfo>()
                        if (model != null) {
                            if (lstExerciseLesson.any { x -> x.exercise_Id == model.exercise_Id }) {
                                _lstExercise!!.add(model)
                            }
                        }
                    }

                    if (_result != null && _result.result_Id!! > 0) {
                        _current_exercise_index =
                            _lstExercise.indexOfFirst { x -> x.exercise_Id == _result?.current_Exercise_Id }
                                ?: -1
                    }
                    // Thông báo cho Adapter biết là dữ liệu đã thay đổi
                    exerciseAdapter!!.notifyDataSetChanged()
                    val tvNumTut = findViewById<TextView>(R.id.tvNumTut)
                    tvNumTut.text =
                        (curent_tutorial_index + 1).toString() + "/" + _lstExercise.count()
                            .toString()
                    LoadUi()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý khi có lỗi xảy ra
                }
            })

        } catch (e: Exception) {
            println("error:" + e.message)
        }
    }

    private fun LoadUi() {
        val tvNote: TextView = findViewById(R.id.tvNote)
        val btnRestart: MaterialButton = findViewById(R.id.btnRestart)
        val btnStart: MaterialButton = findViewById(R.id.btnStart)
        val btnContinue: MaterialButton = findViewById(R.id.btnContinue)
        val layoutBtnContinue: FlexboxLayout = findViewById(R.id.layoutBtnContinue)
        //load button by status
        if (_result != null && _result.code != null && _result.code != "") {
            if (_result.status == 1) {
                //đang tập dở
                layoutBtnContinue.visibility = View.VISIBLE
                btnRestart.visibility = View.VISIBLE
                tvNote.text = "Đã tập " + _result.count_Done.toString() + "/" + _lstExercise.count()
                    .toString() + " bài"
                btnStart.visibility = View.GONE

                btnContinue.setOnClickListener {
                    StatrWorkout(_lstExercise, _current_exercise_index);
                }

                btnRestart.setOnClickListener {
                    val db =
                        FirebaseDatabase.getInstance().reference.child("Workout").child("Result")
                    db.child(_result.code!!).removeValue().addOnSuccessListener {
//                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
                    }

                    StatrWorkout(_lstExercise, 0);
                }
            }
        } else {
            //chưa tập gì
            btnRestart.visibility = View.GONE
            btnRestart.visibility = View.GONE
            btnStart.visibility = View.VISIBLE
            btnStart.setOnClickListener {
                StatrWorkout(_lstExercise, 0);
            }

        }
    }

    fun StatrWorkout(mListExercise: MutableList<ExerciseInfo>, curent_exercise_index: Int) {
        val intent = Intent(this, WorkoutActivity::class.java)

        intent.putExtra("exercise_list", mListExercise as Serializable)
        intent.putExtra("curent_exercise_index", curent_exercise_index)
        intent.putExtra("Lesson_Id", lesson.lesson_Id)
        intent.putExtra("Lesson_Type", lesson.lesson_Type)
        intent.putExtra("result", _result)

        startActivity(intent)
    }
}