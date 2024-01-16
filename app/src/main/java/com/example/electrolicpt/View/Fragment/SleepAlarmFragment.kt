package com.example.electrolicpt

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.electrolicpt.AddAlarmSleepActivity
import com.example.electrolicpt.ObjectInfor.AlarmDataInfo
import com.example.electrolicpt.R
import com.example.electrolicpt.adapter.AlarmAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SleepAlarmFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var alarmAdapter: AlarmAdapter
    private val alarmList = mutableListOf<AlarmDataInfo>()
    private lateinit var timeTextView: TextView
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_sleep_alarm, container, false)
        initUiTime()
        initUi()
        initFirebaseDataListener()
        return rootView
    }

    private fun initUiTime(){
        timeTextView = rootView.findViewById(R.id.timeCurr)

        // Khởi tạo Handler
        handler = Handler(Looper.getMainLooper())

        // Khởi tạo Runnable để cập nhật thời gian
        runnable = object : Runnable {
            override fun run() {
                updateCurrentTime()
                handler.postDelayed(this, 1000) // Cập nhật mỗi giây
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Bắt đầu cập nhật khi fragment được resume
        handler.post(runnable)
    }

    override fun onPause() {
        super.onPause()
        // Dừng cập nhật khi fragment bị pause
        handler.removeCallbacks(runnable)
    }

    private fun updateCurrentTime() {
        // Lấy thời gian hiện tại
        val currentTime = getCurrentTime()

        // Sửa đổi TextView ngay trên UI thread
        activity?.runOnUiThread {
            timeTextView.text = currentTime
        }
    }

    private fun getCurrentTime(): String {
        // Tạo một đối tượng SimpleDateFormat để định dạng thời gian
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())

        // Lấy thời gian hiện tại
        return sdf.format(Date())
    }
   private fun initUi(){

       // Khởi tạo RecyclerView và Adapter
       recyclerView = rootView.findViewById(R.id.recycalviewAlarmSleep)
       recyclerView.layoutManager = LinearLayoutManager(requireContext())
       alarmAdapter = AlarmAdapter(alarmList)
       recyclerView.adapter = alarmAdapter

       val nextAdd = rootView.findViewById<ImageView>(R.id.addSleepAlarm)
       nextAdd.setOnClickListener { v: View? ->
           val intent = Intent(requireActivity(), AddAlarmSleepActivity::class.java)
           startActivity(intent)
       }
   }



    private fun initFirebaseDataListener() {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("alarms")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                alarmList.clear()

                for (snapshot in dataSnapshot.children) {
                    val alarmData = snapshot.getValue(AlarmDataInfo::class.java)
                    alarmData?.let {
                        alarmList.add(it)
                    }
                }

                // Cập nhật dữ liệu trong RecyclerView khi có thay đổi từ Firebase
                alarmAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
            }
        })
    }


}