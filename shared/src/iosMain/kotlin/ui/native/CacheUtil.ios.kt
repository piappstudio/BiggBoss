package ui.native

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.URLByAppendingPathComponent
import platform.Foundation.create
import platform.Foundation.writeToURL

@OptIn(ExperimentalForeignApi::class)
actual fun loadFromCache(fileName: String): String? {
    val cacheDirectory = NSFileManager.defaultManager.URLsForDirectory(
        NSCachesDirectory, NSUserDomainMask
    ).firstOrNull() as? NSURL

    val fileURL = cacheDirectory?.URLByAppendingPathComponent(fileName)

    return fileURL?.let {
        val content = NSString.create(contentsOfURL = it, encoding = NSUTF8StringEncoding, error = null)
        content?.toString()
    }
}
@OptIn(ExperimentalForeignApi::class)
actual fun writeToCache(content:String, fileName: String):Boolean {
    val cacheDirectory = NSFileManager.defaultManager.URLsForDirectory(
        NSCachesDirectory, NSUserDomainMask
    ).firstOrNull() as? NSURL

    val fileURL = cacheDirectory?.URLByAppendingPathComponent(fileName)

    return fileURL?.let {
        val jsonContent = NSString.create(string =content)
        jsonContent.writeToURL(it, atomically = true, encoding = NSUTF8StringEncoding, error = null)
        true
    } ?: false
}