package com.ipca.utils

import kotlinx.datetime.*
import java.time.LocalDate as JLocalDate

fun JLocalDate.toKotlinx(): LocalDate = LocalDate(this.year, this.monthValue, this.dayOfMonth)
fun LocalDate.toJava(): JLocalDate = JLocalDate.of(this.year, this.monthNumber, this.dayOfMonth)

fun todayKotlinx(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

// Convenience extensions for LocalDate arithmetic using java.time under the hood
fun LocalDate.plusDays(days: Int): LocalDate = this.toJava().plusDays(days.toLong()).toKotlinx()
fun LocalDate.minusDays(days: Int): LocalDate = this.toJava().minusDays(days.toLong()).toKotlinx()
fun LocalDate.plusMonths(months: Int): LocalDate = this.toJava().plusMonths(months.toLong()).toKotlinx()
fun LocalDate.minusMonths(months: Int): LocalDate = this.toJava().minusMonths(months.toLong()).toKotlinx()

// Also provide overloads accepting DatePeriod to mimic usage
fun LocalDate.plus(period: DatePeriod): LocalDate = when {
    period.months != 0 -> this.plusMonths(period.months)
    period.days != 0 -> this.plusDays(period.days)
    else -> this
}

fun LocalDate.minus(period: DatePeriod): LocalDate = when {
    period.months != 0 -> this.minusMonths(period.months)
    period.days != 0 -> this.minusDays(period.days)
    else -> this
}
