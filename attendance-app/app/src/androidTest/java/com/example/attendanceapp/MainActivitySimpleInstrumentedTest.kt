package com.example.attendanceapp

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivitySimpleInstrumentedTest {

    @Test
    fun activity_launches_and_context_packageName() {
        ActivityScenario.launch(MainActivity::class.java).onActivity { activity ->
            assertNotNull(activity)
            assertEquals("com.example.attendanceapp", activity.applicationContext.packageName)
        }
    }
}