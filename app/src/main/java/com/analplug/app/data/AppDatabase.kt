package com.analplug.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.analplug.app.data.dao.PlugDao
import com.analplug.app.data.dao.TagDao
import com.analplug.app.data.dao.UserProfileDao
import com.analplug.app.data.dao.WearSessionDao
import com.analplug.app.data.entity.Plug
import com.analplug.app.data.entity.PlugTagCrossRef
import com.analplug.app.data.entity.Tag
import com.analplug.app.data.entity.UserProfile
import com.analplug.app.data.entity.WearSession

@Database(
    entities = [
        UserProfile::class,
        Plug::class,
        Tag::class,
        PlugTagCrossRef::class,
        WearSession::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun plugDao(): PlugDao
    abstract fun tagDao(): TagDao
    abstract fun wearSessionDao(): WearSessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "anal_plug_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
