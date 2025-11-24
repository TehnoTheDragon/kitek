package me.tehnothedragon.kitek

import me.tehnothedragon.kitek.content.ContentLoader
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Kitek : ModInitializer {
    companion object {
        const val MODID: String = "kitek"
        internal val logger: Logger = LoggerFactory.getLogger(MODID)
    }

    override fun onInitialize() {
        ContentLoader.start()
    }
}
