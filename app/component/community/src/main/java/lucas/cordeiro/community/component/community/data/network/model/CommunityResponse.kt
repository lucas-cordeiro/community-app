package lucas.cordeiro.community.component.community.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommunityResponse(
    @SerialName("response") val response: List<CommunityMemberResponse>,
)

@Serializable
data class CommunityMemberResponse(
    @SerialName("id") val id: Int,
    @SerialName("topic") val topic: String,
    @SerialName("firstName") val firstName: String,
    @SerialName("pictureUrl") val pictureUrl: String,
    @SerialName("natives") val natives: List<String>,
    @SerialName("learns") val learns: List<String>,
    @SerialName("referenceCnt") val referenceCnt: Int,
)
