package com.example.electrolicpt.adapter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.electrolicpt.R
import com.example.electrolicpt.ObjectInfor.AlarmDataInfo
import com.example.electrolicpt.Utils.SleepAlarmReceiver
import com.google.android.material.switchmaterial.SwitchMaterial
import java.util.Calendar

class AlarmAdapter(private val alarmList: List<AlarmDataInfo>) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hour: TextView = itemView.findViewById(R.id.txthour)
        val minute: TextView = itemView.findViewById(R.id.txtmin)
        val isAM: TextView = itemView.findViewById(R.id.txtisTime)
        val notifi: TextView = itemView.findViewById(R.id.notificationText)
        val imgAddSleep :ImageView = itemView.findViewById(R.id.imgaddSleep)
        val swStart: SwitchMaterial = itemView.findViewById(R.id.swStart)
        // Các thành phần khác trong layout item_alarmsleep.xml
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarmsleep, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = alarmList[position]



        holder.hour.text = currentItem.hour.toString()
        holder.minute.text = currentItem.minute.toString()
        holder.notifi.text = currentItem.notificationText
        holder.isAM.text = if (currentItem.am) "AM" else "PM"

        // Kiểm tra điều kiện và thêm ảnh vào ImageView
        val hourValue = currentItem.hour.toString().toInt()
        val minuteValue = currentItem.minute.toString().toInt()
        if (hourValue > 21) {
            // Nếu giá trị của hour.text > 21, thêm ảnh khác vào ImageView
            holder.imgAddSleep.setImageResource(R.drawable.baseline_bedtime_24)
        } else if (hourValue > 18) {
            // Nếu giá trị của hour.text > 18, thêm ảnh vào ImageView
            holder.imgAddSleep.setImageResource(R.drawable.baseline_wb_cloudy_24)
        } else {
            // Ngược lại, thêm ảnh khác vào ImageView
            holder.imgAddSleep.setImageResource(R.drawable.baseline_brightness_high_24)
        }

        //Đặt sự kiện cho SwitchMaterial
        holder.swStart.setOnCheckedChangeListener { _, isChecked ->


            if (isChecked) {
                // Nếu SwitchMaterial được bật, đặt báo thức
                setAlarm(holder.itemView.context, currentItem)

            } else {
                // Nếu SwitchMaterial được tắt, hủy báo thức
                cancelAlarm(holder.itemView.context)

            }
        }

    }

    private fun setAlarm(context: Context, alarmData: AlarmDataInfo) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, SleepAlarmReceiver::class.java)
        intent.action = "SLEEP"  // Đặt một action tùy chỉnh để phân biệt các intent

        // Truyền thông tin thời gian từ alarmData vào intent
        intent.putExtra("hour", alarmData.hour)
        intent.putExtra("minute", alarmData.minute)
        intent.putExtra("notificationText", alarmData.notificationText)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, alarmData.hour)
        calendar.set(Calendar.MINUTE, alarmData.minute)
        calendar.set(Calendar.SECOND, 0)

        // Đặt báo thức tại thời gian đã chọn
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, SleepAlarmReceiver::class.java)
        intent.action = "SLEEP"

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Hủy báo thức
        alarmManager.cancel(pendingIntent)
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }



}