package zahit.textmagic.ui.video.display

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_video_display.videoDisplayAddTextButton
import kotlinx.android.synthetic.main.fragment_video_display.videoDisplayProgressVideoView
import kotlinx.android.synthetic.main.fragment_video_display.videoDisplayRemoveTextButton
import kotlinx.android.synthetic.main.fragment_video_display.videoDisplayToggleStateButton
import zahit.textmagic.R
import zahit.textmagic.ui.video.VideoViewModel

class VideoDisplayFragment : Fragment() {

    private val videoViewModel: VideoViewModel by lazy {
        ViewModelProvider(requireActivity()).get(VideoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_video_display, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoDisplayProgressVideoView.createMagicTexts(videoViewModel.magicTexts)
        videoDisplayToggleStateButton.setOnClickListener {
            val isVideoPlaying = videoDisplayProgressVideoView.toggle()
            if (isVideoPlaying) {
                videoDisplayToggleStateButton.text = getString(R.string.video_display_pause_button)
                videoDisplayAddTextButton.visibility = View.GONE
                videoDisplayRemoveTextButton.visibility = View.GONE
            } else {
                videoDisplayToggleStateButton.text = getString(R.string.video_display_play_button)
                videoDisplayAddTextButton.visibility = View.VISIBLE
                videoDisplayRemoveTextButton.visibility = View.VISIBLE
            }
        }

        videoDisplayAddTextButton.setOnClickListener {
            findNavController().navigate(VideoDisplayFragmentDirections.actionVideoDisplayFragmentToAddTextDialogFragment())
        }

        videoDisplayRemoveTextButton.setOnClickListener {
            findNavController().navigate(VideoDisplayFragmentDirections.actionVideoDisplayFragmentToRemoveTextDialogFragment())
        }

        videoViewModel.magicTextRemovedEvent.observe(viewLifecycleOwner, {
            it.getIfNotHandled()?.let { magicText ->
                videoDisplayProgressVideoView.removeMagicText(magicText)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        videoDisplayProgressVideoView.onResume()
    }

    override fun onPause() {
        super.onPause()
        videoDisplayProgressVideoView.onPause()
    }
}