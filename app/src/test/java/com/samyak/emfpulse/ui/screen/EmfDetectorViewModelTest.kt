package com.samyak.emfpulse.ui.screen

import android.app.Application
import org.junit.Assert.assertNotNull
import org.junit.Test

class EmfDetectorViewModelTest {

    @Test
    fun testApplicationConstructorExistsForReflectionFactory() {
        val constructor = EmfDetectorViewModel::class.java.getConstructor(Application::class.java)
        assertNotNull("Constructor EmfDetectorViewModel(Application) must exist for AndroidViewModelFactory", constructor)
    }
}

