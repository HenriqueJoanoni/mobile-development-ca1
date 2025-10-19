package com.example.scrollablelist

import com.example.scrollablelist.data.DataSource
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun dataSource_returns_nonEmptyList_and_uniqueIds() {
        val items = DataSource.getSampleItems()
        /** list should not be empty */
        assertTrue(items.isNotEmpty())

        /** ids should be unique */
        val ids = items.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }
}