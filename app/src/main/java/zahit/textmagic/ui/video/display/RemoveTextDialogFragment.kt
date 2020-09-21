package zahit.textmagic.ui.video.display

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_remove_text_dialog.magicTextRecyclerView
import kotlinx.android.synthetic.main.fragment_remove_text_dialog.magicTextRemoveEmptyDescription
import zahit.textmagic.R
import zahit.textmagic.model.MagicText
import zahit.textmagic.ui.video.VideoViewModel

class RemoveTextDialogFragment : AppCompatDialogFragment() {
    private lateinit var magicTextAdapter: MagicTextAdapter

    private val videoViewModel: VideoViewModel by lazy {
        ViewModelProvider(requireActivity()).get(VideoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_remove_text_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        magicTextAdapter = MagicTextAdapter(object : MagicTextAdapter.MagicTextListener {
            override fun onMagicTextRemoved(magicText: MagicText) {
                videoViewModel.removeMagicText(magicText)
                if (magicTextAdapter.itemCount == 0) {
                    dismiss()
                }
            }

            override fun onNoMoreMagicTextAvailable() {
                dismiss()
            }
        })

        magicTextRecyclerView.layoutManager = LinearLayoutManager(view.context)
        magicTextRecyclerView.adapter = magicTextAdapter
        magicTextAdapter.replaceMagicTexts(videoViewModel.magicTexts)
        if (magicTextAdapter.itemCount == 0) {
            magicTextRemoveEmptyDescription.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}