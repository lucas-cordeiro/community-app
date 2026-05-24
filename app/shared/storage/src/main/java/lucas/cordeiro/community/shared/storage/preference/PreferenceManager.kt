package lucas.cordeiro.community.shared.storage.preference

import kotlinx.coroutines.flow.Flow

interface PreferenceManager {
    suspend fun getString(key: String): String?
    suspend fun setString(key: String, value: String)

    suspend fun getStringSet(key: String): Set<String>
    suspend fun setStringSet(key: String, value: Set<String>)
    fun observeStringSet(key: String): Flow<Set<String>>

    suspend fun removeKey(key: String)
    suspend fun clear()
}
