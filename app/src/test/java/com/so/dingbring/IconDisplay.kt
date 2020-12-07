package com.so.dingbring

import com.google.android.gms.common.util.ArrayUtils
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit fragment_home.xml, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class IconDisplay {
    @Test
    fun iconDisplay() {
        var mNbUser = 12
        var mProfilStatus = ""
        val mResult = "intermediate"


        if (mNbUser in 0..4) {
            mProfilStatus = "newbie"
        }
        if (mNbUser in 5..9) {
            mProfilStatus = "beginner"
        }
        if (mNbUser in 10..14) {
            mProfilStatus = "intermediate"
        }
        if (mNbUser in 15..19) {
            mProfilStatus = "experienced"
        }
        if (mNbUser in 20..24) {
            mProfilStatus = "advanced"
        }
        if (mNbUser > 24) {
            mProfilStatus = "expert"
        }

        Assert.assertEquals(mProfilStatus, mResult)
    }
}