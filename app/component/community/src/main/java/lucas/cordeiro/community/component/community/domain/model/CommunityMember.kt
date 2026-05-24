package lucas.cordeiro.community.component.community.domain.model

data class CommunityMember(
    val id: Int,
    val topic: String,
    val firstName: String,
    val pictureUrl: String,
    val natives: List<String>,
    val learns: List<String>,
    val referenceCnt: Int,
    val isLiked: Boolean,
) {
    val isNew: Boolean get() = referenceCnt == 0
}
