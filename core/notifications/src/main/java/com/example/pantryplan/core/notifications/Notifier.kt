package com.example.pantryplan.core.notifications

import com.example.pantryplan.core.models.PantryItem

interface Notifier {
    fun scheduleNotificationsForPantryItems(items: List<PantryItem>)
    fun cancelNotificationsForPantryItems(items: List<PantryItem>)
}