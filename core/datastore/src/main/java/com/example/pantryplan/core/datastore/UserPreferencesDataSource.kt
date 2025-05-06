package com.example.pantryplan.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
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

private val allergiesPreferencesKey = byteArrayPreferencesKey("allergies")
private val intolerancesPreferencesKey = byteArrayPreferencesKey("intolerances")

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<Preferences>,
) {
    val preferences: Flow<UserPreferences> = userPreferences.data.map { preferences ->
        var allergies =
            readJavaSerializablePreference<EnumSet<Allergen>>(allergiesPreferencesKey, preferences)

        if (allergies == null)
            allergies = EnumSet.noneOf(Allergen::class.java)

        var intolerances = readJavaSerializablePreference<EnumSet<Allergen>>(
            intolerancesPreferencesKey,
            preferences
        )

        if (intolerances == null)
            intolerances = EnumSet.noneOf(Allergen::class.java)

        UserPreferences(
            allergies = allergies!!,
            intolerances = intolerances!!
        )
    }

    suspend fun setAllergies(allergies: EnumSet<Allergen>) {
        setJavaSerializablePreference(allergiesPreferencesKey, allergies)
    }

    suspend fun setIntolerances(allergies: EnumSet<Allergen>) {
        setJavaSerializablePreference(intolerancesPreferencesKey, allergies)
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