package ui.native

expect fun writeToCache(content:String, fileName: String):Boolean
expect fun loadFromCache(fileName:String):String?