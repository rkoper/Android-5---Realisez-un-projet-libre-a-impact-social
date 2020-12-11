package com.so.dingbring

import org.junit.Assert
import org.junit.Test

/**
 * Example local unit fragment_home.xml, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FacebookImgTest {
    @Test
    fun facebookImgTest() {


        val mSaveImg = "https://graph.facebook.com/10157807810668790/picture?access_token=EAAxeCj8bVTwBAGlqLkK2wIrG9ItqRWcaRlhqiXA7h4b9aonNPasZCbwKQKU5cvHZCFPb3XNQ4UJv5KsoahrsZAapQg8Geyuifb1QjunR6F4GTZCPRQgYrJVfA7ZAvGuWhxE4CmUZA4NXj0iITerPH4PZAcim8tPWTHjrOZBZCJclQwNQR6bKuNw7DIvGpY1h3Tqj3ITfVg4UqMPfACPSuhYmt34TIKK1UZCeVMQYC6wzjCIQZDZD"
        val mPhotoUser = "https://graph.facebook.com/10157807810668790/picture"
        var mImageUrl = ""
        val mPhotoUserSplit1 = mPhotoUser.split("//")
        val mPhotoUserSplit2 = mPhotoUserSplit1[1].split(".com/")
        val mPhotoUserSplit3 = mPhotoUserSplit2[0]
        mImageUrl = if (mPhotoUserSplit3 == "graph.facebook") {
            val token = "EAAxeCj8bVTwBAGlqLkK2wIrG9ItqRWcaRlhqiXA7h4b9aonNPasZCbwKQKU5cvHZCFPb3XNQ4UJv5KsoahrsZAapQg8Geyuifb1QjunR6F4GTZCPRQgYrJVfA7ZAvGuWhxE4CmUZA4NXj0iITerPH4PZAcim8tPWTHjrOZBZCJclQwNQR6bKuNw7DIvGpY1h3Tqj3ITfVg4UqMPfACPSuhYmt34TIKK1UZCeVMQYC6wzjCIQZDZD"
            "$mPhotoUser?access_token=$token"
        } else {
            mPhotoUser
        }


        Assert.assertEquals(mSaveImg, mImageUrl)
    }


}