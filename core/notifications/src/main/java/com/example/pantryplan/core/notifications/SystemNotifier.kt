package com.example.pantryplan.core.notifications

import android.Manifest.permission
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pantryplan.core.datastore.UserPreferencesDataSource
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.DurationUnit

private const val PANTRY_EXPIRATION_NOTIFICATION_CHANNEL_ID = "pantry_item_expiration"
private const val PANTRY_EXPIRING_SOON_NOTIFICATION_CHANNEL_ID = "pantry_item_expiring_soon"

private const val PANTRY_EXPIRATION_NOTIFICATION_ID_PREFIX = "pantry.expiration."
private const val PANTRY_EXPIRING_SOON_NOTIFICATION_ID_PREFIX = "pantry.expiring_soon."

@Singleton
internal class SystemNotifier @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferenceSource: UserPreferencesDataSource
) : Notifier {
    override suspend fun scheduleNotificationsForPantryItems(
        items: List<PantryItem>
    ) = with(context) {
        if (!permissionsCheck()) {
            return
        }

        val preferences = userPreferenceSource.preferences.first()

        val alarmManager = this.getSystemService(AlarmManager::class.java)
        cancelNotificationsForPantryItems(items)

        for (item in items) {
            if (item.state == PantryItemState.FROZEN) continue

            val now = Clock.System.now()
            val expiringSoonDate = item.expiryDate.minus(preferences.expiringSoonAmount)

            if (now < item.expiryDate) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    item.expiryDate.toEpochMilliseconds(),
                    createPantryExpirationNotificationIntent(item)
                )
            }

            if (now < expiringSoonDate) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    expiringSoonDate.toEpochMilliseconds(),
                    createPantryExpiringSoonNotificationIntent(
                        item,
                        preferences.expiringSoonAmount.toInt(DurationUnit.DAYS)
                    )
                )
            }
        }
    }

    override fun cancelNotificationsForPantryItems(items: List<PantryItem>) = with(context) {
        val alarmManager = this.getSystemService(AlarmManager::class.java)

        for (item in items) {
            alarmManager.cancelNotification(
                PANTRY_EXPIRATION_NOTIFICATION_ID_PREFIX + item.id,
                context
            )
            alarmManager.cancelNotification(
                PANTRY_EXPIRING_SOON_NOTIFICATION_ID_PREFIX + item.id,
                context
            )
        }
    }
}

private fun AlarmManager.cancelNotification(
    id: String,
    context: Context
) {
    cancel(
        PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            Intent(context, Notification::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    )
}

private fun Context.createPantryExpirationNotificationIntent(
    item: PantryItem
): PendingIntent {
    val intent = Intent(this, Notification::class.java)
    val id = PANTRY_EXPIRATION_NOTIFICATION_ID_PREFIX + item.id

    ensurePantryExpirationNotificationChannelExists()

    intent.putExtra("ID", id)
    intent.putExtra("CHANNEL_ID", PANTRY_EXPIRATION_NOTIFICATION_CHANNEL_ID)
    intent.putExtra("PRIORITY", NotificationCompat.PRIORITY_DEFAULT)
    intent.putExtra(
        "TITLE",
        getString(R.string.core_notifications_pantry_expiration_title, item.name)
    )
    intent.putExtra("BODY", getString(R.string.core_notifications_pantry_expiration_body))

    return PendingIntent.getBroadcast(
        this,
        id.hashCode(),
        Intent(this, Notification::class.java),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
}

private fun Context.createPantryExpiringSoonNotificationIntent(
    item: PantryItem,
    expiringSoonAmount: Int
): PendingIntent {
    val intent = Intent(this, Notification::class.java)
    val id = PANTRY_EXPIRING_SOON_NOTIFICATION_ID_PREFIX + item.id

    ensurePantryExpiringSoonNotificationChannelExists()

    intent.putExtra("ID", id)
    intent.putExtra("CHANNEL_ID", PANTRY_EXPIRING_SOON_NOTIFICATION_CHANNEL_ID)
    intent.putExtra("PRIORITY", NotificationCompat.PRIORITY_DEFAULT)
    intent.putExtra("TITLE", getString(R.string.core_notifications_pantry_expiring_soon_title))
    intent.putExtra(
        "BODY",
        resources.getQuantityString(
            R.plurals.core_notifications_pantry_expiring_soon_body,
            expiringSoonAmount
        )
    )

    return PendingIntent.getBroadcast(
        this,
        id.hashCode(),
        Intent(this, Notification::class.java),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
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