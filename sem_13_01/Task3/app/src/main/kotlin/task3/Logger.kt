package task3

import java.io.File
import java.io.FileOutputStream

import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.builtins.ListSerializer

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlinx.serialization.serializer
import java.io.FileInputStream
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.withLock

object Logger {
    private const val TEXT_FILENAME: String = "journal.log"
    private const val JSON_FILENAME: String = "journal.json"

    private val logsTextFile: File = File(TEXT_FILENAME)
    private val logsJsonFile: File = File(JSON_FILENAME)
    
    init {
        if (!logsTextFile.exists()) {
            logsTextFile.createNewFile()
        }
        if (!logsJsonFile.exists()) {
            logsJsonFile.createNewFile()
        }
    }
    
    private val writeLock = ReentrantReadWriteLock()

    @Serializable
    private data class Log(val severity: LogSeverity, val src: String, val timestamp: Instant, val description: String) {
        override fun toString(): String = "[$severity] [$timestamp] [source: $src] [desciption: $description]"
    }

    @ExperimentalSerializationApi
    fun log(severity: LogSeverity, src: String, timestamp: Instant, description: String) {
        val logInstance = Log(severity, src, timestamp, description)
        writeLock.writeLock().withLock {
            FileOutputStream(logsTextFile, true).use {
                it.write(logInstance.toString().toByteArray())
                it.write('\n'.code)
            }

            val inJsonSteam = FileInputStream(logsJsonFile)
            val jsonLogs: ArrayList<Log> = try {
                Json.decodeFromStream<ArrayList<Log>>(FileInputStream(logsJsonFile))
            } catch (ex: Exception) {
                ArrayList()
            } finally {
                inJsonSteam.close()
            }

            jsonLogs.add(logInstance)
            Json.encodeToStream(ListSerializer(serializer<Log>()), jsonLogs, FileOutputStream(logsJsonFile))
        }
    }
}
