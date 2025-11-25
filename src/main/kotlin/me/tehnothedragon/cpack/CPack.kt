package me.tehnothedragon.cpack

import me.tehnothedragon.cpack.content.ContentLoader
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CPack : ModInitializer {
    companion object {
        const val MODID: String = "cpack"
        internal val logger: Logger = LoggerFactory.getLogger(MODID)
    }

    override fun onInitialize() {
        ContentLoader.start()
    }
}
