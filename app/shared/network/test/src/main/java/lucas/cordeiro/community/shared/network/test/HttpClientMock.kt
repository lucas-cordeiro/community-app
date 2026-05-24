package lucas.cordeiro.community.shared.network.test

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientMock {

    private const val HOST = "tandem2019.web.app"
    private const val BASE_PATH = "/api/"

    fun handler(
        block: suspend MockRequestHandleScope.(request: HttpRequestData) -> HttpResponseData,
    ): HttpClient = HttpClient(MockEngine(block)) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = HOST
                encodedPath = BASE_PATH + encodedPath
            }
        }
    }
}

fun MockRequestHandleScope.success(
    content: String,
    status: HttpStatusCode = HttpStatusCode.OK,
): HttpResponseData = respond(
    content = content,
    status = status,
    headers = headersOf(HttpHeaders.ContentType, "application/json"),
)

fun MockRequestHandleScope.error(
    status: HttpStatusCode = HttpStatusCode.InternalServerError,
): HttpResponseData = respond(
    content = "error",
    status = status,
    headers = headersOf(HttpHeaders.ContentType, "application/json"),
)
