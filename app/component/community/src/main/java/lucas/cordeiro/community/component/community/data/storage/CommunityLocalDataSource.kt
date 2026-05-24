package lucas.cordeiro.community.component.community.data.storage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lucas.cordeiro.community.shared.storage.preference.PreferenceManager

internal interface CommunityLocalDataSource {
    fun observeLikedIds(): Flow<Set<Int>>
    suspend fun toggleLike(id: Int)
}

internal class CommunityLocalDataSourceImpl(
    private val preferenceManager: PreferenceManager,
) : CommunityLocalDataSource {

    override fun observeLikedIds(): Flow<Set<Int>> =
        preferenceManager.observeStringSet(LIKED_IDS_KEY).map { it.toIntSet() }

    override suspend fun toggleLike(id: Int) {
        val key = id.toString()
        preferenceManager.updateStringSet(LIKED_IDS_KEY) { current ->
            if (key in current) current - key else current + key
        }
    }

    private fun Set<String>.toIntSet(): Set<Int> = mapNotNull(String::toIntOrNull).toSet()

    companion object {
        private const val LIKED_IDS_KEY = "liked_member_ids"
    }
}
