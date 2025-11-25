package me.tehnothedragon.cpack.content

import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

interface UnitContentType: ContentType<Unit> {
    override fun preprocessAndProcess(pack: ContentPack, path: Path) {
        process(pack, ContentType.DataHolder(Unit, path.nameWithoutExtension))
    }
}