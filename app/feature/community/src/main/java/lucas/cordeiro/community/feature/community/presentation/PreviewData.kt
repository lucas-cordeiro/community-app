package lucas.cordeiro.community.feature.community.presentation

import lucas.cordeiro.community.component.community.domain.model.CommunityMember

internal object CommunityPreviewData {
    private const val TOPIC = "I can help you learn English and Spanish."

    val members = listOf(
        CommunityMember(1, TOPIC, "Jonathan", "", listOf("en, de, it"), listOf("ru, pt"), 12, isLiked = false),
        CommunityMember(2, TOPIC, "Martina", "", listOf("es, pt"), listOf("ru, en"), 0, isLiked = true),
        CommunityMember(3, TOPIC, "Lena", "", listOf("en"), listOf("pt"), 3, isLiked = true),
        CommunityMember(4, TOPIC, "Leonardo", "", listOf("en"), listOf("ru"), 0, isLiked = false),
    )
}
