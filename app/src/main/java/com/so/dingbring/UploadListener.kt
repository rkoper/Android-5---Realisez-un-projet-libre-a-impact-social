package com.so.dingbring

interface UploadListener {

    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}