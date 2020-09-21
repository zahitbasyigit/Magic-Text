package zahit.textmagic.model

import java.util.concurrent.atomic.AtomicBoolean

/**
 * This class is for live data that should be handled only once such as showing error dialogs.
 * https://gist.github.com/JoseAlcerreca/5b661f1800e1e654f07cc54fe87441af
 */
open class Event<out T>(private val content: T) {

    private val handled = AtomicBoolean(false)

    fun getIfNotHandled(): T? {
        return if (handled.compareAndSet(false, true)) {
            content
        } else {
            null
        }
    }
}