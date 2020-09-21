package zahit.textmagic.ui.video.display

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_add_text_dialog.addTextCancelButton
import kotlinx.android.synthetic.main.fragment_add_text_dialog.addTextConfirmButton
import kotlinx.android.synthetic.main.fragment_add_text_dialog.addTextInputEditText
import zahit.textmagic.R
import zahit.textmagic.ui.video.VideoViewModel

class AddTextDialogFragment : AppCompatDialogFragment() {
    private val videoViewModel: VideoViewModel by lazy {
        ViewModelProvider(requireActivity()).get(VideoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_text_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextCancelButton.setOnClickListener {
            dismiss()
        }
        addTextConfirmButton.setOnClickListener {
            val inputText = addTextInputEditText.text?.toString() ?: ""
            videoViewModel.addMagicText(inputText)
            findNavController().navigate(
                AddTextDialogFragmentDirections.actionAddTextDialogFragmentToVideoEditFragment()
            )
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