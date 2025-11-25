package me.tehnothedragon.cpack.content

import java.nio.file.Path

interface ContentType<T> {
    fun process(pack: ContentPack, dataHolder: DataHolder<T>)
    fun preprocessAndProcess(pack: ContentPack, path: Path)

    class DataHolder<T>(
        val data: T,
        val name: String,
    )
}