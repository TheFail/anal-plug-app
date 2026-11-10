package com.analplug.app.data.repository

import com.analplug.app.data.dao.WearSessionDao
import com.analplug.app.data.entity.WearSession
import com.analplug.app.data.relation.WearSessionWithPlug
import kotlinx.coroutines.flow.Flow

class WearSessionRepository(
    private val dao: WearSessionDao
) {
    fun getAllSessions(): Flow<List<WearSession>> = dao.getAllSessions()

    fun getAllSessionsWithPlug(): Flow<List<WearSessionWithPlug>> = dao.getAllSessionsWithPlug()

    fun getSessionsForPlug(plugId: Long): Flow<List<WearSession>> = dao.getSessionsForPlug(plugId)

    fun getSessionsInRange(from: Long, to: Long): Flow<List<WearSession>> =
        dao.getSessionsInRange(from, to)

    suspend fun getById(id: Long): WearSession? = dao.getById(id)

    suspend fun saveSession(session: WearSession): Long {
        return if (session.id == 0L) dao.insert(session) else {
            dao.update(session)
            session.id
        }
    }

    suspend fun deleteSession(session: WearSession) = dao.delete(session)

    fun getAverageDurationMillis(): Flow<Double?> = dao.getAverageDurationMillis()

    suspend fun getMostUsedPlugIds(limit: Int = 5) = dao.getMostUsedPlugIds(limit)
}
