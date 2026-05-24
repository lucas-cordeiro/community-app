package lucas.cordeiro.community.shared.network.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal object HttpClientProvider {
    private const val HOST = "tandem2019.web.app"
    private const val BASE_PATH = "/api/"

    fun create(): HttpClient = HttpClient(OkHttp) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }

        install(Logging) {
            logger = HttpLogger()
            level = LogLevel.INFO
        }

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = HOST
                encodedPath = BASE_PATH + encodedPath
            }
            contentType(ContentType.Application.Json)
        }
    }
}
