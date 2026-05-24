package lucas.cordeiro.community.di

import android.content.Context
import androidx.startup.Initializer
import lucas.cordeiro.community.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class KoinInitializer : Initializer<KoinApplication> {
    override fun create(context: Context): KoinApplication = startKoin {
        if (BuildConfig.DEBUG) {
            printLogger(Level.ERROR)
        }
        androidContext(context)
        modules(AppDI.provideModules())
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}
