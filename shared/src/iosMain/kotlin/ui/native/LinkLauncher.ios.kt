package ui.native

// In the iOS module
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
actual class LinkLauncher  {
    actual fun openLink(url: String) {
        val nsURL = NSURL(string = url)
        val application = UIApplication.sharedApplication
        if (application.canOpenURL(nsURL)) {
            application.openURL(nsURL)
        }
    }
    actual fun dialNumber(phoneNumber: String) {
        val nsURL = NSURL(string = "tel:$phoneNumber")
        val application = UIApplication.sharedApplication
        if (application.canOpenURL(nsURL)) {
            application.openURL(nsURL)
        }
    }
}