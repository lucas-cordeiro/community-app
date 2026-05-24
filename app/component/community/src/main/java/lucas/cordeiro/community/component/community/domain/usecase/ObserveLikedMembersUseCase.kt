package lucas.cordeiro.community.component.community.domain.usecase

import kotlinx.coroutines.flow.Flow

fun interface ObserveLikedMembersUseCase {
    operator fun invoke(): Flow<Set<Int>>
}
