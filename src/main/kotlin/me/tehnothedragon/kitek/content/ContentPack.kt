package me.tehnothedragon.kitek.content

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.sun.jdi.InvalidTypeException
import me.tehnothedragon.kitek.Kitek
import java.nio.file.Path

class ContentPack(val packPath: Path, val metadata: Meta) {
    companion object {
        fun fromPackDir(path: Path): ContentPack {
            val kitekPackJson = path.resolve("kitek.pack.json").normalize().toFile()
                .also {
                    if (!it.exists())
                        throw NoSuchFileException(it)
                    if (!it.isFile())
                        throw InvalidTypeException("File $it expected to be a file (.json) but found directory instead.")
                }
                .readText()

            val packJson: JsonElement = JsonParser.parseString(kitekPackJson)

            val metadata: Meta = Meta.CODEC
                .parse(JsonOps.INSTANCE, packJson)
                .resultOrPartial(Kitek.logger::warn)
                .orElseThrow()

            return ContentPack(path, metadata)
        }
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