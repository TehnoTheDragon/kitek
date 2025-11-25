package me.tehnothedragon.cpack.resource

import me.tehnothedragon.cpack.CPack
import me.tehnothedragon.cpack.content.ContentPack
import net.minecraft.registry.Registry
import net.minecraft.resource.*
import net.minecraft.text.Text
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

class ContentPackResourcePackProvider(private val supplier: Supplier<Registry<ContentPack>>): ResourcePackProvider {
    private fun createResourcePackProfileFor(contentPack: ContentPack): ResourcePackProfile {
        val info = ResourcePackInfo(
            contentPack.metadata.id,
            Text.literal("${contentPack.metadata.name} Resources"),
            ResourcePackSource.BUILTIN,
            Optional.empty()
        )

        val resourcePack = ContentPackResourcePack(contentPack, info)

        CPack.logger.info("Created ResourcePackInfo $info")

        val position = ResourcePackPosition(
            true,
            ResourcePackProfile.InsertionPosition.BOTTOM,
            false,
        )

        val profile = ResourcePackProfile.create(
            info,
            ContentPackResourcePackFactory(resourcePack),
            ResourceType.CLIENT_RESOURCES,
            position
        )!!

        return profile
    }

    override fun register(consumer: Consumer<ResourcePackProfile>) {
        supplier.get().forEach { contentPack ->
            consumer.accept(createResourcePackProfileFor(contentPack))
        }
    }

    class ContentPackResourcePackFactory(val resourcePack: ResourcePack): ResourcePackProfile.PackFactory {
        override fun open(info: ResourcePackInfo): ResourcePack {
            return this.resourcePack
        }

        override fun openWithOverlays(
            info: ResourcePackInfo,
            metadata: ResourcePackProfile.Metadata,
        ): ResourcePack {
            return this.resourcePack
        }
    }
}