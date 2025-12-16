//package com.ipca.unit
//
//import java.time.LocalDate
//import kotlin.test.Test
//import kotlin.test.assertEquals
//
//class GoodStatusTest {
//    @Test
//    fun `expired when validity date is in the past`() {
//        val today = LocalDate.now()
//        val validity = today.minusDays(1)
//
//        val status = GoodStatusCalculator.calculate(validity)
//
//        assertEquals(GoodStatus.EXPIRED, status)
//    }
//}