package lucas.cordeiro.community.component.community.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import lucas.cordeiro.community.component.community.data.network.model.CommunityResponse

internal interface CommunityNetworkDataSource {
    suspend fun getCommunity(page: Int): CommunityResponse
}

internal class CommunityNetworkDataSourceImpl(
    private val httpClient: HttpClient,
) : CommunityNetworkDataSource {
    override suspend fun getCommunity(page: Int): CommunityResponse =
        httpClient.get("community_$page.json").body()
}
