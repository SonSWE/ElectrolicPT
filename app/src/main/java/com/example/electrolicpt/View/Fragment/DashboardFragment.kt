package com.example.electrolicpt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.electrolicpt.MainActivity
import com.example.electrolicpt.ObjectInfor.LessonInfo
import com.example.electrolicpt.ObjectInfor.UserLessonInfo
import com.example.electrolicpt.R
import com.example.electrolicpt.adapter.LessonByMuscleAdapter
import com.example.electrolicpt.adapter.LessonPlanAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class DashboardFragment : Fragment() {
    private lateinit var rvLessonByPlan: RecyclerView
    private lateinit var rvLessonByMuscleEz: RecyclerView
    private lateinit var rvLessonByMuscleDiff: RecyclerView

    private lateinit var mLessonPlanAdapter: LessonPlanAdapter
    private lateinit var mLessonByMuscleAdapterEz: LessonByMuscleAdapter
    private lateinit var mLessonByMuscleAdapterDiff: LessonByMuscleAdapter

    private lateinit var _lstLessonByPlan: MutableList<LessonInfo>
    private lateinit var _lstLessonByMucleEz: MutableList<LessonInfo>
    private lateinit var _lstLessonByMucleDiff: MutableList<LessonInfo>

    private lateinit var rootView: View
    private lateinit var activity: MainActivity
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false)
        activity = getActivity() as MainActivity
        database = FirebaseDatabase.getInstance().reference


        LoadData()

        val ivAvt = rootView.findViewById<ImageView>(R.id.ivAvt)
        val tvName = rootView.findViewById<TextView>(R.id.tvName)
        val tvTargetText = rootView.findViewById<TextView>(R.id.tvTargetText)
        val tvHeight = rootView.findViewById<TextView>(R.id.tvHeight)
        val tvWeight = rootView.findViewById<TextView>(R.id.tvWeight)
        val tvTarget = rootView.findViewById<TextView>(R.id.tvTarget)

        Glide.with(activity).load("https://e.com.vn/uploads/tin-tuc/2021_08/phuong-phap-quy-trinh-cach-tap-gym-dung-cach-va-hieu-qua-cho-nam-1.jpg").into(ivAvt)

        tvName.text = activity.userLogin?.full_Name
        if(activity.userLogin?.target == 0){
            tvTargetText.text = "Xây dựng cơ bắp"
        }else if(activity.userLogin?.target == 1){
            tvTargetText.text = "Lấy lại vóc dáng"
        }else if(activity.userLogin?.target == 2){
            tvTargetText.text = "Duy trì sức khỏe"
        }

        tvHeight.text = activity.userLogin?.heigth.toString()
        tvWeight.text = activity.userLogin?.weigth.toString()
        tvTarget.text = activity.userLogin?.weigth.toString()

        // Inflate the layout for this fragment
        return rootView
    }

    private fun LoadData() {
        rvLessonByPlan = rootView.findViewById(R.id.rvLessonByPlan)
        rvLessonByMuscleEz = rootView.findViewById(R.id.rvLessonMuscle)
        rvLessonByMuscleDiff = rootView.findViewById(R.id.rvLessonMuscleDiff)


        _lstLessonByPlan = ArrayList<LessonInfo>()
        _lstLessonByMucleEz = ArrayList<LessonInfo>()
        _lstLessonByMucleDiff = ArrayList<LessonInfo>()

        mLessonPlanAdapter = LessonPlanAdapter(_lstLessonByPlan)
        rvLessonByPlan.setAdapter(mLessonPlanAdapter)

        mLessonByMuscleAdapterEz = LessonByMuscleAdapter(_lstLessonByMucleEz)
        rvLessonByMuscleEz.setAdapter(mLessonByMuscleAdapterEz)

        mLessonByMuscleAdapterDiff = LessonByMuscleAdapter(_lstLessonByMucleDiff)
        rvLessonByMuscleDiff.setAdapter(mLessonByMuscleAdapterDiff)

        val myRef = database.child("Workout")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //thông tin bài tập của user đăng nhập
                val lstUserLesson: ArrayList<UserLessonInfo> = ArrayList<UserLessonInfo>()
                for (dataSnapshot in snapshot.child("User_Lesson").children) {
                    val model = dataSnapshot.getValue<UserLessonInfo>()
                    if (model != null) {
                        lstUserLesson.add(model)
                    }
                }

                _lstLessonByPlan!!.clear()
                _lstLessonByMucleEz!!.clear()
                _lstLessonByMucleDiff!!.clear()

                for (dataSnapshot in snapshot.child("Lesson").children) {
                    val model = dataSnapshot.getValue<LessonInfo>()
                    if (model != null) {
                        if (model.lesson_Type == 1) {
                            var i =
                                lstUserLesson.indexOfFirst { x -> x.user_Id == activity.userLogin.user_Id && x.lesson_Id == model.lesson_Id }
                                    ?: -1
                            if (i >= 0) {
                                model.current_Date = lstUserLesson[i].current_Date
                                _lstLessonByPlan!!.add(model)
                            }
                        } else if (model.lesson_Type == 2) {
                            if (model.level == 1) {
                                _lstLessonByMucleEz!!.add(model)
                            } else if (model.level == 3) {
                                _lstLessonByMucleDiff!!.add(model)
                            }
                        }
                    }
                }

                // Thông báo cho Adapter biết là dữ liệu đã thay đổi
                mLessonPlanAdapter!!.notifyDataSetChanged()
                mLessonByMuscleAdapterEz!!.notifyDataSetChanged()
                mLessonByMuscleAdapterDiff!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        })


    }
}