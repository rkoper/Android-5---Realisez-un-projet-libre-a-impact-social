package com.so.dingbring

import com.google.android.gms.common.util.ArrayUtils
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit fragment_home.xml, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DateAdapter {

    val mResult = "20200101"
    val data = arrayListOf("Test", "01/01/2020", "Test2", "Test3")
    @Test
    fun dataAdapter() {
    val newDate = data[1].split("/")
    val d = newDate[0]
    val m = newDate[1]
    val y = newDate[2]
    val mCompareDate = y+m+d
        Assert.assertEquals(mCompareDate, mResult)}
}