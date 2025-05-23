package com.example.pantryplan.settings

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryplan.core.data.access.repository.UserPreferencesRepository
import com.example.pantryplan.core.models.Allergen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.EnumSet
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val uiState: StateFlow<SettingsUiState> = userPreferencesRepository.preferences
        .map { preferences ->
            val settings = UserSettings(
                showRelativeDates = mutableStateOf(preferences.useRelativeDates),
                expiringSoonAmount = mutableStateOf(preferences.expiringSoonAmount),
                expectedMealCount = mutableIntStateOf(preferences.expectedMealCount)
            )
            settings.allergies.addAll(preferences.allergies)

            SettingsUiState(
                settings = settings
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = SettingsUiState(),
        )

    fun updateUseRelativeDates(use: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setUseRelativeDates(use)
        }
    }

    fun updateExpiringSoonAmount(amount: Duration) {
        viewModelScope.launch {
            userPreferencesRepository.setExpiringSoonAmount(amount)
        }
    }

    fun updateAllergies(allergies: EnumSet<Allergen>) {
        viewModelScope.launch {
            userPreferencesRepository.setAllergies(allergies)
        }
    }

    fun updateExpectedMealCount(expectedMeals: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setExpectedMealsCount(expectedMeals)
        }
    }
}

data class UserSettings(
    val showRelativeDates: MutableState<Boolean> = mutableStateOf(true),
    val expiringSoonAmount: MutableState<Duration> = mutableStateOf(2.days),

    val allergies: SnapshotStateSet<Allergen> = mutableStateSetOf(),

    val expectedMealCount: MutableIntState = mutableIntStateOf(3)
)

data class SettingsUiState(
    val settings: UserSettings = UserSettings()
)