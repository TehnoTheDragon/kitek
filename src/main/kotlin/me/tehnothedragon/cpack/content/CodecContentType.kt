package me.tehnothedragon.cpack.content

import com.mojang.serialization.Codec
import me.tehnothedragon.cpack.CPack
import me.tehnothedragon.cpack.content.ContentType.DataHolder
import me.tehnothedragon.cpack.utils.CodecHelper
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

interface CodecContentType<T> : ContentType<T> {
    val codec: Codec<T>

    override fun preprocessAndProcess(pack: ContentPack, path: Path)  {
        val data: T = CodecHelper.parseCodecWithPath<T>(this.codec, path)
            .resultOrPartial(CPack.logger::error)
            .orElseThrow()
        this.process(pack, DataHolder(data, path.nameWithoutExtension))
    }
}