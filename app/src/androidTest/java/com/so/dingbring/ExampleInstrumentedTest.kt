package com.so.dingbring

import androidx.test.platform.app.InstrumentationRegistry

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented fragment_home.xml, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under fragment_home.xml.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.so.dingbring", appContext.packageName)
    }
}