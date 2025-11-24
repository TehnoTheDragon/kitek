package me.tehnothedragon.kitek.registry

import me.tehnothedragon.kitek.content.ContentPack
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.registry.Registry

object KitekRegistries {
    val CONTENT_PACK: Registry<ContentPack> = FabricRegistryBuilder.createSimple(KitekRegistryKeys.CONTENT_PACK)
        .attribute(RegistryAttribute.SYNCED)
        .buildAndRegister()
}