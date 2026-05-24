package lucas.cordeiro.community.shared.storage.preference

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreferenceManagerImplTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val preferenceManager = PreferenceManagerImpl(context)

    @Before
    fun reset() = runTest {
        preferenceManager.setStringSet(KEY, emptySet())
    }

    @Test
    fun stringSetIsPersistedAndReadBackByNewInstance() = runTest {
        // Given
        preferenceManager.setStringSet(KEY, setOf("1", "2"))

        // When (new instance over the same DataStore — simulates a relaunch)
        val reloaded = PreferenceManagerImpl(context).getStringSet(KEY)

        // Then
        assertEquals(setOf("1", "2"), reloaded)
    }

    @Test
    fun observeStringSetEmitsStoredValue() = runTest {
        // Given
        preferenceManager.setStringSet(KEY, setOf("7"))

        // When
        val observed = preferenceManager.observeStringSet(KEY).first()

        // Then
        assertEquals(setOf("7"), observed)
    }

    private companion object {
        const val KEY = "liked_member_ids"
    }
}
