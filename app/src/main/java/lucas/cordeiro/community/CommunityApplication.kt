package lucas.cordeiro.community

import android.app.Application
import lucas.cordeiro.community.shared.core.Logger

class CommunityApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Logger.enable()
        }
    }
}
