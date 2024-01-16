package com.example.electrolicpt

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.electrolicpt.adapter.MealMenuAdapter
import com.example.electrolicpt.adapter.MealMenuLunchAdapter
import com.example.electrolicpt.adapter.MealMenuNightAdapter
import com.example.electrolicpt.Utils.AlarmReceiver
import com.example.electrolicpt.ObjectInfor.MealLunchInfo
import com.example.electrolicpt.ObjectInfor.MealMorningInfo
import com.example.electrolicpt.ObjectInfor.MealNightInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar
import java.util.Locale

class MealMenuActivity : AppCompatActivity() {



    private var mMealMenuAdapter: MealMenuAdapter? = null
    private var mListFoodMor: MutableList<MealMorningInfo>? = mutableListOf()

    private var mMealMenuLunchAdapter: MealMenuLunchAdapter? = null
    private var mListFoodLun: MutableList<MealLunchInfo>? = mutableListOf()

    private var mMealMenuNightAdapter: MealMenuNightAdapter? = null
    private var mListFoodNNight: MutableList<MealNightInfo>? = mutableListOf()

    var notificationReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if ("NOTIFICATION_SHOWN" == intent.action) {
                // Thông báo đã được hiển thị xong, thực hiện các thao tác cập nhật màu sắc ở đây
                updateTextColor()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_menu)
        initUi()
        initUiLunch()
        initUiNight()

        val intentFilter = IntentFilter("NOTIFICATION_SHOWN")
        registerReceiver(notificationReceiver, intentFilter)

        // Khởi tạo TextView của bạn ở đây
        val myTextView = findViewById<TextView>(R.id.textNext)
        myTextView.setOnClickListener { v: View? ->
            // Tạo Intent để chuyển từ MainActivity sang một Activity khác (ActivityB)
            val intent = Intent(this@MealMenuActivity, WaterHistoryActivity::class.java)

            // Bắt đầu hoạt động mới
            startActivity(intent)
        }
        val DetailMead = findViewById<TextView>(R.id.detailMeal)
        DetailMead.setOnClickListener { v: View? ->
            // Tạo Intent để chuyển từ MainActivity sang một Activity khác (ActivityB)
            val intent = Intent(this@MealMenuActivity, DetailMealActivity::class.java)

            // Bắt đầu hoạt động mới
            startActivity(intent)
        }

        val DetailMeadLunch = findViewById<TextView>(R.id.detailMealLunch)
        DetailMeadLunch.setOnClickListener { v: View? ->
            // Tạo Intent để chuyển từ MainActivity sang một Activity khác (ActivityB)
            val intent = Intent(this@MealMenuActivity, DetailMealLunchActivity::class.java)

            // Bắt đầu hoạt động mới
            startActivity(intent)
        }

        val DetailMeadNight = findViewById<TextView>(R.id.detailMealNight)
        DetailMeadNight.setOnClickListener { v: View? ->
            // Tạo Intent để chuyển từ MainActivity sang một Activity khác (ActivityB)
            val intent = Intent(this@MealMenuActivity, DetailMealNightActivity::class.java)

            // Bắt đầu hoạt động mới
            startActivity(intent)
        }


        // Lắng nghe sự kiện khi nhấp vào TextView "Disable notification"
        val notificationDis = findViewById<TextView>(R.id.notificationDis)
        notificationDis.setOnClickListener { // Hiển thị hộp thoại chọn thời gian
            showTimePickerDialog()
        }

