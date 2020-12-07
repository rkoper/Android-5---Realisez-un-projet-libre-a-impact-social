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
class UpdateStatusItem {
    @Test
    fun updateStatusItem() {

        var mFireResponse = "I need"
        var mFireSend = "I bring"
        var mFireNewValue = "I bring"
        var mIdItem = "XXX-XXX-XXX"


        var mData = HashMap<Int, String>()
        mData[0] = "I bring"

       if (mData.containsKey(0)) {
           mFireSend = mData[0].toString()

            if (mFireSend == "I bring" )
                {mFireNewValue = "I bring"}
            else {mFireNewValue =  "I need"}}

        Assert.assertEquals(mFireNewValue, mFireSend)
    }
}