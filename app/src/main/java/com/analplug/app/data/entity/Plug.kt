package com.analplug.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plugs")
data class Plug(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val shortName: String,
    val photoPath: String? = null,
    val material: String? = null,
    val shape: String? = null,
    val size: String? = null,
    val hasVibration: Boolean = false,
    val hasRemoteApp: Boolean = false,
    val remoteAppPackage: String? = null,
    val description: String? = null
)
