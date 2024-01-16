package com.example.electrolicpt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.electrolicpt.adapter.WaterAdapter
import com.example.electrolicpt.ObjectInfor.WaterInfo
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WaterHistoryActivity : AppCompatActivity() {
    private lateinit var waterAdapter: WaterAdapter
    private val mDataList = mutableListOf<WaterInfo>()
    private lateinit var progressBar: ProgressBar
    private lateinit var myImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_history)

        val mRecyclerView: RecyclerView = findViewById(R.id.recyclerView)
        // Thêm LinearLayoutManager vào RecyclerView
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        waterAdapter = WaterAdapter(mDataList, this)
        mRecyclerView.adapter = waterAdapter

        val addButton: MaterialButton = findViewById(R.id.showTimeBtn)
        addButton.setOnClickListener {
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            mDataList.add(WaterInfo("250", currentTime))
            waterAdapter.notifyItemInserted(mDataList.size - 1)

            // Cập nhật ProgressBar
            updateProgressBar()
        }

        myImageView = findViewById(R.id.myImageView)
        myImageView.setOnClickListener { // Xử lý sự kiện click
            val intent = Intent(this, MealMenuActivity::class.java)
            startActivity(intent)
        }
    }

    fun updateProgressBar() {
        val progressBarLocal = findViewById<ProgressBar>(R.id.progress2)
        // Tính tổng lượng nước đã uống
        val totalWater = mDataList.sumBy { it.water.toIntOrNull() ?: 0 }

        // Cập nhật giá trị ProgressBar
        progressBarLocal.progress = totalWater

        // Hiển thị giá trị hiện tại trên ProgressBar
        val textPer = findViewById<TextView>(R.id.text_per)
        textPer.text = "$totalWater/2250"
    }
}