package com.example.electrolicpt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.electrolicpt.adapter.DateAdapter
import com.example.electrolicpt.ObjectInfor.DateInLessonInfo
import com.example.electrolicpt.ObjectInfor.LessonInfo
import com.example.electrolicpt.ObjectInfor.ResultInfo
import com.example.electrolicpt.ObjectInfor.UserInfo
import com.example.electrolicpt.Utils.UserShareReferentHelper
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class DateWorkoutListActivity : AppCompatActivity() {
    lateinit var rvDate: RecyclerView
    lateinit var dateAdapter: DateAdapter
    lateinit var _lstDate: MutableList<DateInLessonInfo>
    lateinit var _result: ResultInfo
    lateinit var database: DatabaseReference

    lateinit var lesson: LessonInfo
    var CurrentDatelesson = LessonInfo()

    lateinit var userLogin: UserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_workout_list)
        userLogin = UserShareReferentHelper().getUser(this)

        database = FirebaseDatabase.getInstance().reference
        lesson = intent.getSerializableExtra("lesson") as LessonInfo

        val tvName: TextView = findViewById(R.id.tvLessonName)
        tvName.text = lesson.name

        val tvDesc: TextView = findViewById(R.id.tvDesc)
        tvDesc.text = lesson.description

        val ivThumbnail: ImageView = findViewById(R.id.ivThumbnail)
        if (lesson.thumbnail != null && lesson.thumbnail != "") {
            Glide.with(this).load(lesson.thumbnail).into(ivThumbnail)
        }

        val btnStart: MaterialButton = findViewById(R.id.btnStart)
        btnStart.setOnClickListener(View.OnClickListener {
            goExerciseList()
        })

        LoadData()
    }

    private fun LoadData() {
        rvDate = findViewById(R.id.rvDate)
        _lstDate = ArrayList<DateInLessonInfo>()

        dateAdapter = DateAdapter(this,_lstDate, lesson)
        rvDate.adapter = dateAdapter

        _result = ResultInfo()

        val myRef = database.child("Workout")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //kết quả bài tập
                for (dataSnapshot in snapshot.child("Result").children) {
                    val model = dataSnapshot.getValue<ResultInfo>()
                    if (model != null && model.lesson_Id == lesson.lesson_Id && model.user_Id == userLogin.user_Id) {
                        _result = model
                    }
                }

                //nếu không có kết quả thì mặc định ngày hiện tại là 1
                if(_result.code != null){

                }

                //danh sách ngày tập theo giáo án
                _lstDate!!.clear()

                for (dataSnapshot in snapshot.child("Date_In_Lesson").children) {
                    val model = dataSnapshot.getValue<DateInLessonInfo>()
                    if (model != null && model.lesson_Id == lesson.lesson_Id) {
                        _lstDate!!.add(model)
                    }
                }


                _lstDate.sortedBy { it.day }
                var day = 0
                //ngày đã tập
                for(day in 0..lesson.current_Date!!){
                    var i =
                        _lstDate.indexOfFirst { x -> x.day == day }
                            ?: -1
                    if(i >= 0){
                        _lstDate[i].status = 1
                    }

                }
                //ngày hiện tại
                var i =
                    _lstDate.indexOfFirst { x -> x.day == lesson.current_Date }
                        ?: -1
                if(i >= 0){
                    _lstDate[i].status = 2
                }

                CurrentDatelesson.lesson_Id = lesson.lesson_Id
                CurrentDatelesson.lesson_Type = lesson.lesson_Type
                CurrentDatelesson.current_Date = lesson.current_Date
                CurrentDatelesson.name = _lstDate[i].name
                CurrentDatelesson.thumbnail = _lstDate[i].thumbnail
                CurrentDatelesson.description = _lstDate[i].description

                // Thông báo cho Adapter biết là dữ liệu đã thay đổi
                dateAdapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        })


    }

    fun goExerciseList(){
        val intent = Intent(this, ExerciseListActivity::class.java)
        intent.putExtra("lesson", CurrentDatelesson)
        startActivity(intent)
    }
}