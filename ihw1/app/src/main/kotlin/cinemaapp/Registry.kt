package cinemaapp

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

const val REGISTRY_FILE_STORAGE_DIR = "./registry_info/"

abstract class Registry {
    abstract fun saveToFile()

    protected inline fun <reified SrType>serialize(fileName: String, map: SrType) {
        try {
            val dirPath: Path = Paths.get(REGISTRY_FILE_STORAGE_DIR)
            if (!dirPath.exists()) {
                Files.createDirectory(dirPath)
            }

            val filePath: Path = dirPath.resolve(fileName)
            filePath.writeText(Json.encodeToString<SrType>(map))
        } catch (ex: Exception) {
            println(ex.toString())
        }
    }

    protected inline fun <reified SrType>deserialize(fileName: String): SrType? {
        try {
            val dirPath: Path = Paths.get(REGISTRY_FILE_STORAGE_DIR)
            if (!dirPath.exists()) {
                Files.createDirectory(dirPath)
                return null
            }

            val filePath: Path = dirPath.resolve(fileName)
            if (!filePath.exists()) {
                return null
            }

            return Json.decodeFromString<SrType>(filePath.readText(Charsets.UTF_8))
        } catch (ex: Exception) {
            return null
        }
    }
}
