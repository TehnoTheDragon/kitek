package me.tehnothedragon.cpack.registry

import me.tehnothedragon.cpack.content.ContentPack
import me.tehnothedragon.cpack.content.ContentType
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey

object CPackRegistries {
    val CONTENT_PACK: Registry<ContentPack> = create(CPackRegistryKeys.CONTENT_PACK)
    val CONTENT_TYPE: Registry<ContentType<*>> = create(CPackRegistryKeys.CONTENT_TYPE)

    private fun <T> create(registryKey: RegistryKey<Registry<T>>, attribute: RegistryAttribute = RegistryAttribute.SYNCED): Registry<T> {
        return FabricRegistryBuilder.createSimple(registryKey)
            .attribute(attribute)
            .buildAndRegister()
    }
}