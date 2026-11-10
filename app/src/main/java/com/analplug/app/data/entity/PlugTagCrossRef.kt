package com.analplug.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "plug_tag_cross_ref",
    primaryKeys = ["plugId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = Plug::class,
            parentColumns = ["id"],
            childColumns = ["plugId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tagId")]
)
data class PlugTagCrossRef(
    val plugId: Long,
    val tagId: Long
)
