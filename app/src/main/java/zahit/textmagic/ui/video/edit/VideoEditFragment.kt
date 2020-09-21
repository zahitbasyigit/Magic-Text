package zahit.textmagic.ui.video.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_video_edit.videoEditProgressVideoView
import kotlinx.android.synthetic.main.fragment_video_edit.videoEditSaveButton
import zahit.textmagic.R
import zahit.textmagic.ui.video.VideoViewModel

class VideoEditFragment : Fragment() {

    private val videoViewModel: VideoViewModel by lazy {
        ViewModelProvider(requireActivity()).get(VideoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            videoViewModel.isRecordFinished = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_video_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoEditSaveButton.setOnClickListener {
            activity?.onBackPressed()
        }
        videoEditProgressVideoView.createMagicTexts(videoViewModel.magicTexts)

        videoEditProgressVideoView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (!videoViewModel.isRecordFinished) {
                        videoEditProgressVideoView.start()
                        videoViewModel.onTouchMove(event.x, event.y)
                    }
                }
                MotionEvent.ACTION_MOVE -> videoViewModel.onTouchMove(event.x, event.y)
                MotionEvent.ACTION_UP -> {
                    videoViewModel.isRecordFinished = true
                    videoEditProgressVideoView.pause()
                }
            }
            v.performClick()
            true
        }
    }

    override fun onPause() {
        super.onPause()
        videoEditProgressVideoView.onPause()
    }

    override fun onResume() {
        super.onResume()
        videoEditProgressVideoView.onResume()
    }
}