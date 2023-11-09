package ui.native

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader


actual fun loadFromCache(fileName: String): String? {
    val returnString = StringBuilder()
    var inStream: InputStreamReader? = null
    var inputBuffer: BufferedReader? = null
    try {
        val stream =  File(PiShared.applicationContext?.cacheDir, fileName).inputStream()
       inStream = InputStreamReader(stream)

        inputBuffer = BufferedReader(inStream)
        do {
            val line = inputBuffer.readLine()
            if (line!=null) {
                returnString.append(line)
            }
        } while (line!=null)
    } catch (exception:Exception) {
        return null
    }
    finally {
        inStream?.close()
        inputBuffer?.close()
    }
    return returnString.toString()

}

actual fun writeToCache(content: String, fileName: String): Boolean {
    try {
        val file = File(PiShared.applicationContext?.cacheDir, fileName)
        val fileWrite = FileWriter(file.absoluteFile)
        val bufferWriter = BufferedWriter(fileWrite)
        bufferWriter.write(content)
        bufferWriter.close()
        return true
    } catch (ex:Exception) {
        return false
    }
}