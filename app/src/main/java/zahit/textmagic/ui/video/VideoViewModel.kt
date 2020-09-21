package zahit.textmagic.ui.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import zahit.textmagic.model.Constants.Companion.TOUCH_RECORD_SENSITIVITY_IN_MILLIS
import zahit.textmagic.model.Event
import zahit.textmagic.model.MagicText
import zahit.textmagic.model.Point

class VideoViewModel : ViewModel() {
    private val _magicTextRemovedEvent = MutableLiveData<Event<MagicText>>()
    val magicTextRemovedEvent: LiveData<Event<MagicText>> = _magicTextRemovedEvent

    val magicTexts = mutableListOf<MagicText>()
    private var lastTouchTimeMillis = 0L

    var isRecordFinished = false

    fun addMagicText(text: String) {
        magicTexts.add(MagicText((text)))
    }

    fun removeMagicText(magicText: MagicText) {
        magicTexts.remove(magicText)
        _magicTextRemovedEvent.postValue(Event(magicText))
    }

    fun onTouchMove(touchX: Float, touchY: Float) {
        if (isRecordFinished) return
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastTouchTimeMillis > TOUCH_RECORD_SENSITIVITY_IN_MILLIS) {
            lastTouchTimeMillis = currentTimeMillis
            magicTexts.last().addPoint(Point(touchX, touchY))
        }
    }
}