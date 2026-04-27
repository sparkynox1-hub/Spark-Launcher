package com.sparkynox.sparklauncher.data.api

import com.sparkynox.sparklauncher.data.models.CurseForgeSearchResult
import com.sparkynox.sparklauncher.data.models.CurseForgeProject
import com.sparkynox.sparklauncher.data.models.CurseForgeFilesResult
import retrofit2.http.*

/**
 * CurseForge API Service — v1
 * Requires API key from CurseForge
 * Author: SparkyNox
 */
interface CurseForgeApiService {

    @GET("mods/search")
    suspend fun searchMods(
        @Query("gameId") gameId: Int = 432, // 432 = Minecraft
        @Query("searchFilter") query: String? = null,
        @Query("classId") classId: Int? = null,  // 6 = mods, 12 = resource packs, 6552 = shaders
        @Query("gameVersion") gameVersion: String? = null,
        @Query("modLoaderType") modLoaderType: Int? = null,  // 1=forge,4=fabric,5=quilt
        @Query("index") index: Int = 0,
        @Query("pageSize") pageSize: Int = 20,
        @Query("sortField") sortField: Int = 2,   // 2=popularity
        @Query("sortOrder") sortOrder: String = "desc"
    ): CurseForgeSearchResult

    @GET("mods/{modId}")
    suspend fun getMod(
        @Path("modId") modId: Int
    ): CurseForgeProject

    @GET("mods/{modId}/files")
    suspend fun getFiles(
        @Path("modId") modId: Int,
        @Query("gameVersion") gameVersion: String? = null,
        @Query("modLoaderType") modLoaderType: Int? = null
    ): CurseForgeFilesResult

    @GET("mods/{modId}/files/{fileId}/download-url")
    suspend fun getDownloadUrl(
        @Path("modId") modId: Int,
        @Path("fileId") fileId: Int
    ): Map<String, String>  // { "data": "url" }

    @GET("mods/featured")
    suspend fun getFeaturedMods(
        @Body body: Map<String, Any>
    ): Map<String, Any>

    companion object {
        const val BASE_URL = "https://api.curseforge.com/v1/"
        const val CLASS_MODS = 6
        const val CLASS_RESOURCE_PACKS = 12
        const val CLASS_SHADERS = 6552
        const val CLASS_MODPACKS = 4471

        const val GAME_ID_MINECRAFT = 432
    }
}
