package com.fmartinier.killerparty

import android.content.Context
import com.fmartinier.killerparty.services.SmsService
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SmsServiceTest {
    private val context: Context = Mockito.mock()
    private val smsService = SmsService(context)

    @Test
    fun isValidPhoneNumber_test() {
        assertTrue(smsService.isValidPhoneNumber("0600000000"))
        assertTrue(smsService.isValidPhoneNumber("06 00 00 00 00"))
        assertTrue(smsService.isValidPhoneNumber("06-00-00-00-00"))
        assertTrue(smsService.isValidPhoneNumber("+33 6 00 00 00 00"))
        assertTrue(smsService.isValidPhoneNumber("+33600000000"))

        assertFalse(smsService.isValidPhoneNumber("+3360000000"))
        assertFalse(smsService.isValidPhoneNumber("060000000"))
        assertFalse(smsService.isValidPhoneNumber("6600000000"))
        assertFalse(smsService.isValidPhoneNumber("060000000000"))
    }

    @Test
    fun formatPhoneNumber_test() {
        val targetNumber = "0600000000"

        assertEquals(targetNumber, smsService.formatPhoneNumber("0600000000"))
        assertEquals(targetNumber, smsService.formatPhoneNumber("06 00 00 00 00"))
        assertEquals(targetNumber, smsService.formatPhoneNumber("+33 6 00 00 00 00"))
        assertEquals(targetNumber, smsService.formatPhoneNumber("+336 00 00 00 00"))
    }
}