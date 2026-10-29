package com.example.data.repository

import com.example.data.local.DailyRoutineDao
import com.example.data.model.DailyRoutineEntity
import com.example.data.model.HabitTargetEntity
import kotlinx.coroutines.flow.Flow

class ChintanSaraniRepository(private val dao: DailyRoutineDao) {

    fun getEntryForDate(date: String): Flow<DailyRoutineEntity?> = dao.getEntryForDate(date)

    suspend fun getEntryForDateSync(date: String): DailyRoutineEntity? = dao.getEntryForDateSync(date)

    val allEntries: Flow<List<DailyRoutineEntity>> = dao.getAllEntries()

    fun getRecentEntries(limit: Int = 30): Flow<List<DailyRoutineEntity>> = dao.getRecentEntries(limit)

    fun getEntriesInRange(startDate: String, endDate: String): Flow<List<DailyRoutineEntity>> =
        dao.getEntriesInRange(startDate, endDate)

    suspend fun saveEntry(entry: DailyRoutineEntity) {
        dao.insertOrUpdate(entry)
    }

    suspend fun deleteEntry(date: String) {
        dao.deleteEntry(date)
    }

    val habitTargets: Flow<HabitTargetEntity?> = dao.getTargets()

    suspend fun saveHabitTargets(targets: HabitTargetEntity) {
        dao.saveTargets(targets)
    }
}
