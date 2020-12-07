package com.so.dingbring

import android.widget.Toast
import com.google.android.gms.common.util.ArrayUtils
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit fragment_home.xml, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DynamicLink {
    @Test
    fun dynamicLink() {
        var pendingDynamicLinkData = "https://dingbring.page.link/?apn=com.so.dingbring&link=https%3A%2F%2Fdingbring.page.link%2Fb8a2b31c-4f56-4c9b-9947-fcb07e0b61e7"
        val mResult = "2Fb8a2b31c-4f56-4c9b-9947-fcb07e0b61e7"

        val deepLink = pendingDynamicLinkData
        val mDeepId =  deepLink.split("apn=com.so.dingbring&link=https%3A%2F%2Fdingbring.page.link%")[1]

        Assert.assertEquals(mDeepId, mResult)
    }
}