        listFoodMorDatabase
        listFoodLunDatabase
        listFoodNightDatabase


    }

    private fun initUi() {
        // Khởi tạo RecyclerView và Adapter cho DetailMeal
        val recyclerViewFoodMor: RecyclerView = findViewById(R.id.recyclerViewFoodMor)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerViewFoodMor.layoutManager = linearLayoutManager

        // Khởi tạo danh sách thực phẩm
        mListFoodMor = ArrayList()
        mMealMenuAdapter = MealMenuAdapter(mListFoodMor)
        recyclerViewFoodMor.adapter=mMealMenuAdapter


    }

    private fun initUiLunch() {
        // Khởi tạo RecyclerView và Adapter cho DetailMeal
        val recyclerViewFoodLun: RecyclerView = findViewById(R.id.recyclerViewFoodLun)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerViewFoodLun.layoutManager = linearLayoutManager

        // Khởi tạo danh sách thực phẩm
        mListFoodLun = ArrayList()
        mMealMenuLunchAdapter = MealMenuLunchAdapter(mListFoodLun)
        recyclerViewFoodLun.adapter=mMealMenuLunchAdapter


    }

    private fun initUiNight() {
        // Khởi tạo RecyclerView và Adapter cho DetailMeal
        val recyclerViewFoodNight: RecyclerView = findViewById(R.id.recyclerViewFoodNight)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerViewFoodNight.layoutManager = linearLayoutManager

        // Khởi tạo danh sách thực phẩm
        mListFoodNNight = ArrayList()
        mMealMenuNightAdapter = MealMenuNightAdapter(mListFoodNNight)
        recyclerViewFoodNight.adapter=mMealMenuNightAdapter


    }

    private val listFoodMorDatabase: Unit
        private get() {
            // Kết nối đến cơ sở dữ liệu Firebase
            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("Meal_Morning")

            // Lắng nghe sự thay đổi trong dữ liệu
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot != null && snapshot.exists()) {
                        mListFoodMor!!.clear()

                        for (dataSnapshot in snapshot.children) {
                            val foodMor = dataSnapshot.getValue(MealMorningInfo::class.java)
                            if (foodMor != null) {
                                mListFoodMor!!.add(foodMor)
                            }
                        }

                        mMealMenuAdapter?.notifyDataSetChanged()

                        // Cập nhật tổng Kcal sau khi cập nhật dữ liệu
                        val totalFoodMorTextView = findViewById<TextView>(R.id.totalFoodMor)
                        totalFoodMorTextView.text = "${mMealMenuAdapter!!.calculateTotalKcal()}"
                        Log.d("MealMenuActivity", "Dữ liệu: ${mListFoodMor.toString()}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(
                        this@MealMenuActivity,
                        "Lấy danh sách thực phẩm thất bại!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    private val listFoodLunDatabase: Unit
        private get() {
            // Kết nối đến cơ sở dữ liệu Firebase
            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("Meal_Lunch")

            // Lắng nghe sự thay đổi trong dữ liệu
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot != null && snapshot.exists()) {
                        mListFoodLun!!.clear()

                        for (dataSnapshot in snapshot.children) {
                            val foodMor = dataSnapshot.getValue(MealLunchInfo::class.java)
                            if (foodMor != null) {
                                mListFoodLun!!.add(foodMor)
                            }
                        }

                        mMealMenuLunchAdapter?.notifyDataSetChanged()

                        // Cập nhật tổng Kcal sau khi cập nhật dữ liệu
                        val totalFoodLunTextView = findViewById<TextView>(R.id.totalFoodLunch)
                        totalFoodLunTextView.text = "${mMealMenuLunchAdapter!!.calculateTotalKcal()}"

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(
                        this@MealMenuActivity,
                        "Lấy danh sách thực phẩm thất bại!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    private val listFoodNightDatabase: Unit
        private get() {
            // Kết nối đến cơ sở dữ liệu Firebase
            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("Meal_Night")

            // Lắng nghe sự thay đổi trong dữ liệu
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot != null && snapshot.exists()) {
                        mListFoodNNight!!.clear()

                        for (dataSnapshot in snapshot.children) {
                            val foodMor = dataSnapshot.getValue(MealNightInfo::class.java)
                            if (foodMor != null) {
                                mListFoodNNight!!.add(foodMor)
                            }
                        }

                        mMealMenuNightAdapter?.notifyDataSetChanged()

                        // Cập nhật tổng Kcal sau khi cập nhật dữ liệu
                        val totalFoodLunTextView = findViewById<TextView>(R.id.totalFoodNight)
                        totalFoodLunTextView.text = "${mMealMenuNightAdapter!!.calculateTotalKcal()}"

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(
                        this@MealMenuActivity,
                        "Lấy danh sách thực phẩm thất bại!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }


    override fun onDestroy() {
        super.onDestroy()
        // Hủy đăng ký BroadcastReceiver khi Activity kết thúc
        unregisterReceiver(notificationReceiver)
    }

    private fun updateTextColor() {
        // Cập nhật màu sắc của TextView sau khi thông báo kết thúc
        val txAlarm = findViewById<TextView>(R.id.txAlarm)
        txAlarm.setTextColor(Color.parseColor("#CCCCCC"))
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar[Calendar.HOUR_OF_DAY]
        val currentMinute = calendar[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(
            this,
            { view, hourOfDay, minute ->
                val formattedTime =
                    String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                val txAlarm = findViewById<TextView>(R.id.txAlarm)
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
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("title", "Hãy uống nước đi")
        intent.putExtra("content", "Đến giờ uống nước rồi!")
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}