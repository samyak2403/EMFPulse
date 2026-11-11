package com.samyak.emfpulse.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class EmfModelTest {

    @Test
    fun testUnitConversion() {
        val microteslaVal = 15.5f
        val inMicrotesla = EmfUnit.MICROTESLA.convert(microteslaVal)
        val inMilligauss = EmfUnit.MILLIGAUSS.convert(microteslaVal)

        assertEquals(15.5f, inMicrotesla, 0.001f)
        assertEquals(155.0f, inMilligauss, 0.001f)
    }

    @Test
    fun testStatusThresholds() {
        assertEquals(EmfStatus.SAFE, EmfStatus.fromValue(0f))
        assertEquals(EmfStatus.SAFE, EmfStatus.fromValue(59.9f))
        assertEquals(EmfStatus.MODERATE, EmfStatus.fromValue(60f))
        assertEquals(EmfStatus.MODERATE, EmfStatus.fromValue(119.9f))
        assertEquals(EmfStatus.HIGH, EmfStatus.fromValue(120f))
        assertEquals(EmfStatus.HIGH, EmfStatus.fromValue(199.9f))
        assertEquals(EmfStatus.DANGER, EmfStatus.fromValue(200f))
        assertEquals(EmfStatus.DANGER, EmfStatus.fromValue(500f))
    }
}
