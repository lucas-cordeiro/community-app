package lucas.cordeiro.community.component.community.domain.usecase

fun interface LoadCommunityPageUseCase {
    suspend operator fun invoke(page: Int): Int
}
