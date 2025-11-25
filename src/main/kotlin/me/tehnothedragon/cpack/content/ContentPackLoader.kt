package me.tehnothedragon.cpack.content

import me.tehnothedragon.cpack.CPack
import me.tehnothedragon.cpack.registry.CPackRegistries
import java.nio.file.FileVisitOption
import java.nio.file.Files
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.reflect.jvm.jvmName

internal class ContentPackLoader(val contentPack: ContentPack) {
    fun load() {
        CPack.logger.debug("Loading {}", contentPack)
        tryLoadContent()
    }

    private fun tryLoadContent() {
        val contentDirPath = contentPack.packPath
            .resolve("content")
            .normalize()

        if (!contentDirPath.exists()) return

        CPack.logger.debug("+ Loading Content")

        CPackRegistries.CONTENT_TYPE.forEach<ContentType<*>> { contentType ->
            CPack.logger.debug("+ Applying Content Type `${contentType::class.jvmName}`")
            Files.walk(contentDirPath, 10, FileVisitOption.FOLLOW_LINKS)
                .forEach { file ->
                    if (file.isDirectory()) return@forEach
                    contentType.preprocessAndProcess(this.contentPack, file)
                }
        }
    }
}