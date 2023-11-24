package analytics

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

actual class AnalyticLogger {
    actual fun logEvent(
        name: String,
        params: Map<String, Any>
    ) {
        val firebaseAnalytic = Firebase.analytics
        val bundle = Bundle().apply {
            params.forEach {
                this.putString(it.key, it.value.toString())
            }
        }
        firebaseAnalytic.logEvent(name, bundle)
    }
}