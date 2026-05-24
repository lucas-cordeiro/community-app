package lucas.cordeiro.community.shared.network.client

import io.ktor.client.plugins.logging.Logger
import lucas.cordeiro.community.shared.core.Logger as AppLogger

internal class HttpLogger : Logger {
    override fun log(message: String) {
        AppLogger.d(TAG, message)
    }

    companion object {
        const val TAG = "HttpLogger"
    }
}
