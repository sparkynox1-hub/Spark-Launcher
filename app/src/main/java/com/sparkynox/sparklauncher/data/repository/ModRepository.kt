package com.sparkynox.sparklauncher.data.repository

import com.sparkynox.sparklauncher.data.api.CurseForgeApiService
import com.sparkynox.sparklauncher.data.api.ModrinthApiService
import com.sparkynox.sparklauncher.data.models.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ModRepository — fetches mods from both Modrinth and CurseForge,
 * normalizes them into the unified Mod model.
 * Author: SparkyNox
 */
@Singleton
class ModRepository @Inject constructor(
    private val modrinthApi: ModrinthApiService,
    private val curseForgeApi: CurseForgeApiService
) {

    suspend fun search(
        query: String,
        source: ModSource = ModSource.ALL,
        category: ModCategory = ModCategory.MODS,
        gameVersion: String? = null,
        loader: String? = null,
        offset: Int = 0
    ): List<Mod> = coroutineScope {

        val results = mutableListOf<Mod>()

        when (source) {
            ModSource.MODRINTH -> {
                val facets = buildModrinthFacets(category, gameVersion, loader)
                val res = modrinthApi.searchProjects(query, facets, offset = offset)
                results.addAll(res.hits.map { it.toMod() })
            }
            ModSource.CURSEFORGE -> {
                val classId = category.toCurseForgeClass()
                val loaderType = loader?.toCurseForgeLoaderType()
                val res = curseForgeApi.searchMods(
                    query = query,
                    classId = classId,
                    gameVersion = gameVersion,
                    modLoaderType = loaderType,
                    index = offset
                )
                results.addAll(res.data.map { it.toMod() })
            }
            ModSource.ALL -> {
                val modrinthDeferred = async {
                    try {
                        val facets = buildModrinthFacets(category, gameVersion, loader)
                        modrinthApi.searchProjects(query, facets, limit = 10).hits.map { it.toMod() }
                    } catch (e: Exception) { emptyList() }
                }
                val curseForgeDeferred = async {
                    try {
                        val classId = category.toCurseForgeClass()
                        val loaderType = loader?.toCurseForgeLoaderType()
                        curseForgeApi.searchMods(
                            query = query,
                            classId = classId,
                            gameVersion = gameVersion,
                            modLoaderType = loaderType,
                            pageSize = 10
                        ).data.map { it.toMod() }
                    } catch (e: Exception) { emptyList() }
                }
                results.addAll(modrinthDeferred.await())
                results.addAll(curseForgeDeferred.await())
                // Sort combined by download count descending
                results.sortByDescending { it.downloadCount }
            }
        }
        results
    }

    suspend fun getFeatured(): List<Mod> = coroutineScope {
        val modrinthDeferred = async {
            try { modrinthApi.getFeatured().hits.map { it.toMod() } } catch (e: Exception) { emptyList() }
        }
        val curseForgeDeferred = async {
            try {
                curseForgeApi.searchMods(index = 0, pageSize = 10, sortField = 2).data.map { it.toMod() }
            } catch (e: Exception) { emptyList() }
        }
        val all = modrinthDeferred.await() + curseForgeDeferred.await()
        all.sortedByDescending { it.downloadCount }
    }

    suspend fun getModrinthVersions(
        projectId: String,
        gameVersion: String? = null,
        loader: String? = null
    ): List<ModrinthVersion> {
        val loaders = loader?.let { listOf(it) }
        val versions = gameVersion?.let { listOf(it) }
        return modrinthApi.getVersions(projectId, loaders, versions)
    }

    suspend fun getCurseForgeFiles(
        modId: Int,
        gameVersion: String? = null,
        loader: String? = null
    ): List<CurseForgeFile> {
        val loaderType = loader?.toCurseForgeLoaderType()
        return curseForgeApi.getFiles(modId, gameVersion, loaderType).data
    }

    // ============================================================
    // Mapping helpers
    // ============================================================

    private fun ModrinthHit.toMod() = Mod(
        id = projectId,
        name = title,
        description = description,
        author = author,
        iconUrl = iconUrl ?: "",
        downloadCount = downloads,
        followersCount = follows,
        categories = displayCategories,
        gameVersions = versions,
        loaders = emptyList(),
        source = ModSource.MODRINTH,
        pageUrl = "https://modrinth.com/mod/$slug",
        latestVersion = latestVersion ?: ""
    )

    private fun CurseForgeModData.toMod() = Mod(
        id = id.toString(),
        name = name,
        description = summary,
        author = authors.firstOrNull()?.name ?: "Unknown",
        iconUrl = logo?.url ?: "",
        downloadCount = downloadCount,
        followersCount = thumbsUpCount,
        categories = categories.map { it.name },
        gameVersions = latestFiles.flatMap { it.gameVersions }.distinct(),
        loaders = emptyList(),
        source = ModSource.CURSEFORGE,
        pageUrl = links.websiteUrl ?: ""
    )

    private fun buildModrinthFacets(
        category: ModCategory,
        gameVersion: String?,
        loader: String?
    ): String {
        val facets = mutableListOf<String>()
        facets.add("[\"project_type:${category.toModrinthType()}\"]")
        gameVersion?.let { facets.add("[\"versions:$it\"]") }
        loader?.let { facets.add("[\"categories:$it\"]") }
        return "[${facets.joinToString(",")}]"
    }

    private fun ModCategory.toModrinthType() = when (this) {
        ModCategory.MODS -> "mod"
        ModCategory.RESOURCE_PACKS -> "resourcepack"
        ModCategory.SHADERS -> "shader"
        ModCategory.MODPACKS -> "modpack"
        ModCategory.PLUGINS -> "plugin"
    }

    private fun ModCategory.toCurseForgeClass() = when (this) {
        ModCategory.MODS -> CurseForgeApiService.CLASS_MODS
        ModCategory.RESOURCE_PACKS -> CurseForgeApiService.CLASS_RESOURCE_PACKS
        ModCategory.SHADERS -> CurseForgeApiService.CLASS_SHADERS
        ModCategory.MODPACKS -> CurseForgeApiService.CLASS_MODPACKS
        ModCategory.PLUGINS -> CurseForgeApiService.CLASS_MODS
    }

    private fun String.toCurseForgeLoaderType() = when (lowercase()) {
        "forge" -> 1
        "cauldron" -> 2
        "liteloader" -> 3
        "fabric" -> 4
        "quilt" -> 5
        "neoforge" -> 6
        else -> null
    }
}
