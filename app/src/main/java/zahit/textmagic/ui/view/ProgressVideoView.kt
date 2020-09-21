package zahit.textmagic.ui.view

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.android.synthetic.main.view_progress_video.view.progressBar
import kotlinx.android.synthetic.main.view_progress_video.view.videoFrame
import kotlinx.android.synthetic.main.view_progress_video.view.videoView
import zahit.textmagic.R
import zahit.textmagic.model.Constants.Companion.TOUCH_RECORD_SENSITIVITY_IN_MILLIS
import zahit.textmagic.model.MagicText

class ProgressVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val magicTexts = mutableListOf<MagicText>()
    private val magicTextIdToTextView = mutableMapOf<Int, TextView>()

    private val simpleExoPlayer = SimpleExoPlayer.Builder(context).build()

    private var progressVideoHandler = Looper.myLooper()?.let {
        Handler(it)
    }
    private var progressVideoRunnable: Runnable? = null
    private var isVideoPlaying = false

    init {
        inflate(context, R.layout.view_progress_video, this)
        videoView.player = simpleExoPlayer
        val uri = Uri.parse("asset:///video.mp4")
        val mediaSource = ProgressiveMediaSource
            .Factory(DefaultDataSourceFactory(context, "TextMagic"))
            .createMediaSource(MediaItem.fromUri(uri))

        simpleExoPlayer.repeatMode = Player.REPEAT_MODE_ALL
        simpleExoPlayer.setMediaSource(mediaSource)
        simpleExoPlayer.addListener(object : Player.EventListener {
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                if (state == Player.STATE_READY) {
                    progressBar.max = simpleExoPlayer.duration.toInt()
                }
            }
        })

        progressVideoRunnable = Runnable {
            progressBar.progress = simpleExoPlayer.currentPosition.toInt()
            displaceMagicTexts(simpleExoPlayer.currentPosition.toInt() / TOUCH_RECORD_SENSITIVITY_IN_MILLIS.toInt())
            runProgressVideoRunnable()
        }
        simpleExoPlayer.prepare()
        runProgressVideoRunnable()
    }

    private fun displaceMagicTexts(index: Int) {
        magicTexts.forEach { magicText ->
            magicTextIdToTextView[magicText.textViewId]?.let { magicTextView ->
                if (magicText.points.isEmpty()) {
                    magicTextView.visibility = View.INVISIBLE
                } else {
                    magicTextView.visibility = View.VISIBLE
                    val lastIndex = index.coerceAtMost(magicText.points.lastIndex)
                    val lastPoint = magicText.points[lastIndex]

                    magicTextView.translationX = lastPoint.x - magicTextView.width / 2f
                    magicTextView.translationY = lastPoint.y - magicTextView.height / 2f
                }
            }
        }
    }

    fun createMagicTexts(newMagicTexts: List<MagicText>) {
        magicTexts.clear()
        magicTextIdToTextView.clear()
        magicTexts.addAll(newMagicTexts)

        newMagicTexts.forEach { magicText ->
            val textView = TextView(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                id = magicText.textViewId
                text = magicText.text
                textSize = 32f
                gravity = Gravity.CENTER
                visibility = View.INVISIBLE
                setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                magicTextIdToTextView[magicText.textViewId] = this
            }
            videoFrame.addView(textView)
        }
    }

    fun removeMagicText(magicText: MagicText) {
        magicTexts.remove(magicText)
        videoFrame.removeView(magicTextIdToTextView[magicText.textViewId])
        magicTextIdToTextView.remove(magicText.textViewId)
    }

    fun start() {
        isVideoPlaying = true
        simpleExoPlayer.play()
    }

    fun pause() {
        isVideoPlaying = false
        simpleExoPlayer.pause()
    }

    fun toggle(): Boolean {
        isVideoPlaying = !isVideoPlaying
        if (isVideoPlaying) {
            start()
        } else {
            pause()
        }
        return isVideoPlaying
    }

    fun onResume() {
        if (isVideoPlaying) {
            simpleExoPlayer.play()
        }
        runProgressVideoRunnable()
    }

    fun onPause() {
        progressVideoRunnable?.let {
            progressVideoHandler?.removeCallbacks(it)
        }
        simpleExoPlayer.pause()
    }

    private fun runProgressVideoRunnable() {
        progressVideoRunnable?.let {
            progressVideoHandler?.post(it)
        }
    }
}