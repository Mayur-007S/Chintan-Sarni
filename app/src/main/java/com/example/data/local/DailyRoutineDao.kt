package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.DailyRoutineEntity
import com.example.data.model.HabitTargetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyRoutineDao {
    @Query("SELECT * FROM daily_routine_entries WHERE date = :date LIMIT 1")
    fun getEntryForDate(date: String): Flow<DailyRoutineEntity?>

    @Query("SELECT * FROM daily_routine_entries WHERE date = :date LIMIT 1")
    suspend fun getEntryForDateSync(date: String): DailyRoutineEntity?

    @Query("SELECT * FROM daily_routine_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<DailyRoutineEntity>>

    @Query("SELECT * FROM daily_routine_entries ORDER BY date DESC LIMIT :limit")
    fun getRecentEntries(limit: Int): Flow<List<DailyRoutineEntity>>

    @Query("SELECT * FROM daily_routine_entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getEntriesInRange(startDate: String, endDate: String): Flow<List<DailyRoutineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entry: DailyRoutineEntity)

    @Query("DELETE FROM daily_routine_entries WHERE date = :date")
    suspend fun deleteEntry(date: String)

    // Targets
    @Query("SELECT * FROM habit_targets WHERE id = 1 LIMIT 1")
    fun getTargets(): Flow<HabitTargetEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTargets(targets: HabitTargetEntity)
}
