package me.tehnothedragon.kitek.registry

import me.tehnothedragon.kitek.content.ContentPack
import me.tehnothedragon.kitek.content.ContentType
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey

object KitekRegistries {
    val CONTENT_PACK: Registry<ContentPack> = create(KitekRegistryKeys.CONTENT_PACK)
    val CONTENT_TYPE: Registry<ContentType<*>> = create(KitekRegistryKeys.CONTENT_TYPE)

    private fun <T> create(registryKey: RegistryKey<Registry<T>>, attribute: RegistryAttribute = RegistryAttribute.SYNCED): Registry<T> {
        return FabricRegistryBuilder.createSimple(registryKey)
            .attribute(attribute)
            .buildAndRegister()
    }
}