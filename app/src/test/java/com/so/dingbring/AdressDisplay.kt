package com.so.dingbring

import org.junit.Assert
import org.junit.Test

/**
 * Example local unit fragment_home.xml, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AdressDisplay {


    @Test
    fun adressDisplay() {

        var mEventAddress = ""
        var mStreetNumber = ""
        var mStreetName = ""
        var mCity = ""
        val mDisplay = "12 Rue des Fripouilles, Paris"

        val mAdressComp = mutableMapOf<String, String>()
        mAdressComp["street_number"] = "12"
        mAdressComp["route"] = "Rue des Fripouilles"
        mAdressComp["localityame"] = "Paris"

            mStreetNumber = mAdressComp["street_number"].toString()
            mStreetName =  mAdressComp["route"].toString()
        mCity = mAdressComp["localityame"].toString()

    mEventAddress = "$mStreetNumber $mStreetName, $mCity"


        Assert.assertEquals(mEventAddress, mDisplay )}
}