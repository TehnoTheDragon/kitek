package me.tehnothedragon.kitek.utils

import com.google.gson.JsonParser
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.JsonOps
import com.sun.jdi.InvalidTypeException
import java.nio.file.Path

object CodecHelper {
    fun <T> parseCodecWithPath(codec: Codec<T>, path: Path, jsonOps: JsonOps = JsonOps.INSTANCE): DataResult<T> {
        val string: String = path.toFile()
            .also {
                if (!it.exists())
                    throw NoSuchFileException(it)
                if (!it.isFile())
                    throw InvalidTypeException("$it is directory but must be a file")
            }
            .readText()

        return parseCodecWithString(codec, string, jsonOps)
    }

    fun <T> parseCodecWithString(codec: Codec<T>, string: String, jsonOps: JsonOps = JsonOps.INSTANCE): DataResult<T> {
        return codec.parse(jsonOps, JsonParser.parseString(string))
    }
}