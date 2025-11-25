package me.tehnothedragon.cpack.content

import me.tehnothedragon.cpack.CPack
import me.tehnothedragon.cpack.registry.CPackRegistries
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import java.nio.file.FileVisitOption
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.notExists

object ContentLoader {
    private fun ensureDirectory(path: Path): Path {
        if (path.notExists()) {
            Files.createDirectory(path)
        }
        return path
    }

    val GAME_DIR: Path = FabricLoader.getInstance().gameDir.normalize().toAbsolutePath()
    val CONTENT_DIR: Path = ensureDirectory(GAME_DIR.resolve("content"))

    private fun getContentPacks(): List<Path> {
        return Files.walk(CONTENT_DIR, 1, FileVisitOption.FOLLOW_LINKS)
            .filter { it.isDirectory() && it != CONTENT_DIR }
            .toList()
    }

    private fun loadPacks() {
        getContentPacks().forEach {
            val contentPack = ContentPack.fromPackDir(it)
            Registry.register(
                CPackRegistries.CONTENT_PACK,
                Identifier.of(CPack.MODID, contentPack.metadata.id),
                contentPack
            )
        }
    }

    private fun processPacks() {
        CPackRegistries.CONTENT_PACK.forEach { ContentPackLoader(it).load() }
    }

    internal fun start() {
        loadPacks()
        processPacks()
    }
}