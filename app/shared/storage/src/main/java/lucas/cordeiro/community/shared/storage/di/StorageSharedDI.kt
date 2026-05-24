package lucas.cordeiro.community.shared.storage.di

import lucas.cordeiro.community.shared.storage.preference.PreferenceManager
import lucas.cordeiro.community.shared.storage.preference.PreferenceManagerImpl
import org.koin.dsl.module

object StorageSharedDI {
    val module = module {
        single<PreferenceManager> { PreferenceManagerImpl(context = get()) }
    }
}
