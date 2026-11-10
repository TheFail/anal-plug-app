package com.analplug.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.analplug.app.data.entity.Plug
import com.analplug.app.data.entity.PlugTagCrossRef
import com.analplug.app.data.relation.PlugWithHistory
import com.analplug.app.data.relation.PlugWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface PlugDao {
    @Query("SELECT * FROM plugs ORDER BY shortName ASC")
    fun getAllPlugs(): Flow<List<Plug>>

    @Transaction
    @Query("SELECT * FROM plugs ORDER BY shortName ASC")
    fun getAllPlugsWithTags(): Flow<List<PlugWithTags>>

    @Transaction
    @Query("SELECT * FROM plugs WHERE id = :id")
    fun getPlugWithTags(id: Long): Flow<PlugWithTags?>

    @Transaction
    @Query("SELECT * FROM plugs WHERE id = :id")
    fun getPlugWithHistory(id: Long): Flow<PlugWithHistory?>

    @Query("SELECT * FROM plugs WHERE id = :id")
    suspend fun getById(id: Long): Plug?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plug: Plug): Long

    @Update
    suspend fun update(plug: Plug)

    @Delete
    suspend fun delete(plug: Plug)

    @Query("DELETE FROM plug_tag_cross_ref WHERE plugId = :plugId")
    suspend fun clearTagsForPlug(plugId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlugTag(crossRef: PlugTagCrossRef)

    @Query("DELETE FROM plug_tag_cross_ref WHERE plugId = :plugId AND tagId = :tagId")
    suspend fun removeTagFromPlug(plugId: Long, tagId: Long)
}
