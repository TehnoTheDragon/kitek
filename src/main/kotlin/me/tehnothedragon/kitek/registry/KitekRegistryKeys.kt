package me.tehnothedragon.kitek.registry

import me.tehnothedragon.kitek.Kitek
import me.tehnothedragon.kitek.content.ContentPack
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier

object KitekRegistryKeys {
    val CONTENT_PACK: RegistryKey<Registry<ContentPack>> = RegistryKey.ofRegistry(Identifier.of(Kitek.MODID, "content_pack"))
}