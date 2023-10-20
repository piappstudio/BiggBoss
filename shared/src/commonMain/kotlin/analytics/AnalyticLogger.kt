package analytics
 expect class AnalyticLogger() {
    fun logEvent(name: String, params: Map<String, Any>)
}