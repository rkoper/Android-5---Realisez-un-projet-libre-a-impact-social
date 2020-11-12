package com.so.dingbring.data

data class MyUser(
    val mNameUser:String,
    val mEmailUser:String,
    val mPhotoUser:String,
    val mUserId:String,
    val mEventUser:ArrayList<String>,
    val mNbEvent: Long?
)