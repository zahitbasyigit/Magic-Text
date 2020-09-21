package zahit.textmagic.ui.video.display

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_magic_text.view.magicTextRemoveButton
import kotlinx.android.synthetic.main.item_magic_text.view.magicTextTitle
import zahit.textmagic.R
import zahit.textmagic.model.MagicText

class MagicTextAdapter(
    private val magicTextListener: MagicTextListener
) : RecyclerView.Adapter<MagicTextAdapter.MagicTextViewHolder>() {

    private val magicTexts = mutableListOf<MagicText>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MagicTextViewHolder =
        MagicTextViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_magic_text, parent, false)
        )

    override fun onBindViewHolder(holder: MagicTextViewHolder, position: Int) {
        holder.bindItem(magicTexts[position])
    }

    override fun getItemCount() = magicTexts.size

    fun replaceMagicTexts(magicTexts: List<MagicText>) {
        this.magicTexts.clear()
        this.magicTexts.addAll(magicTexts)
        notifyDataSetChanged()
    }

    fun removeMagicText(magicText: MagicText) {
        val position = magicTexts.indexOf(magicText)
        if (position == -1) return
        magicTexts.removeAt(position)
        if (magicTexts.isEmpty()) magicTextListener.onNoMoreMagicTextAvailable()
        notifyItemRemoved(position)
    }

    inner class MagicTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(magicText: MagicText) {
            itemView.apply {
                magicTextTitle.text = magicText.text
                magicTextRemoveButton.setOnClickListener {
                    magicTextListener.onMagicTextRemoved(magicText)
                    removeMagicText(magicText)
                }
            }
        }
    }

    interface MagicTextListener {
        fun onMagicTextRemoved(magicText: MagicText)
        fun onNoMoreMagicTextAvailable()
    }
}