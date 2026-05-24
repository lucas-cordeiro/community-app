package lucas.cordeiro.community.component.community.domain.usecase

fun interface ToggleMemberLikeUseCase {
    suspend operator fun invoke(id: Int)
}
