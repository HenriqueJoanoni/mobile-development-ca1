package com.example.attendanceapp

object AttendanceUtils {
    fun calculateAttendance(present: Int, absent: Int): Double {
        val total = present + absent
        return if (total > 0) (present.toDouble() / total) * 100 else 0.0
    }
}