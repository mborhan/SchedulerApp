package com.test.schedulerapp.commonutils

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object TimeCalculation {

    fun calculateDelayInMinutes(targetHour: Int, targetMinute: Int): Long {
        val now = LocalDateTime.now()
        val todayTarget = now.toLocalDate().atTime(targetHour, targetMinute)

        // jodi target time already chole jae, next day nite hobe
        val finalTarget = if (todayTarget.isBefore(now)) {
            todayTarget.plusDays(1)
        } else {
            todayTarget
        }

        return ChronoUnit.MINUTES.between(now, finalTarget)
    }

    fun getCurrentTimeStamp(): Long {
        return System.currentTimeMillis()
    }

}