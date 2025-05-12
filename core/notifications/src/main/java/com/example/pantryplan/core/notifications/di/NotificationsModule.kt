package com.example.pantryplan.core.notifications.di

import com.example.pantryplan.core.notifications.Notifier
import com.example.pantryplan.core.notifications.SystemNotifier
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationsModule {
    @Binds
    internal abstract fun bindsNotifier(
        notifier: SystemNotifier,
    ): Notifier
}