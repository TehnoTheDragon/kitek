package me.tehnothedragon.cpack_test_mod

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.tehnothedragon.cpack.content.CodecContentType
import me.tehnothedragon.cpack.content.ContentPack
import me.tehnothedragon.cpack.content.ContentType
import me.tehnothedragon.cpack.registry.CPackRegistries
import net.fabricmc.api.ModInitializer
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

class CPackTestMod: ModInitializer {
    override fun onInitialize() {
        Registry.register(CPackRegistries.CONTENT_TYPE, Identifier.of("kitek_test_mod", "item"), ItemContentType())
    }

    class ItemContentType: CodecContentType<ItemContentType.Data> {
        override val codec: Codec<Data> = RecordCodecBuilder.create { builder ->
            builder.group(
                Codec.INT.fieldOf("maxStack").forGetter(Data::maxStack)
            ).apply(builder, ::Data)
        }

        override fun process(pack: ContentPack, dataHolder: ContentType.DataHolder<Data>) {
            LoggerFactory.getLogger("kitek.test.mod").info("Register Item ${pack.metadata.id}:${dataHolder.name}")
            Registry.register(
                Registries.ITEM,
                pack.getIdentifierFor(dataHolder.name),
                Item(Item.Settings().maxCount(dataHolder.data.maxStack))
            )
        }

        class Data(val maxStack: Int)
    }
}