package com.example.electrolicpt

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class WorkoutBySetsActivity : AppCompatActivity() {
    private var isDone = false

    private lateinit var youTubePlayer: YouTubePlayer

    private var isFullscreen = false
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullscreen) {
                // if the player is in fullscreen, exit fullscreen
                youTubePlayer.toggleFullscreen()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_by_sets)

        //<editor-fold desc="youtubeView">
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.video_tutorial)
        val fullscreenViewContainer = findViewById<FrameLayout>(R.id.full_screen_view_container)

        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1) // enable full screen button
            .build()

        // we need to initialize manually in order to pass IFramePlayerOptions to the player
        youTubePlayerView.enableAutomaticInitialization = false

        youTubePlayerView.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true

                // the video will continue playing in fullscreenView
                youTubePlayerView.visibility = View.GONE
                fullscreenViewContainer.visibility = View.VISIBLE
                fullscreenViewContainer.addView(fullscreenView)

                // optionally request landscape orientation
                //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            override fun onExitFullscreen() {
                isFullscreen = false

                // the video will continue playing in the player
                youTubePlayerView.visibility = View.VISIBLE
                fullscreenViewContainer.visibility = View.GONE
                fullscreenViewContainer.removeAllViews()

                //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        })

        youTubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@WorkoutBySetsActivity.youTubePlayer = youTubePlayer
                youTubePlayer.loadVideo("2FtpMWMADZw", 0f)
            }
        }, iFramePlayerOptions)

        lifecycle.addObserver(youTubePlayerView)
        //</editor-fold>

        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            finish()
        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->

        }

        val btnPre: CardView = findViewById<CardView>(R.id.btnPrevious)
        btnPre.setOnClickListener(View.OnClickListener {

            val builder = AlertDialog.Builder(this)

            builder.setTitle("Cảnh báo")
            builder.setMessage("Nếu bạn thoát bây giờ thì dữ liệu tập luyện sẽ bị xóa")
            builder.setPositiveButton("Đồng ý", positiveButtonClick)
            builder.setNegativeButton("Không", negativeButtonClick)

            val alertDialog = builder.create()
            alertDialog.show()

        })

        val btnNext: CardView = findViewById<CardView>(R.id.btnNext)
        btnNext.setOnClickListener(View.OnClickListener {
            if(!isDone){
                val builder = AlertDialog.Builder(this)

                builder.setTitle("Thông báo")
                builder.setMessage("Bạn phải hoàn thành bài tập này trước khi chuyển sang bài tập tiếp theo")
                builder.setPositiveButton("Đồng ý", negativeButtonClick)

                val alertDialog = builder.create()
                alertDialog.show()
            }
        })

    }
}