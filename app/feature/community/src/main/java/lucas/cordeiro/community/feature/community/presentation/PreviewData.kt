package lucas.cordeiro.community.feature.community.presentation

import lucas.cordeiro.community.component.community.domain.model.CommunityMember

internal object CommunityPreviewData {
    private const val TOPIC = "I can help you learn English and Spanish."

    val members = listOf(
        CommunityMember(
            id = 1,
            topic = TOPIC,
            firstName = "Jonathan",
            pictureUrl = "",
            natives = listOf("en", "de", "it"),
            learns = listOf("ru", "pt"),
            referenceCnt = 12,
            isLiked = false,
            nationality = "gb"
        ),
        CommunityMember(
            id = 2,
            topic = TOPIC,
            firstName = "Martina",
            pictureUrl = "",
            natives = listOf("es", "pt"),
            learns = listOf("ru", "en"),
            referenceCnt = 0,
            isLiked = true,
            nationality = "es"
        ),
        CommunityMember(
            id = 3,
            topic = TOPIC,
            firstName = "Lena",
            pictureUrl = "",
            natives = listOf("en"),
            learns = listOf("pt"),
            referenceCnt = 3,
            isLiked = true,
            nationality = "de"
        ),
        CommunityMember(
            id = 4,
            topic = TOPIC,
            firstName = "Leonardo",
            pictureUrl = "",
            natives = listOf("en"),
            learns = listOf("ru"),
            referenceCnt = 0,
            isLiked = false,
            nationality = "it"
        ),
    )
}
