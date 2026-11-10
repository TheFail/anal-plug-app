package com.analplug.app.data.repository

import com.analplug.app.data.dao.PlugDao
import com.analplug.app.data.entity.Plug
import com.analplug.app.data.entity.PlugTagCrossRef
import com.analplug.app.data.relation.PlugWithHistory
import com.analplug.app.data.relation.PlugWithTags
import kotlinx.coroutines.flow.Flow

class PlugRepository(
    private val dao: PlugDao
) {
    fun getAllPlugs(): Flow<List<Plug>> = dao.getAllPlugs()

    fun getAllPlugsWithTags(): Flow<List<PlugWithTags>> = dao.getAllPlugsWithTags()

    fun getPlugWithTags(id: Long): Flow<PlugWithTags?> = dao.getPlugWithTags(id)

    fun getPlugWithHistory(id: Long): Flow<PlugWithHistory?> = dao.getPlugWithHistory(id)

    suspend fun getById(id: Long): Plug? = dao.getById(id)

    suspend fun savePlug(plug: Plug): Long {
        return if (plug.id == 0L) dao.insert(plug) else {
            dao.update(plug)
            plug.id
        }
    }

    suspend fun deletePlug(plug: Plug) = dao.delete(plug)

    suspend fun setTagsForPlug(plugId: Long, tagIds: List<Long>) {
        dao.clearTagsForPlug(plugId)
        tagIds.forEach { tagId ->
            dao.insertPlugTag(PlugTagCrossRef(plugId, tagId))
        }
    }

    suspend fun removeTagFromPlug(plugId: Long, tagId: Long) {
        dao.removeTagFromPlug(plugId, tagId)
    }
}
