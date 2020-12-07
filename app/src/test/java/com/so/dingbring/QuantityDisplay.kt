package com.so.dingbring

import com.google.android.gms.common.util.ArrayUtils
import kotlinx.android.synthetic.main.fragment_detail.*
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit fragment_home.xml, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class QuantityDisplay {
    @Test
    fun quantityDisplay() {
        val mResult = 2
        var mQty =  3
            if (mQty > 1){ mQty -= 1 }

        Assert.assertEquals(mQty, mResult)
    }
}