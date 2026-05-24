package lucas.cordeiro.community.di

import lucas.cordeiro.community.component.community.di.CommunityComponentDI
import lucas.cordeiro.community.feature.community.di.CommunityFeatureDI
import lucas.cordeiro.community.shared.network.di.NetworkSharedDI
import lucas.cordeiro.community.shared.storage.di.StorageSharedDI
import org.koin.core.module.Module

object AppDI {
    fun provideModules(): List<Module> = listOf(
        NetworkSharedDI.module,
        StorageSharedDI.module,
        CommunityComponentDI.module,
        CommunityFeatureDI.module,
    )
}
