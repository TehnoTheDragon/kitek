package me.tehnothedragon.kitek.content

import com.mojang.serialization.Codec
import me.tehnothedragon.kitek.Kitek
import me.tehnothedragon.kitek.content.ContentType.DataHolder
import me.tehnothedragon.kitek.utils.CodecHelper
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

interface CodecContentType<T> : ContentType<T> {
    val codec: Codec<T>

    override fun preprocessAndProcess(pack: ContentPack, path: Path)  {
        val data: T = CodecHelper.parseCodecWithPath<T>(this.codec, path)
            .resultOrPartial(Kitek.logger::error)
            .orElseThrow()
        this.process(pack, DataHolder(data, path.nameWithoutExtension))
    }
}