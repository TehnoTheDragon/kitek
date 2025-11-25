package me.tehnothedragon.kitek.content

import me.tehnothedragon.kitek.Kitek
import me.tehnothedragon.kitek.registry.KitekRegistries
import java.nio.file.FileVisitOption
import java.nio.file.Files
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.reflect.jvm.jvmName

internal class ContentPackLoader(val contentPack: ContentPack) {
    fun load() {
        Kitek.logger.debug("Loading {}", contentPack)
        tryLoadContent()
    }

    private fun tryLoadContent() {
        val contentDirPath = contentPack.packPath
            .resolve("content")
            .normalize()

        if (!contentDirPath.exists()) return

        Kitek.logger.debug("+ Loading Content")

        KitekRegistries.CONTENT_TYPE.forEach<ContentType<*>> { contentType ->
            Kitek.logger.debug("+ Applying Content Type `${contentType::class.jvmName}`")
            Files.walk(contentDirPath, 10, FileVisitOption.FOLLOW_LINKS)
                .forEach { file ->
                    if (file.isDirectory()) return@forEach
                    contentType.preprocessAndProcess(this.contentPack, file)
                }
        }
    }
}