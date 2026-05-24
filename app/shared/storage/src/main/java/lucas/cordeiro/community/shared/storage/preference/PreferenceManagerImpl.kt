package lucas.cordeiro.community.shared.storage.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "community_prefs")

internal class PreferenceManagerImpl(context: Context) : PreferenceManager {
    private val dataStore = context.dataStore

    override suspend fun getStringSet(key: String): Set<String> =
        dataStore.data.map { it[stringSetPreferencesKey(key)] ?: emptySet() }.first()

    override suspend fun setStringSet(key: String, value: Set<String>) {
        dataStore.edit { it[stringSetPreferencesKey(key)] = value }
    }

    override suspend fun updateStringSet(key: String, transform: (Set<String>) -> Set<String>) {
        val prefKey = stringSetPreferencesKey(key)
        dataStore.edit { it[prefKey] = transform(it[prefKey] ?: emptySet()) }
    }

    override fun observeStringSet(key: String): Flow<Set<String>> =
        dataStore.data.map { it[stringSetPreferencesKey(key)] ?: emptySet() }
}
