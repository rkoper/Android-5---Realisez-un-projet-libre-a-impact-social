package com.so.dingbring

import com.google.android.gms.common.util.ArrayUtils
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit fragment_home.xml, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DateUtil {
    @Test
    fun dateUtil() {
        val test = Utils.formatDate(2020,0,1)
        val result  =  "01/01/2020"
        Assert.assertEquals(result, test)
    }
}