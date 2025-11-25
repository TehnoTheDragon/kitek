package me.tehnothedragon.kitek.resource

import com.google.gson.Gson
import com.google.gson.JsonObject
import me.tehnothedragon.kitek.Kitek
import me.tehnothedragon.kitek.content.ContentPack
import net.minecraft.SharedConstants
import net.minecraft.resource.*
import net.minecraft.resource.metadata.ResourceMetadataReader
import net.minecraft.util.Identifier
import net.minecraft.util.PathUtil
import org.apache.commons.io.IOUtils
import java.io.InputStream
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import java.util.regex.Pattern
import kotlin.io.path.exists

class ContentPackResourcePack(
    val contentPack: ContentPack,
    val contentPackResourcePackInfo: ResourcePackInfo
): ResourcePack {
    companion object {
        private val ASSETS_PREFIX = ResourceType.CLIENT_RESOURCES.directory + "/"
        private val DATA_PREFIX = ResourceType.SERVER_DATA.directory + "/"
        private val RESOURCE_PACK_PATH = Pattern.compile("[a-z0-9-_.]+")
    }

    private val namespaces: Map<ResourceType, Set<String>> = scanNamespaces()

    private fun scanNamespaces(): Map<ResourceType, Set<String>> {
        val result: MutableMap<ResourceType, Set<String>> = EnumMap(ResourceType::class.java)

        for (type in ResourceType.entries) {
            val namespaces: MutableSet<String> = HashSet()

            val directory = this.contentPack.packPath.resolve(type.directory)
            if (!Files.isDirectory(directory)) continue

            val separator = this.contentPack.packPath.fileSystem.separator

            for (subpath in Files.newDirectoryStream(directory)) {
                if (!Files.isDirectory(subpath)) continue

                val filename = subpath.fileName.toString()
                    .replace(separator, "")

                if (!RESOURCE_PACK_PATH.matcher(filename).matches()) {
                    Kitek.logger.warn("Kitek ContentPackResourcePack: ignored invalid namespace: $filename in content pack ID ${this.contentPack.metadata.id}")
                    continue
                }

                namespaces.add(filename)
            }

            result[type] = namespaces
        }

        return result
    }

    private fun getPackMeta(): InputStream {
        val description = "Content Pack '${this.contentPack.metadata.name}' Resources"

        val pack = JsonObject()
        pack.addProperty("pack_format", SharedConstants.getGameVersion().getResourceVersion(ResourceType.CLIENT_RESOURCES))
        pack.addProperty("description", description)

        val metadata = JsonObject()
        metadata.add("pack", pack)

        return IOUtils.toInputStream(Gson().toJson(metadata), Charsets.UTF_8)
    }

    private fun getPath(filename: String): Path? {
        if (hasAbsentNamespace(filename)) return null

        val childPath = this.contentPack.packPath.resolve(filename.replace("/", this.contentPack.packPath.fileSystem.separator)).toAbsolutePath().normalize()
        if (childPath.startsWith(this.contentPack.packPath) && childPath.exists()) {
            return childPath
        }

        return null
    }

    private fun hasAbsentNamespace(filename: String): Boolean {
        var prefixLength: Int
        val type: ResourceType = when {
            filename.startsWith(ASSETS_PREFIX) -> {
                prefixLength = ASSETS_PREFIX.length
                ResourceType.CLIENT_RESOURCES
            }

            filename.startsWith(DATA_PREFIX) -> {
                prefixLength = DATA_PREFIX.length
                ResourceType.SERVER_DATA
            }

            else -> return false
        }

        val namespaceEnd: Int = filename.indexOf('/', prefixLength)
        if (namespaceEnd < 0) return false

        return !namespaces[type]?.contains(filename.substring(prefixLength, namespaceEnd))!!
    }

    private fun openFile(filename: String): InputSupplier<InputStream>? {
        val path = getPath(filename)

        if (path != null && Files.isRegularFile(path)) {
            return InputSupplier { Files.newInputStream(path) }
        }

        return null
    }

    private fun getFilename(type: ResourceType, id: Identifier): String {
        return String.format(Locale.ROOT, "%s/%s/%s", type.directory, id.namespace, id.path)
    }

    override fun openRoot(vararg pathSegments: String): InputSupplier<InputStream>? {
        PathUtil.validatePath(*pathSegments)

        return this.openFile(java.lang.String.join("/", *pathSegments))
    }

    override fun open(
        type: ResourceType,
        id: Identifier,
    ): InputSupplier<InputStream>? {
        if (!getNamespaces(type).contains(id.namespace)) {
            return null
        }

        Kitek.logger.info("Requested: ${id.namespace}:${id.path}")
        val path = getPath(getFilename(type, id))
        return path?.let { InputSupplier.create(it) }
    }

    override fun findResources(
        type: ResourceType,
        namespace: String,
        path: String,
        visitor: ResourcePack.ResultConsumer,
    ) {
        if (!namespaces.getOrDefault(type, Collections.emptySet()).contains(namespace)) {
            return
        }

        val basePath = this.contentPack.packPath

        val separator = basePath.fileSystem.separator
        val namespacePath = basePath.resolve(type.directory).resolve(namespace)
        val searchPath = namespacePath.resolve(path.replace("/", separator)).normalize()
        if (!searchPath.exists()) return

        Files.walkFileTree(searchPath, object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                val filename = namespacePath.relativize(file).toString().replace(separator, "/")
                val identifier = Identifier.tryParse(namespace, filename)

                if (identifier == null) {
                    Kitek.logger.error("Invalid path in content pack resource-pack $id: $namespace:$filename, ignoring")
                } else {
                    visitor.accept(identifier, InputSupplier.create(file))
                }

                return FileVisitResult.CONTINUE
            }
        })
    }

    override fun getNamespaces(type: ResourceType): Set<String> {
        return this.namespaces.getOrDefault(type, Collections.emptySet())
    }

    override fun <T: Any> parseMetadata(reader: ResourceMetadataReader<T>): T? {
        return AbstractFileResourcePack.parseMetadata(reader, getPackMeta())
    }

    override fun getInfo(): ResourcePackInfo {
        return this.contentPackResourcePackInfo
    }

    override fun close() {
    }
}