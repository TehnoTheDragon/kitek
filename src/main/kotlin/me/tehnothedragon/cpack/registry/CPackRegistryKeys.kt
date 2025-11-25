package me.tehnothedragon.cpack.registry

import me.tehnothedragon.cpack.CPack
import me.tehnothedragon.cpack.content.ContentPack
import me.tehnothedragon.cpack.content.ContentType
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier

object CPackRegistryKeys {
    val CONTENT_PACK: RegistryKey<Registry<ContentPack>> = create("content_pack")
    val CONTENT_TYPE: RegistryKey<Registry<ContentType<*>>> = create("content_type")

    private fun <T> create(type: String): RegistryKey<Registry<T>> {
        return RegistryKey.ofRegistry(Identifier.of(CPack.MODID, type))
    }
}