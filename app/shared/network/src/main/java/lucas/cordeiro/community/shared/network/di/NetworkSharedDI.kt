package lucas.cordeiro.community.shared.network.di

import io.ktor.client.HttpClient
import lucas.cordeiro.community.shared.network.client.HttpClientProvider
import org.koin.dsl.module

object NetworkSharedDI {
    val module = module {
        single<HttpClient> { HttpClientProvider.create() }
    }
}
