package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.DailyRoutineEntity
import com.example.data.model.HabitTargetEntity
import com.example.data.repository.ChintanSaraniRepository
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.components.DayChartData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class WeeklyProgressDay(
    val dateStr: String,
    val dayLabel: String,
    val fullDateLabel: String,
    val entry: DailyRoutineEntity?
)

class ChintanSaraniViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ChintanSaraniRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = ChintanSaraniRepository(db.dailyRoutineDao())
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val dayNameFormatMr = SimpleDateFormat("EE", Locale("mr", "IN"))
    private val dayNameFormatEn = SimpleDateFormat("EE", Locale.ENGLISH)

    private val todayString: String
        get() = dateFormat.format(Date())

    private val _selectedDate = MutableStateFlow(todayString)
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _activeLanguage = MutableStateFlow(AppLanguage.MARATHI)
    val activeLanguage: StateFlow<AppLanguage> = _activeLanguage.asStateFlow()

    private val _currentStep = MutableStateFlow(1)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    private val _currentDraft = MutableStateFlow(DailyRoutineEntity(date = todayString))
    val currentDraft: StateFlow<DailyRoutineEntity> = _currentDraft.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    val allEntries: StateFlow<List<DailyRoutineEntity>> = repository.allEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val habitTargets: StateFlow<HabitTargetEntity> = repository.habitTargets
        .combine(MutableStateFlow(HabitTargetEntity())) { target, defaultVal ->
            target ?: defaultVal
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HabitTargetEntity())

    // Weekly 7-day chart data derived from entries and selectedDate
    val weeklyChartData: StateFlow<List<DayChartData>> = combine(
        allEntries,
        _selectedDate,
        _activeLanguage
    ) { entries, selectedDateStr, lang ->
        val map = entries.associateBy { it.date }
        val calendar = Calendar.getInstance()
        val list = mutableListOf<DayChartData>()

        // 7 days ending at today or selectedDate
        for (i in 6 downTo 0) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -i)
            val dStr = dateFormat.format(cal.time)
            val entry = map[dStr]
            val dayLabel = if (lang == AppLanguage.MARATHI) {
                dayNameFormatMr.format(cal.time)
            } else {
                dayNameFormatEn.format(cal.time)
            }

            list.add(
                DayChartData(
                    dateStr = dStr,
                    dayLabel = dayLabel,
                    naamjapTotal = entry?.totalNaamjap ?: 0,
                    habitsCompleted = entry?.completedHabitsCount ?: 0,
                    isSelected = dStr == selectedDateStr
                )
            )
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _weekOffset = MutableStateFlow(0)
    val weekOffset: StateFlow<Int> = _weekOffset.asStateFlow()

    fun changeWeek(delta: Int) {
        _weekOffset.value += delta
    }

    fun resetToCurrentWeek() {
        _weekOffset.value = 0
    }

    val weeklyProgressDays: StateFlow<List<WeeklyProgressDay>> = combine(
        allEntries,
        _weekOffset,
        _activeLanguage
    ) { entries, offset, lang ->
        val map = entries.associateBy { it.date }
        val cal = Calendar.getInstance()
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.add(Calendar.WEEK_OF_YEAR, offset)
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val fullDateFormat = SimpleDateFormat("d MMM", if (lang == AppLanguage.MARATHI) Locale("mr", "IN") else Locale.ENGLISH)
        val list = mutableListOf<WeeklyProgressDay>()
        for (i in 0 until 7) {
            val dStr = dateFormat.format(cal.time)
            val dayLabel = if (lang == AppLanguage.MARATHI) {
                dayNameFormatMr.format(cal.time)
            } else {
                dayNameFormatEn.format(cal.time)
            }
            val fullLabel = fullDateFormat.format(cal.time)
            list.add(
                WeeklyProgressDay(
                    dateStr = dStr,
                    dayLabel = dayLabel,
                    fullDateLabel = fullLabel,
                    entry = map[dStr]
                )
            )
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadEntryForDate(todayString)
    }

    fun toggleLanguage() {
        _activeLanguage.value = _activeLanguage.value.toggle()
    }

    fun setLanguage(lang: AppLanguage) {
        _activeLanguage.value = lang
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
        loadEntryForDate(date)
    }

    private fun loadEntryForDate(date: String) {
        viewModelScope.launch {
            val existing = repository.getEntryForDateSync(date)
            if (existing != null) {
                _currentDraft.value = existing
                // Set step based on completion or default to 1
                _currentStep.value = when {
                    existing.completedSteps >= 3 -> 3
                    existing.completedSteps == 2 -> 3
                    existing.completedSteps == 1 -> 2
                    else -> 1
                }
            } else {
                _currentDraft.value = DailyRoutineEntity(date = date)
                _currentStep.value = 1
            }
        }
    }

    fun setStep(step: Int) {
        _currentStep.value = step.coerceIn(1, 3)
    }

    fun updateDraft(transform: (DailyRoutineEntity) -> DailyRoutineEntity) {
        val updated = transform(_currentDraft.value)
        _currentDraft.value = updated
        // Auto-persist draft changes to DB so progress is never lost
        viewModelScope.launch {
            repository.saveEntry(updated)
        }
    }

    fun submitStep1() {
        val updated = _currentDraft.value.copy(
            completedSteps = maxOf(_currentDraft.value.completedSteps, 1)
        )
        _currentDraft.value = updated
        viewModelScope.launch {
            repository.saveEntry(updated)
            _currentStep.value = 2
            _snackbarMessage.value = if (_activeLanguage.value == AppLanguage.MARATHI) {
                "टप्पा १ पूर्ण! आता टप्पा २ (भाव व अंतर्मुखता) भरा."
            } else {
                "Step 1 Complete! Now proceed to Step 2 (Devotion & Reflection)."
            }
        }
    }

    fun submitStep2() {
        val updated = _currentDraft.value.copy(
            completedSteps = maxOf(_currentDraft.value.completedSteps, 2)
        )
        _currentDraft.value = updated
        viewModelScope.launch {
            repository.saveEntry(updated)
            _currentStep.value = 3
            _snackbarMessage.value = if (_activeLanguage.value == AppLanguage.MARATHI) {
                "टप्पा २ पूर्ण! आता टप्पा ३ (दोष निर्मूलन व साधना) भरा."
            } else {
                "Step 2 Complete! Now proceed to Step 3 (Rectification & Humility)."
            }
        }
    }

    fun submitStep3() {
        val updated = _currentDraft.value.copy(
            completedSteps = 3
        )
        _currentDraft.value = updated
        viewModelScope.launch {
            repository.saveEntry(updated)
            _snackbarMessage.value = AppStrings.savedSuccess(_activeLanguage.value)
        }
    }

    fun updateHabitTargets(newTargets: HabitTargetEntity) {
        viewModelScope.launch {
            repository.saveHabitTargets(newTargets)
            _snackbarMessage.value = if (_activeLanguage.value == AppLanguage.MARATHI) {
                "ध्येय यशस्वीरीत्या जतन केले!"
            } else {
                "Habit targets saved successfully!"
            }
        }
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun getYesterdayDate(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return dateFormat.format(cal.time)
    }

    fun getTodayDate(): String = todayString
}
