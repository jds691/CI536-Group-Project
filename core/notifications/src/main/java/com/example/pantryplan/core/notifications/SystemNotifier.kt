package com.example.pantryplan.core.notifications

import android.Manifest.permission
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pantryplan.core.models.PantryItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val PANTRY_EXPIRATION_NOTIFICATION_CHANNEL_ID = "pantry_item_expiration"
private const val PANTRY_EXPIRING_SOON_NOTIFICATION_CHANNEL_ID = "pantry_item_expiring_soon"

private const val PANTRY_EXPIRATION_NOTIFICATION_ID_PREFIX = "pantry.expiration."
private const val PANTRY_EXPIRING_SOON_NOTIFICATION_ID_PREFIX = "pantry.expiring_soon."

@Singleton
internal class SystemNotifier @Inject constructor(
    @ApplicationContext private val context: Context,
) : Notifier {
    override fun scheduleNotificationsForPantryItems(
        items: List<PantryItem>
    ) = with(context) {
        if (!permissionsCheck()) {
            return
        }
    }
}

private fun Context.createPantryExpirationNotificationIntent(
    item: PantryItem
): Intent {
    val intent = Intent(this, Notification::class.java)

    ensurePantryExpirationNotificationChannelExists()

    intent.putExtra("ID", PANTRY_EXPIRATION_NOTIFICATION_ID_PREFIX + item.id)
    intent.putExtra("CHANNEL_ID", PANTRY_EXPIRATION_NOTIFICATION_CHANNEL_ID)
    intent.putExtra("PRIORITY", NotificationCompat.PRIORITY_DEFAULT)
    intent.putExtra(
        "TITLE",
        getString(R.string.core_notifications_pantry_expiration_title, item.name)
    )
    intent.putExtra("BODY", getString(R.string.core_notifications_pantry_expiration_body))

    return intent
}

private fun Context.createPantryExpiringSoonNotificationIntent(
    item: PantryItem
): Intent {
    val intent = Intent(this, Notification::class.java)

    ensurePantryExpiringSoonNotificationChannelExists()

    intent.putExtra("ID", PANTRY_EXPIRING_SOON_NOTIFICATION_ID_PREFIX + item.id)
    intent.putExtra("CHANNEL_ID", PANTRY_EXPIRING_SOON_NOTIFICATION_CHANNEL_ID)
    intent.putExtra("PRIORITY", NotificationCompat.PRIORITY_DEFAULT)
    intent.putExtra("TITLE", getString(R.string.core_notifications_pantry_expiring_soon_title))
    // TODO: Load days from UserPreferencesRepository
    intent.putExtra(
        "BODY",
        resources.getQuantityString(R.plurals.core_notifications_pantry_expiring_soon_body, 2)
    )

    return intent
}

// MARK: Channel and group registration

private fun Context.ensurePantryExpirationNotificationChannelExists() {
    val channel = NotificationChannel(
        PANTRY_EXPIRATION_NOTIFICATION_CHANNEL_ID,
        getString(R.string.core_notifications_pantry_expiration_notification_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT,
    ).apply {
        description =
            getString(R.string.core_notifications_pantry_expiration_notification_channel_description)
    }
    // Register the channel with the system
    NotificationManagerCompat.from(this).createNotificationChannel(channel)
}

private fun Context.ensurePantryExpiringSoonNotificationChannelExists() {
    val channel = NotificationChannel(
        PANTRY_EXPIRING_SOON_NOTIFICATION_CHANNEL_ID,
        getString(R.string.core_notifications_pantry_expiring_soon_notification_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT,
    ).apply {
        description =
            getString(R.string.core_notifications_pantry_expiring_soon_notification_channel_description)
    }
    // Register the channel with the system
    NotificationManagerCompat.from(this).createNotificationChannel(channel)
}

private fun Context.permissionsCheck(): Boolean {
    if (ActivityCompat.checkSelfPermission(
            this,
            permission.POST_NOTIFICATIONS
        ) != PERMISSION_GRANTED
    ) {
        return false
    }

    if (
    // Permission we need is different at API level 33+ but is implicitly granted by the system
        VERSION.SDK_INT >= VERSION_CODES.S && VERSION.SDK_INT < VERSION_CODES.TIRAMISU &&
        ActivityCompat.checkSelfPermission(
            this,
            permission.SCHEDULE_EXACT_ALARM
        ) != PERMISSION_GRANTED
    ) {
        return false
    }

    return true
}