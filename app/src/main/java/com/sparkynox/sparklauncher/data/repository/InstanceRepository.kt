package com.sparkynox.sparklauncher.data.repository

import com.sparkynox.sparklauncher.data.InstanceDao
import com.sparkynox.sparklauncher.data.models.GameInstance
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstanceRepository @Inject constructor(
    private val dao: InstanceDao
) {
    fun getInstancesFlow(): Flow<List<GameInstance>> = dao.getAllFlow()

    fun getInstancesSync(): List<GameInstance> = dao.getAllSync()

    suspend fun createInstance(
        name: String,
        versionName: String,
        versionType: String,
        modLoader: String = "vanilla",
        modLoaderVersion: String = ""
    ): GameInstance {
        val instance = GameInstance(
            id = UUID.randomUUID().toString(),
            name = name,
            versionName = versionName,
            versionType = versionType,
            modLoader = modLoader,
            modLoaderVersion = modLoaderVersion
        )
        dao.insert(instance)
        return instance
    }

    suspend fun updateLastPlayed(id: String) {
        val instance = dao.getById(id) ?: return
        dao.update(instance.copy(lastPlayed = System.currentTimeMillis()))
    }

    suspend fun delete(instance: GameInstance) = dao.delete(instance)

    suspend fun update(instance: GameInstance) = dao.update(instance)
}
