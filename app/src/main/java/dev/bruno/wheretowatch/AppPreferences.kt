package dev.bruno.wheretowatch

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.AppPreferences.ThemeConfig.AUTO
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.di.ApplicationContext
import dev.bruno.wheretowatch.di.SingleIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface AppPreferences {
    val themeConfig: Flow<ThemeConfig>
    suspend fun setThemeConfig(newTheme: ThemeConfig)

    enum class ThemeConfig { AUTO, LIGHT, DARK }
}

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = AppPreferences::class)
class DatastorePreferences @Inject constructor(
    @ApplicationContext context: Context,
) : AppPreferences {

    private val datastore by lazy {
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("WhereToWatchPrefs")
        }
    }

    override val themeConfig: Flow<AppPreferences.ThemeConfig>
        get() = datastore.data.map { pref ->
            val themeName = pref[Keys.themeConfig] ?: AUTO.name
            AppPreferences.ThemeConfig.valueOf(themeName)
        }

    override suspend fun setThemeConfig(newTheme: AppPreferences.ThemeConfig) {
        datastore.edit { mutPref -> mutPref[Keys.themeConfig] = newTheme.name }
    }

    private object Keys {
        val themeConfig = stringPreferencesKey("themeConfigKey")
    }
}