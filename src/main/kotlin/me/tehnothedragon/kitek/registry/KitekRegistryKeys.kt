package me.tehnothedragon.kitek.registry

import me.tehnothedragon.kitek.Kitek
import me.tehnothedragon.kitek.content.ContentPack
import me.tehnothedragon.kitek.content.ContentType
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier

object KitekRegistryKeys {
    val CONTENT_PACK: RegistryKey<Registry<ContentPack>> = create("content_pack")
    val CONTENT_TYPE: RegistryKey<Registry<ContentType<*>>> = create("content_type")

    private fun <T> create(type: String): RegistryKey<Registry<T>> {
        return RegistryKey.ofRegistry(Identifier.of(Kitek.MODID, type))
    }
}