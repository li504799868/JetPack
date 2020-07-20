package com.lzp.jetpack

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lzp.jetpack.bean.User

object Resposity {

    fun getUser(userId: String): LiveData<User>{
        val liveData = MutableLiveData<User>()
        liveData.value = User(userId, 1)
        return liveData
    }
}