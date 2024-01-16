package com.example.electrolicpt

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.electrolicpt.DetailMealActivity
import com.example.electrolicpt.DetailMealLunchActivity
import com.example.electrolicpt.DetailMealNightActivity
import com.example.electrolicpt.ObjectInfor.MealLunchInfo
import com.example.electrolicpt.ObjectInfor.MealMorningInfo
import com.example.electrolicpt.ObjectInfor.MealNightInfo
import com.example.electrolicpt.R
import com.example.electrolicpt.Utils.AlarmReceiver
import com.example.electrolicpt.WaterHistoryActivity
import com.example.electrolicpt.adapter.MealMenuAdapter
import com.example.electrolicpt.adapter.MealMenuLunchAdapter
import com.example.electrolicpt.adapter.MealMenuNightAdapter
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar
import java.util.Locale

class MealFragment : Fragment() {

    private lateinit var rootView: View
    private var mMealMenuAdapter: MealMenuAdapter? = null
    private var mListFoodMor: MutableList<MealMorningInfo>? = mutableListOf()

    private var mMealMenuLunchAdapter: MealMenuLunchAdapter? = null
    private var mListFoodLun: MutableList<MealLunchInfo>? = mutableListOf()

    private var mMealMenuNightAdapter: MealMenuNightAdapter? = null
    private var mListFoodNNight: MutableList<MealNightInfo>? = mutableListOf()

//    private val notificationReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            if ("NOTIFICATION_SHOWN" == intent?.action) {
//                // Xử lý khi thông báo được hiển thị
//                updateTextColor()
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_meal, container, false)
        try {
            initUinext()
            initUi()
            initUiLunch()
            initUiNight()
            // Đăng ký notificationReceiver
            val intentFilter = IntentFilter("NOTIFICATION_SHOWN")
//        requireActivity().registerReceiver(notificationReceiver, intentFilter)

        } catch (e: Exception) {
            println("error: " + e.message)
        }
        return rootView
    }

    private fun initUinext() {
        // Initialize TextViews and set ClickListeners using rootView
        val myTextView = rootView.findViewById<TextView>(R.id.textNext)
        myTextView.setOnClickListener { v: View? ->
            val intent = Intent(requireActivity(), WaterHistoryActivity::class.java)
            startActivity(intent)
        }

        val detailMead = rootView.findViewById<MaterialButton>(R.id.detailMeal)
        detailMead.setOnClickListener { v: View? ->
            val intent = Intent(requireActivity(), DetailMealActivity::class.java)
            startActivity(intent)
        }

        val detailMeadLunch = rootView.findViewById<MaterialButton>(R.id.detailMealLunch)
        detailMeadLunch.setOnClickListener { v: View? ->
            val intent = Intent(requireActivity(), DetailMealLunchActivity::class.java)
            startActivity(intent)
        }

        val detailMeadNight = rootView.findViewById<MaterialButton>(R.id.detailMealNight)
        detailMeadNight.setOnClickListener { v: View? ->
            val intent = Intent(requireActivity(), DetailMealNightActivity::class.java)
            startActivity(intent)
        }

        val notificationDis = rootView.findViewById<TextView>(R.id.notificationDis)
        notificationDis.setOnClickListener {
            showTimePickerDialog()
        }
    }

    private fun initUi() {
        // Initialize RecyclerView and Adapter for breakfast
        val recyclerViewFoodMor: RecyclerView? = rootView.findViewById(R.id.recyclerViewFoodMor)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerViewFoodMor?.layoutManager = linearLayoutManager

        // Initialize the list of food items
        mListFoodMor = ArrayList()
        mMealMenuAdapter = MealMenuAdapter(mListFoodMor)
        recyclerViewFoodMor?.adapter = mMealMenuAdapter

        // Call the database method to fetch breakfast data
        listFoodMorDatabase()
    }

    private fun initUiLunch() {
        // Initialize RecyclerView and Adapter for lunch
        val recyclerViewFoodLun: RecyclerView? = rootView?.findViewById(R.id.recyclerViewFoodLun)

        // Check if recyclerViewFoodLun is not null before using it
        if (recyclerViewFoodLun != null) {
            val linearLayoutManager = LinearLayoutManager(requireContext())
            recyclerViewFoodLun.layoutManager = linearLayoutManager

            // Initialize the list of food items for lunch
            mListFoodLun = ArrayList()
            mMealMenuLunchAdapter = MealMenuLunchAdapter(mListFoodLun)
            recyclerViewFoodLun.adapter = mMealMenuLunchAdapter

            // Call the database method to fetch lunch data
            listFoodLunDatabase()
        } else {
            // Handle the case where recyclerViewFoodLun is null
            // You can log an error or show a message
        }
    }

    private fun initUiNight() {
        // Initialize RecyclerView and Adapter for dinner
        val recyclerViewFoodNight: RecyclerView? =
            rootView?.findViewById(R.id.recyclerViewFoodNight)

        // Check if recyclerViewFoodNight is not null before using it
        if (recyclerViewFoodNight != null) {
            val linearLayoutManager = LinearLayoutManager(requireContext())
            recyclerViewFoodNight.layoutManager = linearLayoutManager

            // Initialize the list of food items for dinner
            mListFoodNNight = ArrayList()
            mMealMenuNightAdapter = MealMenuNightAdapter(mListFoodNNight)
            recyclerViewFoodNight.adapter = mMealMenuNightAdapter

            // Call the database method to fetch dinner data
            listFoodNightDatabase()
        } else {
            // Handle the case where recyclerViewFoodNight is null
            // You can log an error or show a message
        }
    }

    private fun listFoodMorDatabase() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("Meal_Morning")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot != null && snapshot.exists()) {
                    mListFoodMor?.clear()

                    for (dataSnapshot in snapshot.children) {
                        val foodMor = dataSnapshot.getValue(MealMorningInfo::class.java)
                        if (foodMor != null) {
                            mListFoodMor?.add(foodMor)
                        }
                    }

                    mMealMenuAdapter?.notifyDataSetChanged()

                    // Assuming there is a TextView in your UI with ID totalFoodMor
                    val totalFoodMorTextView = rootView.findViewById<TextView>(R.id.totalFoodMor)
                    totalFoodMorTextView.text = "${mMealMenuAdapter?.calculateTotalKcal()}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Failed to retrieve breakfast data!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun listFoodLunDatabase() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("Meal_Lunch")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot != null && snapshot.exists()) {
                    mListFoodLun?.clear()

                    for (dataSnapshot in snapshot.children) {
                        val foodLun = dataSnapshot.getValue(MealLunchInfo::class.java)
                        if (foodLun != null) {
                            mListFoodLun?.add(foodLun)
                        }
                    }

                    mMealMenuLunchAdapter?.notifyDataSetChanged()

                    // Assuming there is a TextView in your UI with ID totalFoodLunch
                    val totalFoodLunTextView = rootView.findViewById<TextView>(R.id.totalFoodLunch)
                    totalFoodLunTextView.text = "${mMealMenuLunchAdapter?.calculateTotalKcal()}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Failed to retrieve lunch data!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun listFoodNightDatabase() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("Meal_Night")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot != null && snapshot.exists()) {
                    mListFoodNNight?.clear()

                    for (dataSnapshot in snapshot.children) {
                        val foodNight = dataSnapshot.getValue(MealNightInfo::class.java)
                        if (foodNight != null) {
                            mListFoodNNight?.add(foodNight)
                        }
                    }

                    mMealMenuNightAdapter?.notifyDataSetChanged()

                    // Assuming there is a TextView in your UI with ID totalFoodNight
                    val totalFoodNightTextView =
                        rootView.findViewById<TextView>(R.id.totalFoodNight)
                    totalFoodNightTextView.text = "${mMealMenuNightAdapter?.calculateTotalKcal()}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Failed to retrieve dinner data!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun updateTextColor() {
        // Cập nhật màu sắc của TextView sau khi thông báo kết thúc
        val txAlarm = rootView.findViewById<TextView>(R.id.txAlarm)
        txAlarm.setTextColor(Color.parseColor("#CCCCCC"))
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar[Calendar.HOUR_OF_DAY]
        val currentMinute = calendar[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val formattedTime =
                    String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                val txAlarm = rootView.findViewById<TextView>(R.id.txAlarm)
                txAlarm.text = formattedTime
                setAlarm(hourOfDay, minute)
            },
            currentHour,
            currentMinute,
            true
        )
        timePickerDialog.show()
    }

    private fun setAlarm(hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hourOfDay
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = 0
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.putExtra("title", "Hãy uống nước đi")
        intent.putExtra("content", "Đến giờ uống nước rồi!")
        val pendingIntent =
            PendingIntent.getBroadcast(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hủy đăng ký BroadcastReceiver khi Fragment kết thúc
//        requireActivity().unregisterReceiver(notificationReceiver)
    }

}