package com.sparkynox.sparklauncher.data.api

import com.sparkynox.sparklauncher.data.models.ModrinthProject
import com.sparkynox.sparklauncher.data.models.ModrinthSearchResult
import com.sparkynox.sparklauncher.data.models.ModrinthVersion
import retrofit2.http.*

/**
 * Modrinth API Service — v2
 * Docs: https://docs.modrinth.com/api-spec
 * Author: SparkyNox
 */
interface ModrinthApiService {

    @GET("search")
    suspend fun searchProjects(
        @Query("query") query: String,
        @Query("facets") facets: String? = null,    // e.g. [["project_type:mod"]]
        @Query("index") index: String = "relevance", // relevance, downloads, follows, newest, updated
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ): ModrinthSearchResult

    @GET("project/{id}")
    suspend fun getProject(
        @Path("id") id: String
    ): ModrinthProject

    @GET("project/{id}/version")
    suspend fun getVersions(
        @Path("id") id: String,
        @Query("loaders") loaders: List<String>? = null,
        @Query("game_versions") gameVersions: List<String>? = null,
        @Query("featured") featured: Boolean? = null
    ): List<ModrinthVersion>

    @GET("version/{id}")
    suspend fun getVersion(
        @Path("id") id: String
    ): ModrinthVersion

    @GET("projects")
    suspend fun getProjects(
        @Query("ids") ids: String  // JSON array string
    ): List<ModrinthProject>

    @GET("search")
    suspend fun getFeatured(
        @Query("facets") facets: String = "[[\"project_type:mod\"]]",
        @Query("index") index: String = "downloads",
        @Query("limit") limit: Int = 10
    ): ModrinthSearchResult

    companion object {
        const val BASE_URL = "https://api.modrinth.com/v2/"
        const val USER_AGENT = "SparkLauncher/1.0.0 (sparkynox@github)"
    }
}
