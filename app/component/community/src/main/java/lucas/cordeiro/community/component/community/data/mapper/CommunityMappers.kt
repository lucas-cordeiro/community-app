package lucas.cordeiro.community.component.community.data.mapper

import lucas.cordeiro.community.component.community.data.network.model.CommunityMemberResponse
import lucas.cordeiro.community.component.community.domain.model.CommunityMember

internal fun CommunityMemberResponse.toDomain(): CommunityMember =
    CommunityMember(
        id = id,
        topic = topic,
        firstName = firstName,
        pictureUrl = pictureUrl,
        natives = natives,
        learns = learns,
        referenceCnt = referenceCnt,
        isLiked = false,
        nationality = natives.firstOrNull()?.let(::flagCodeFor),
    )

private fun flagCodeFor(language: String): String? = when (language.lowercase().trim()) {
    "en" -> "gb"
    "de" -> "de"
    "es" -> "es"
    "it" -> "it"
    "ru" -> "ru"
    "pt" -> "br"
    "ja" -> "jp"
    "ko" -> "kr"
    "fr" -> "fr"
    "zh" -> "cn"
    "nl" -> "nl"
    "tr" -> "tr"
    "pl" -> "pl"
    else -> null
}
