package com.johnny.meet_kotlin.rongcloud.networklib

import retrofit2.http.GET

/**
 * @author Johnny
 */
interface RongCloudApi {

    @GET("")
    fun getToken()
}