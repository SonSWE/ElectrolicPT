package com.example.electrolicpt.Utils



import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.electrolicpt.R
import java.util.Calendar

class SleepAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "SLEEP") {
            val hourOnCard = intent.getIntExtra("hour", 0)
            val minuteOnCard = intent.getIntExtra("minute", 0)
            val notificationText = intent.getStringExtra("notificationText") ?: ""

            val currentTime = Calendar.getInstance()
            val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val currentMinute = currentTime.get(Calendar.MINUTE)

//            Log.d("YourAlarmReceiver", "Current Time: $currentHour:$currentMinute")
//            Log.d("YourAlarmReceiver", "Time on Card: $hourOnCard:$minuteOnCard")

            if (currentHour == hourOnCard && currentMinute == minuteOnCard) {
                // Thời gian hiện tại trùng với thời gian trên thẻ, thực hiện hành động cần thiết
//                Log.d("YourAlarmReceiver", "Alarm triggered!")

                // Phát âm thanh báo thức
                val mediaPlayer = MediaPlayer.create(context, R.raw.sleepalarm)
                mediaPlayer.isLooping = true
                mediaPlayer.start()

                // Bạn có thể thêm hành động cần thiết ở đây, ví dụ:
                val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channelId = "YourChannelId"
                val channelName = "YourChannelName"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
                    notificationManager.createNotificationChannel(channel)
                }

                val notificationBuilder = NotificationCompat.Builder(context, channelId)
                    .setContentTitle("Báo thức nhắc nhở")
                    .setContentText(notificationText)
                    .setSmallIcon(R.drawable.ic_bell)
                    .setAutoCancel(true)

                notificationManager.notify(1, notificationBuilder.build())

                // (Tùy chọn) Đặt một hẹn giờ để dừng âm thanh sau một khoảng thời gian
                Handler(Looper.getMainLooper()).postDelayed({
                    mediaPlayer.stop()
                    mediaPlayer.release()
                }, 10000) // Dừng âm thanh sau 10 giây (có thể điều chỉnh theo nhu cầu của bạn)

            } else {
                Log.d("YourAlarmReceiver", "Alarm not triggered. Time does not match.")
            }
        }
    }
}