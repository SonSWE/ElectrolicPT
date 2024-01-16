package com.example.electrolicpt

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.electrolicpt.MealMenuActivity
import com.example.electrolicpt.ObjectInfor.WaterInfo
import com.example.electrolicpt.R
import com.example.electrolicpt.adapter.WaterAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WaterFragment : Fragment() {
    private lateinit var waterAdapter: WaterAdapter
    private val mDataList = mutableListOf<WaterInfo>()
    private lateinit var progressBar: ProgressBar
    private lateinit var myImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.activity_water_history, container, false)

        val mRecyclerView: RecyclerView = rootView.findViewById(R.id.recyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Sử dụng requireContext() để truyền vào WaterAdapter
        waterAdapter = WaterAdapter(mDataList, requireContext())
        mRecyclerView.adapter = waterAdapter

        val addButton: Button = rootView.findViewById(R.id.showTimeBtn)
        addButton.setOnClickListener {
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            mDataList.add(WaterInfo("250", currentTime))
            waterAdapter.notifyItemInserted(mDataList.size - 1)

            // Cập nhật thanh tiến trình
            updateProgressBar()
        }

        myImageView = rootView.findViewById(R.id.myImageView)
        myImageView.setOnClickListener {
            // Xử lý sự kiện khi nhấn vào ImageView
            val intent = Intent(requireActivity(), MealMenuActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }

    private fun updateProgressBar() {
        val progressBarLocal = requireView().findViewById<ProgressBar>(R.id.progress2)
        val totalWater = mDataList.sumBy { it.water.toIntOrNull() ?: 0 }

        // Cập nhật giá trị thanh tiến trình
        progressBarLocal.progress = totalWater

        // Hiển thị giá trị hiện tại trên thanh tiến trình
        val textPer = requireView().findViewById<TextView>(R.id.text_per)
        textPer.text = "$totalWater/2250"
    }

}