package com.example.attendanceapp

import org.junit.Assert.assertEquals
import org.junit.Test

class AttendanceUtilsTest {
    @Test
    fun calculateAttendance_zeroTotal_returnsZero() {
        val result = AttendanceUtils.calculateAttendance(0, 0)
        assertEquals(0.0, result, 0.0001)
    }

    @Test
    fun calculateAttendance_normalCase_returnsCorrectPercentage() {
        val result = AttendanceUtils.calculateAttendance(3, 1)
        assertEquals(75.0, result, 0.0001)
    }

    @Test
    fun calculateAttendance_allPresent_returns100() {
        val result = AttendanceUtils.calculateAttendance(10, 0)
        assertEquals(100.0, result, 0.0001)
    }
}