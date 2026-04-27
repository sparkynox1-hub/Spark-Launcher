package com.sparkynox.sparklauncher.data

import androidx.room.*
import com.sparkynox.sparklauncher.data.models.GameInstance
import kotlinx.coroutines.flow.Flow

@Dao
interface InstanceDao {
    @Query("SELECT * FROM instances ORDER BY lastPlayed DESC")
    fun getAllFlow(): Flow<List<GameInstance>>

    @Query("SELECT * FROM instances ORDER BY lastPlayed DESC")
    fun getAllSync(): List<GameInstance>

    @Query("SELECT * FROM instances WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): GameInstance?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(instance: GameInstance)

    @Update
    suspend fun update(instance: GameInstance)

    @Delete
    suspend fun delete(instance: GameInstance)

    @Query("DELETE FROM instances WHERE id = :id")
    suspend fun deleteById(id: String)
}

@Database(
    entities = [GameInstance::class],
    version = 1,
    exportSchema = false
)
abstract class SparkDatabase : RoomDatabase() {
    abstract fun instanceDao(): InstanceDao
}
