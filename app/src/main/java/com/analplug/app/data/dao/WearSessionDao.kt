package com.analplug.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.analplug.app.data.entity.WearSession
import com.analplug.app.data.relation.WearSessionWithPlug
import kotlinx.coroutines.flow.Flow

@Dao
interface WearSessionDao {
    @Query("SELECT * FROM wear_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<WearSession>>

    @Query("SELECT * FROM wear_sessions WHERE plugId = :plugId ORDER BY startTime DESC")
    fun getSessionsForPlug(plugId: Long): Flow<List<WearSession>>

    @Query("SELECT * FROM wear_sessions WHERE id = :id")
    suspend fun getById(id: Long): WearSession?

    @Transaction
    @Query("SELECT * FROM wear_sessions ORDER BY startTime DESC")
    fun getAllSessionsWithPlug(): Flow<List<WearSessionWithPlug>>

    @Query("""
        SELECT * FROM wear_sessions 
        WHERE startTime >= :from AND startTime < :to 
        ORDER BY startTime DESC
    """)
    fun getSessionsInRange(from: Long, to: Long): Flow<List<WearSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: WearSession): Long

    @Update
    suspend fun update(session: WearSession)

    @Delete
    suspend fun delete(session: WearSession)

    @Query("DELETE FROM wear_sessions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT AVG(endTime - startTime) FROM wear_sessions WHERE endTime IS NOT NULL")
    fun getAverageDurationMillis(): Flow<Double?>

    @Query("""
        SELECT plugId, COUNT(*) as count 
        FROM wear_sessions 
        GROUP BY plugId 
        ORDER BY count DESC 
        LIMIT :limit
    """)
    suspend fun getMostUsedPlugIds(limit: Int = 5): List<PlugUsage>
}

data class PlugUsage(
    val plugId: Long,
    val count: Int
)
