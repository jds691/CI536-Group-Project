package com.example.pantryplan.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.pantryplan.core.models.Allergen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.EnumSet
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private val useRelativeDatesPreferencesKey = booleanPreferencesKey("use_relative_dates")
private val expiringSoonAmountPreferencesKey = longPreferencesKey("expiring_soon_amount")

private val allergiesPreferencesKey = byteArrayPreferencesKey("allergies")

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<Preferences>,
) {
    val preferences: Flow<UserPreferences> = userPreferences.data.map { preferences ->
        var allergies =
            readJavaSerializablePreference<EnumSet<Allergen>>(allergiesPreferencesKey, preferences)

        if (allergies == null)
            allergies = EnumSet.noneOf(Allergen::class.java)


        val expiringSoon =
            preferences[expiringSoonAmountPreferencesKey]?.toDuration(DurationUnit.DAYS) ?: 2.days

        val useRelativeDates =
            preferences[useRelativeDatesPreferencesKey] ?: true

        UserPreferences(
            useRelativeDates = useRelativeDates,
            expiringSoonAmount = expiringSoon,

            allergies = allergies!!
        )
    }

    suspend fun setUseRelativeDates(use: Boolean) {
        userPreferences.edit { preferences ->
            preferences[useRelativeDatesPreferencesKey] = use
        }
    }

    suspend fun setExpiringSoonAmount(expiringSoon: Duration) {
        userPreferences.edit { preferences ->
            preferences[expiringSoonAmountPreferencesKey] = expiringSoon.toLong(DurationUnit.DAYS)
        }
    }

    suspend fun setAllergies(allergies: EnumSet<Allergen>) {
        setJavaSerializablePreference(allergiesPreferencesKey, allergies)
    }

    private suspend inline fun <reified T> readJavaSerializablePreference(
        key: Preferences.Key<ByteArray>,
        preferences: Preferences
    ): T? {
        val bytes = preferences[key] ?: return null

        val input = withContext(Dispatchers.IO) {
            val bis = ByteArrayInputStream(bytes)
            ObjectInputStream(bis)
        }
        val obj = withContext(Dispatchers.IO) {
            input.readObject()
        }

        if (obj is T) {
            return obj
        } else {
            throw ClassCastException("Unable to cast preference key '${key.name}' to desired type")
        }
    }

    private suspend fun setJavaSerializablePreference(
        key: Preferences.Key<ByteArray>,
        value: java.io.Serializable
    ) {
        userPreferences.edit { preferences ->
            val bos = ByteArrayOutputStream()

            try {
                val out = ObjectOutputStream(bos)
                out.writeObject(value)
                out.flush()
            } catch (ex: Exception) {
                throw RuntimeException()
            }

            preferences[key] = bos.toByteArray()
        }
    }
}