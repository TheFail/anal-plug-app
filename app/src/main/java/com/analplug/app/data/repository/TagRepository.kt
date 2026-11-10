package com.analplug.app.data.repository

import com.analplug.app.data.dao.TagDao
import com.analplug.app.data.entity.Tag
import kotlinx.coroutines.flow.Flow

class TagRepository(
    private val dao: TagDao
) {
    fun getAllTags(): Flow<List<Tag>> = dao.getAllTags()

    suspend fun addTag(name: String): Long {
        return dao.insert(Tag(name = name.trim()))
    }

    suspend fun updateTag(tag: Tag) = dao.update(tag)

    suspend fun deleteTag(tag: Tag) = dao.delete(tag)
}
