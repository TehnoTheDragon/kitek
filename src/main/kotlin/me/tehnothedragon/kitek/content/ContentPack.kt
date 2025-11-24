package me.tehnothedragon.kitek.content

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.tehnothedragon.kitek.Kitek
import me.tehnothedragon.kitek.utils.CodecHelper
import net.minecraft.util.Identifier
import java.nio.file.Path

class ContentPack(val packPath: Path, val metadata: Meta) {
    companion object {
        fun fromPackDir(path: Path): ContentPack {
            val metadata: Meta = CodecHelper.parseCodecWithPath(Meta.CODEC, path.resolve("kitek.pack.json").normalize())
                .resultOrPartial(Kitek.logger::error)
                .orElseThrow()

            return ContentPack(path, metadata)
        }
    }

    fun getIdentifierFor(path: String): Identifier {
        return Identifier.of(this.metadata.id, path)
    }

    override fun toString(): String {
        return "ContentPack(id = ${this.metadata.id}, version = ${this.metadata.version})"
    }

    class Meta(
        val packFormat: Int,
        val id: String,
        val version: String,
        val name: String
    ) {
        companion object {
            val CODEC: Codec<Meta> = RecordCodecBuilder.create {
                it.group(
                    Codec.INT.fieldOf("pack_format").forGetter(Meta::packFormat),
                    Codec.STRING.fieldOf("id").forGetter(Meta::id),
                    Codec.STRING.fieldOf("version").forGetter(Meta::version),
                    Codec.STRING.fieldOf("name").forGetter(Meta::name),
                ).apply(it, ::Meta)
            }
        }
    }
}