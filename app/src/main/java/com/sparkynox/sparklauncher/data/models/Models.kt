package com.sparkynox.sparklauncher.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// ============================================================
// GAME INSTANCE
// ============================================================
@Entity(tableName = "instances")
data class GameInstance(
    @PrimaryKey val id: String,
    val name: String,
    val versionName: String,    // e.g. "1.21.4"
    val versionType: String,    // "release", "snapshot", "old_beta"
    val modLoader: String,      // "vanilla", "fabric", "forge", "quilt", "neoforge"
    val modLoaderVersion: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val lastPlayed: Long = 0L,
    val playtimeMs: Long = 0L,
    val iconPath: String = "",
    val description: String = "",
    val javaArgsOverride: String = "",  // empty = use global
    val ramOverride: Int = 0            // 0 = use global
)

// ============================================================
// MOD (unified across Modrinth + CurseForge)
// ============================================================
enum class ModSource { MODRINTH, CURSEFORGE, ALL }
enum class ModCategory { MODS, RESOURCE_PACKS, SHADERS, MODPACKS, PLUGINS }

data class Mod(
    val id: String,
    val name: String,
    val description: String,
    val author: String,
    val iconUrl: String,
    val downloadCount: Long,
    val followersCount: Long,
    val categories: List<String>,
    val gameVersions: List<String>,
    val loaders: List<String>,
    val source: ModSource,
    val pageUrl: String,
    val latestVersion: String = "",
    val updatedAt: Long = 0L
)

// ============================================================
// DOWNLOAD PROGRESS
// ============================================================
data class DownloadProgress(
    val name: String,
    val percent: Int,
    val bytesDownloaded: Long,
    val totalBytes: Long
)

// ============================================================
// MODRINTH API MODELS
// ============================================================
data class ModrinthSearchResult(
    val hits: List<ModrinthHit>,
    @SerializedName("total_hits") val totalHits: Int,
    val offset: Int,
    val limit: Int
)

data class ModrinthHit(
    @SerializedName("project_id") val projectId: String,
    val slug: String,
    val title: String,
    val description: String,
    @SerializedName("icon_url") val iconUrl: String?,
    val author: String,
    val categories: List<String>,
    @SerializedName("display_categories") val displayCategories: List<String>,
    val downloads: Long,
    val follows: Long,
    @SerializedName("project_type") val projectType: String,
    val versions: List<String>,
    @SerializedName("date_modified") val dateModified: String,
    @SerializedName("latest_version") val latestVersion: String?
)

data class ModrinthProject(
    val id: String,
    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    @SerializedName("icon_url") val iconUrl: String?,
    val downloads: Long,
    val followers: Long,
    val categories: List<String>,
    @SerializedName("game_versions") val gameVersions: List<String>,
    val loaders: List<String>,
    @SerializedName("source_url") val sourceUrl: String?
)

data class ModrinthVersion(
    val id: String,
    val name: String,
    @SerializedName("version_number") val versionNumber: String,
    @SerializedName("game_versions") val gameVersions: List<String>,
    val loaders: List<String>,
    val files: List<ModrinthFile>,
    val downloads: Long,
    @SerializedName("date_published") val datePublished: String
)

data class ModrinthFile(
    val url: String,
    val filename: String,
    val primary: Boolean,
    val size: Long
)

// ============================================================
// CURSEFORGE API MODELS
// ============================================================
data class CurseForgeSearchResult(
    val data: List<CurseForgeModData>,
    val pagination: CurseForgePagination
)

data class CurseForgeProject(
    val data: CurseForgeModData
)

data class CurseForgeModData(
    val id: Int,
    val name: String,
    val summary: String,
    val authors: List<CurseForgeAuthor>,
    val logo: CurseForgeLogo?,
    val downloadCount: Long,
    val thumbsUpCount: Long,
    val categories: List<CurseForgeModCategory>,
    val latestFiles: List<CurseForgeFile>,
    val links: CurseForgeLinks
)

data class CurseForgeAuthor(val id: Int, val name: String)
data class CurseForgeLogo(val url: String)
data class CurseForgeModCategory(val id: Int, val name: String)
data class CurseForgeLinks(val websiteUrl: String?)
data class CurseForgePagination(val index: Int, val pageSize: Int, val resultCount: Int, val totalCount: Long)

data class CurseForgeFilesResult(val data: List<CurseForgeFile>)

data class CurseForgeFile(
    val id: Int,
    val displayName: String,
    val fileName: String,
    val downloadUrl: String?,
    val gameVersions: List<String>,
    val fileLength: Long,
    val downloadCount: Long
)

// ============================================================
// ACCOUNT
// ============================================================
sealed class Account {
    data class Cracked(val username: String) : Account()
    data class Microsoft(
        val username: String,
        val uuid: String,
        val accessToken: String,
        val refreshToken: String,
        val xuid: String
    ) : Account()
}
