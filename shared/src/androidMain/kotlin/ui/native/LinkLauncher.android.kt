package ui.native

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
object PiShared {
    var applicationContext: android.content.Context? = null

    public fun setContext(context: android.content.Context) {
        applicationContext = context.applicationContext
    }
}
actual class LinkLauncher {
    actual fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        try {
            PiShared.applicationContext?.let {
                it.startActivity(intent)
            }
        }catch (ex:Exception) {

        }

    }
    actual fun dialNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        try {
            PiShared.applicationContext?.let {
                it.startActivity(intent)
            }

        } catch (ex:Exception) {

        }

    }
}