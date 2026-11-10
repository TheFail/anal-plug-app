package com.analplug.app.data.repository

import com.analplug.app.data.dao.UserProfileDao
import com.analplug.app.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow

class UserProfileRepository(
    private val dao: UserProfileDao
) {
    fun getProfile(): Flow<UserProfile?> = dao.getProfile()

    suspend fun saveProfile(profile: UserProfile) {
        dao.saveProfile(profile)
    }
}
