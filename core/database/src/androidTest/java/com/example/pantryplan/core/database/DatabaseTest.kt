package com.example.pantryplan.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.pantryplan.core.database.dao.PantryDao
import org.junit.After
import org.junit.Before

internal abstract class DatabaseTest {

    private lateinit var db: PantryPlanDatabase
    protected lateinit var pantryDao: PantryDao

    @Before
    fun setup() {
        db = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(
                context,
                PantryPlanDatabase::class.java,
            ).build()
        }
        pantryDao = db.pantryDao()
    }

    @After
    fun teardown() = db.close()
}