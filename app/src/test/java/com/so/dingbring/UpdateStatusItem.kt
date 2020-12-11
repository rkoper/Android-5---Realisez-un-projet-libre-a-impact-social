package com.so.dingbring

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


        val mData = HashMap<Int, String>()
        mData[0] = "I bring"

       if (mData.containsKey(0)) {
           mFireSend = mData[0].toString()

           mFireNewValue = if (mFireSend == "I bring" ) {
               "I bring"
           } else {
               "I need"
           }
       }

        Assert.assertEquals(mFireNewValue, mFireSend)
    }
}