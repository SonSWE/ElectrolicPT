package com.example.electrolicpt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import android.widget.ToggleButton
import com.example.electrolicpt.ObjectInfor.AlarmDataInfo
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddAlarmSleepActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
//    private lateinit var swStart: SwitchMaterial
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm_sleep)

        // Khởi tạo Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("alarms")

        val imageView: ImageView = findViewById(R.id.Sleepback)
        imageView.setOnClickListener {
        super.onBackPressed()
        }
    
        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            // Lấy giá trị từ TimePicker
            val timePicker: TimePicker = findViewById(R.id.fragment_createalarm_timePicker)
            val hour: Int = timePicker.hour
            val minute: Int = timePicker.minute

            // Lấy giá trị từ ToggleButtons
            val selectedDays = mutableListOf<String>()
            val toggleButtons = listOf(
                findViewById<ToggleButton>(R.id.fragment_createalarm_checkMon),
                findViewById<ToggleButton>(R.id.fragment_createalarm_checkTue),
                findViewById<ToggleButton>(R.id.fragment_createalarm_checkWed),
                findViewById<ToggleButton>(R.id.fragment_createalarm_checkThu),
                findViewById<ToggleButton>(R.id.fragment_createalarm_checkFri),
                findViewById<ToggleButton>(R.id.fragment_createalarm_checkSat),
                findViewById<ToggleButton>(R.id.fragment_createalarm_checkSun),

            )

            for (toggleButton in toggleButtons) {
                if (toggleButton.isChecked) {
                    selectedDays.add(toggleButton.textOn.toString())
                }
            }

            // Lấy giá trị từ EditText
            val notificationText: String = (findViewById<EditText>(R.id.txtName)).text.toString()

            fun determineIsAM(hour: Int): Boolean {
                return hour < 12
            }

            // Tạo một đối tượng dữ liệu
            val isAM: Boolean = determineIsAM(hour)
            val alarmData = AlarmDataInfo(hour, minute, selectedDays, notificationText, isAM)


            // Đẩy dữ liệu lên Firebase
            val key: String? = databaseReference.push().key
            if (key != null) {
                databaseReference.child(key).setValue(alarmData)

                showToast("Báo thức đã được thêm thành công!")
                super.onBackPressed()
            }
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



}