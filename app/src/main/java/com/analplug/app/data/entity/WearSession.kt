package com.analplug.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "wear_sessions",
    foreignKeys = [
        ForeignKey(
            entity = Plug::class,
            parentColumns = ["id"],
            childColumns = ["plugId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("plugId"), Index("startTime")]
)
data class WearSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val plugId: Long,
    val startTime: Long,
    val endTime: Long? = null,
    val notes: String? = null
)
