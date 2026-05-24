package lucas.cordeiro.community.shared.core

import android.util.Log

object Logger {
    private var enabled = false

    fun enable() {
        enabled = true
    }

    fun d(tag: String, message: String) {
        if (enabled) Log.d(tag, message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (enabled) Log.e(tag, message, throwable)
    }
}
