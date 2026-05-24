package lucas.cordeiro.community.component.community.stubs

import lucas.cordeiro.community.component.community.data.network.model.CommunityMemberResponse
import lucas.cordeiro.community.component.community.data.network.model.CommunityResponse

object CommunityResponseStub {

    fun member(
        id: Int = 1,
        topic: String = "topic",
        firstName: String = "Name$id",
        pictureUrl: String = "",
        natives: List<String> = listOf("en"),
        learns: List<String> = listOf("pt"),
        referenceCnt: Int = id,
    ): CommunityMemberResponse = CommunityMemberResponse(
        id = id,
        topic = topic,
        firstName = firstName,
        pictureUrl = pictureUrl,
        natives = natives,
        learns = learns,
        referenceCnt = referenceCnt,
    )

    fun response(members: List<CommunityMemberResponse>): CommunityResponse =
        CommunityResponse(members)

    fun responseOf(size: Int, startId: Int = 1): CommunityResponse =
        response((startId until startId + size).map { member(id = it) })
}
