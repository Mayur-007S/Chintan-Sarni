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

enum class PracticeFilter {
    ALL,
    NAAMJAP,
    KAPUR_ITR,
    PRARTHANA,
    KRUTAJNATA,
    AAVARAN,
    BHAVJAGRUTI,
    AATMANIVEDAN,
    MANACHA_AADHAV,
    GRANTH_PATHAN,
    DAINANDIN_LIKHAN,
    CHUKA_VICHARNE,
    PRAYASHCHITTA,
    SARANI_LIKHAN,
    CHUKA_LIHINE,
    SWAYAMSUCHANA,
    KSHAMAYACHANA
}

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

    private val _lastSubmittedEntry = MutableStateFlow<DailyRoutineEntity?>(null)
    val lastSubmittedEntry: StateFlow<DailyRoutineEntity?> = _lastSubmittedEntry.asStateFlow()

    private val _selectedDetailEntry = MutableStateFlow<DailyRoutineEntity?>(null)
    val selectedDetailEntry: StateFlow<DailyRoutineEntity?> = _selectedDetailEntry.asStateFlow()

    val allEntries: StateFlow<List<DailyRoutineEntity>> = repository.allEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _historySearchQuery = MutableStateFlow("")
    val historySearchQuery: StateFlow<String> = _historySearchQuery.asStateFlow()

    private val _historyPracticeFilter = MutableStateFlow(PracticeFilter.ALL)
    val historyPracticeFilter: StateFlow<PracticeFilter> = _historyPracticeFilter.asStateFlow()

    fun setHistorySearchQuery(query: String) {
        _historySearchQuery.value = query
    }

    fun setHistoryPracticeFilter(filter: PracticeFilter) {
        _historyPracticeFilter.value = filter
    }

    fun resetHistoryFilters() {
        _historySearchQuery.value = ""
        _historyPracticeFilter.value = PracticeFilter.ALL
    }

    val filteredHistoryEntries: StateFlow<List<DailyRoutineEntity>> = combine(
        allEntries,
        _historySearchQuery,
        _historyPracticeFilter
    ) { entries, query, filter ->
        val trimmed = query.trim()
        entries.filter { entry ->
            val matchesFilter = when (filter) {
                PracticeFilter.ALL -> true
                PracticeFilter.NAAMJAP -> entry.totalNaamjap > 0
                PracticeFilter.KAPUR_ITR -> entry.kapurItrUpay > 0
                PracticeFilter.PRARTHANA -> entry.prarthana > 0
                PracticeFilter.KRUTAJNATA -> entry.krutajnata > 0
                PracticeFilter.AAVARAN -> entry.aavaranKadhne > 0
                PracticeFilter.BHAVJAGRUTI -> entry.bhavjagruti > 0
                PracticeFilter.AATMANIVEDAN -> entry.aatmanivedan > 0
                PracticeFilter.MANACHA_AADHAV -> entry.manachaAadhavDone || entry.manachaAadhavCount > 0
                PracticeFilter.GRANTH_PATHAN -> entry.granthPathanDone || entry.granthPathanCount > 0
                PracticeFilter.DAINANDIN_LIKHAN -> entry.dainandinLikhanDone || entry.dainandinLikhanCount > 0
                PracticeFilter.CHUKA_VICHARNE -> entry.chukaVicharneDone || entry.chukaVicharneCount > 0
                PracticeFilter.PRAYASHCHITTA -> entry.prayashchittaDone || entry.prayashchittaCount > 0
                PracticeFilter.SARANI_LIKHAN -> entry.saraniLikhanDone
                PracticeFilter.CHUKA_LIHINE -> entry.chukaLihineDone || entry.chukaLihineCount > 0
                PracticeFilter.SWAYAMSUCHANA -> entry.swayamsuchanaDone || entry.swayamsuchanaCount > 0
                PracticeFilter.KSHAMAYACHANA -> entry.kshamayachanaDone
            }

            if (!matchesFilter) {
                false
            } else if (trimmed.isEmpty()) {
                true
            } else {
                matchesHistorySearchQuery(entry, trimmed)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
                    totalHabits = entry?.totalHabitsCount ?: 17,
                    completionPercentage = entry?.completionPercentage ?: 0f,
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

    private fun getInitialWeeklyProgress(): List<WeeklyProgressDay> {
        val cal = Calendar.getInstance()
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val list = mutableListOf<WeeklyProgressDay>()
        val fullDateFormat = SimpleDateFormat("d MMM", Locale("mr", "IN"))
        for (i in 0 until 7) {
            val dStr = dateFormat.format(cal.time)
            val dayLabel = dayNameFormatMr.format(cal.time)
            val fullLabel = fullDateFormat.format(cal.time)
            list.add(WeeklyProgressDay(dStr, dayLabel, fullLabel, null))
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        return list
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
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), getInitialWeeklyProgress())

    init {
        viewModelScope.launch {
            val existingList = repository.allEntries.firstOrNull()
            if (existingList.isNullOrEmpty()) {
                // Seed sample routine for yesterday with full completion so submitted list & history show data
                val yesterdaySample = createSampleRoutine(getYesterdayDate()).copy(completedSteps = 3)
                repository.saveEntry(yesterdaySample)
            }
            loadEntryForDate(todayString)
        }
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
            if (existing != null && existing.completedHabitsCount > 0) {
                _currentDraft.value = existing
                // Set step based on completion or default to 1
                _currentStep.value = when {
                    existing.completedSteps >= 3 -> 3
                    existing.completedSteps == 2 -> 3
                    existing.completedSteps == 1 -> 2
                    else -> 1
                }
            } else {
                // If no entry or empty, start with clean draft for user entry
                _currentDraft.value = existing ?: DailyRoutineEntity(date = date)
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

    fun submitStep3(onComplete: (() -> Unit)? = null) {
        val updated = _currentDraft.value.copy(
            completedSteps = 3,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repository.saveEntry(updated)
            // Track the newly submitted entry to show in UI components
            _lastSubmittedEntry.value = updated
            // Clear draft values so the user can immediately enter new values
            _currentDraft.value = DailyRoutineEntity(date = _selectedDate.value)
            // Redirect back to Step 1 (the beginning of the form)
            _currentStep.value = 1
            _snackbarMessage.value = if (_activeLanguage.value == AppLanguage.MARATHI) {
                "चिंतन सारणी यशस्वीरीत्या जतन झाली! खालील नोंद वर टॅप करून तपशील पाहू शकता."
            } else {
                "Chintan Sarani saved successfully! Tap the record card below to inspect full details."
            }
            onComplete?.invoke()
        }
    }

    fun showEntryDetails(entry: DailyRoutineEntity) {
        _selectedDetailEntry.value = entry
    }

    fun dismissEntryDetails() {
        _selectedDetailEntry.value = null
    }

    fun loadEntryIntoDraft(entry: DailyRoutineEntity) {
        _selectedDate.value = entry.date
        _currentDraft.value = entry
        _currentStep.value = 1
        _selectedDetailEntry.value = null
        _snackbarMessage.value = if (_activeLanguage.value == AppLanguage.MARATHI) {
            "${entry.date} ची नोंद फॉर्ममध्ये उघडली आहे."
        } else {
            "Loaded entry for ${entry.date} into form."
        }
    }

    fun startNewEntry() {
        _currentDraft.value = DailyRoutineEntity(date = _selectedDate.value)
        _currentStep.value = 1
        _snackbarMessage.value = if (_activeLanguage.value == AppLanguage.MARATHI) {
            "नवीन नोंदीसाठी फॉर्म पूर्ववत (Clear) करण्यात आला आहे."
        } else {
            "Form cleared and reset for new entry."
        }
    }

    /**
     * Deletes a daily routine entry for the specified date from the database
     * and clears associated detail/draft states if active.
     */
    fun deleteEntry(date: String) {
        viewModelScope.launch {
            repository.deleteEntry(date)
            if (_selectedDetailEntry.value?.date == date) {
                _selectedDetailEntry.value = null
            }
            if (_lastSubmittedEntry.value?.date == date) {
                _lastSubmittedEntry.value = null
            }
            if (_selectedDate.value == date) {
                _currentDraft.value = DailyRoutineEntity(date = date)
                _currentStep.value = 1
            }
            _snackbarMessage.value = AppStrings.deleteSuccessSnackbar(_activeLanguage.value, date)
        }
    }

    /**
     * Creates a sample routine populated with values strictly between 5 and 10
     * across all 16 items and notes.
     */
    fun createSampleRoutine(date: String = todayString): DailyRoutineEntity {
        val isMarathi = _activeLanguage.value == AppLanguage.MARATHI
        return DailyRoutineEntity(
            date = date,
            timestamp = System.currentTimeMillis(),
            // 1) नामजप: कुलदेवी व श्री गुरुदेव दत्त (वेळ मिनिटांत: ५ ते १० मिनिटे)
            naamjapKuldevi = 10,
            naamjapGurudevDatta = 5,
            // 2) कापूर इत्र उपाय
            kapurItrUpay = 5,
            // 3) प्रार्थना
            prarthana = 8,
            // 4) कृतज्ञता
            krutajnata = 6,
            // 5) आवरण काढणे
            aavaranKadhne = 5,
            // Phase 2: Devotion & Reflection
            // 6) भावजागृती चे प्रयत्न
            bhavjagruti = 7,
            // 7) आत्मनिवेदन
            aatmanivedan = 5,
            // 8) मनाचा आढावा घेणे
            manachaAadhavDone = true,
            manachaAadhavCount = 5,
            // 9) दैनंदिन लिखाण
            dainandinLikhanDone = true,
            dainandinLikhanCount = 5,
            // 10) दैनिक ग्रंथ पठण
            granthPathanDone = true,
            granthPathanCount = 10,
            // Phase 3: Error Rectification & Humility
            // 11) चुका विचारणे व सांगणे
            chukaVicharneDone = true,
            chukaVicharneCount = 5,
            // 12) प्रायश्चित व शिक्षा
            prayashchittaDone = true,
            prayashchittaCount = 5,
            // 13) सारणी लिखाण
            saraniLikhanDone = true,
            // 14) चुका लिहिणे
            chukaLihineDone = true,
            chukaLihineCount = 6,
            // 15) स्वयंसूचना सत्र
            swayamsuchanaDone = true,
            swayamsuchanaCount = 5,
            // 16) क्षमायाचना
            kshamayachanaDone = true,
            completedSteps = 1,
            reflectionNotes = if (isMarathi) {
                "आज ५ ते १० मिनिटांचा नामजप व उपाय भावपूर्ण झाले. मन शांत व एकाग्र होते."
            } else {
                "Completed 5 to 10 min/count for daily spiritual practices with peace and focus."
            }
        )
    }

    /**
     * Populates the current form draft with sample values between 5 and 10
     */
    fun fillSampleValues() {
        val sample = createSampleRoutine(_selectedDate.value)
        _currentDraft.value = sample
        _currentStep.value = 1
        viewModelScope.launch {
            repository.saveEntry(sample)
        }
        _snackbarMessage.value = AppStrings.fillSampleSuccessSnackbar(_activeLanguage.value)
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

    fun matchesHistorySearchQuery(entry: DailyRoutineEntity, query: String): Boolean {
        val q = query.lowercase(Locale.ROOT)
        // 1. Direct date match (e.g. "2026", "10", "30", "2026-10-30")
        if (entry.date.contains(q, ignoreCase = true)) {
            return true
        }

        // 2. Reflection notes match
        if (entry.reflectionNotes.contains(q, ignoreCase = true)) {
            return true
        }

        // 3. Practice name in Marathi or English:
        // Naamjap
        if ((q.contains("नामजप") || q.contains("naamjap") || q.contains("कुलदेवी") || q.contains("kuldevi") || q.contains("दत्त") || q.contains("datta") || q.contains("chant")) && entry.totalNaamjap > 0) {
            return true
        }
        // Camphor / Attar
        if ((q.contains("कापूर") || q.contains("kapur") || q.contains("इत्र") || q.contains("अत्तर") || q.contains("attar") || q.contains("camphor") || q.contains("उपाय") || q.contains("remedy")) && entry.kapurItrUpay > 0) {
            return true
        }
        // Prayer
        if ((q.contains("प्रार्थना") || q.contains("prarthana") || q.contains("prayer")) && entry.prarthana > 0) {
            return true
        }
        // Gratitude
        if ((q.contains("कृतज्ञता") || q.contains("krutajnata") || q.contains("gratitude") || q.contains("thanks")) && entry.krutajnata > 0) {
            return true
        }
        // Aavaran
        if ((q.contains("आवरण") || q.contains("aavaran") || q.contains("covering")) && entry.aavaranKadhne > 0) {
            return true
        }
        // Bhavjagruti
        if ((q.contains("भाव") || q.contains("भावजागृती") || q.contains("bhav") || q.contains("devotion")) && entry.bhavjagruti > 0) {
            return true
        }
        // Aatmanivedan
        if ((q.contains("आत्मनिवेदन") || q.contains("aatmanivedan") || q.contains("निवेदन") || q.contains("surrender") || q.contains("confession")) && entry.aatmanivedan > 0) {
            return true
        }
        // Mind review
        if ((q.contains("आढावा") || q.contains("aadhav") || q.contains("मन") || q.contains("mind") || q.contains("review")) && (entry.manachaAadhavDone || entry.manachaAadhavCount > 0)) {
            return true
        }
        // Granth reading
        if ((q.contains("ग्रंथ") || q.contains("granth") || q.contains("पठण") || q.contains("reading") || q.contains("text") || q.contains("book")) && (entry.granthPathanDone || entry.granthPathanCount > 0)) {
            return true
        }
        // Journaling
        if ((q.contains("लिखाण") || q.contains("likhan") || q.contains("दैनंदिन") || q.contains("journal") || q.contains("diary")) && (entry.dainandinLikhanDone || entry.dainandinLikhanCount > 0)) {
            return true
        }
        // Chuka vicharne
        if ((q.contains("विचारणे") || q.contains("vicharne") || q.contains("सांगणे") || q.contains("admit")) && (entry.chukaVicharneDone || entry.chukaVicharneCount > 0)) {
            return true
        }
        // Prayashchitta
        if ((q.contains("प्रायश्चित") || q.contains("prayashchitta") || q.contains("penance") || q.contains("शिक्षा")) && (entry.prayashchittaDone || entry.prayashchittaCount > 0)) {
            return true
        }
        // Sarani
        if ((q.contains("सारणी") || q.contains("sarani") || q.contains("chart")) && entry.saraniLikhanDone) {
            return true
        }
        // Chuka lihine
        if ((q.contains("चूक") || q.contains("चुका") || q.contains("chuka") || q.contains("mistake") || q.contains("fault") || q.contains("error")) && (entry.chukaLihineDone || entry.chukaVicharneDone || entry.chukaLihineCount > 0)) {
            return true
        }
        // Swayamsuchana
        if ((q.contains("स्वयंसूचना") || q.contains("swayam") || q.contains("suchana") || q.contains("autosuggestion")) && (entry.swayamsuchanaDone || entry.swayamsuchanaCount > 0)) {
            return true
        }
        // Kshamayachana
        if ((q.contains("क्षमा") || q.contains("क्षमायाचना") || q.contains("kshamayachana") || q.contains("forgive") || q.contains("forgiveness")) && entry.kshamayachanaDone) {
            return true
        }

        return false
    }

    fun getYesterdayDate(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return dateFormat.format(cal.time)
    }

    fun getTodayDate(): String = todayString
}
