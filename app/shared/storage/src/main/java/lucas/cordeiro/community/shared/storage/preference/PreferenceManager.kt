package lucas.cordeiro.community.shared.storage.preference

import kotlinx.coroutines.flow.Flow

interface PreferenceManager {

    suspend fun getStringSet(key: String): Set<String>
    suspend fun setStringSet(key: String, value: Set<String>)
    fun observeStringSet(key: String): Flow<Set<String>>
}
