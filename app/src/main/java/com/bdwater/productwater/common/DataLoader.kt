package com.bdwater.productwater.common

/**
 * Created by hegang on 2018/8/16.
 */
interface DataLoader {
    fun load()
    fun onLoad()
    fun loading()
    fun onFinally()
    fun onError(message: String?)
}