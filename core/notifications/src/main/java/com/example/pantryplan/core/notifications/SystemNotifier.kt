package com.example.pantryplan.core.notifications

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SystemNotifier @Inject constructor(
    @ApplicationContext private val context: Context,
) : Notifier {
}