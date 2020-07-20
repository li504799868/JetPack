package com.lzp.jetpack

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.lzp.jetpack.bean.User
import com.lzp.jetpack.livedata.SingleMutableLiveData

class MainViewModel(var counterReserved: Int) : ViewModel() {
//class MainViewModel() : ViewModel() {
//    var counter = 0

//    init {
//
//    }

    val counter: LiveData<Int>
        get() = _counter as LiveData<Int>

    private val _counter = SingleMutableLiveData<Int>()

    fun setValue(value: Int) {
        _counter.value = value
    }

    fun plus() {
        val now = counter.value ?: 0
        _counter.value = now + 1
    }

    fun clear() {
        _counter.value = 0
    }

    private val userLiveData = MutableLiveData<User>()

    // 使用map可以对User进行处理,监听某个属性的变化
    val userNameLiveData = Transformations.map(userLiveData) {
        it.name
    }

    fun randomUser() {
        val user = User((Math.random() * 1000).toString(), 1)
        userLiveData.value = user
    }

    fun getUser(userId: String): LiveData<User> {
        return Resposity.getUser(userId)
    }

    val userIdLiveData = MutableLiveData<String>()

    /**
     * 当userIdLiveData发生变化时，user也会跟着一起变化
     * */
    val user:LiveData<User> = Transformations.switchMap(userIdLiveData){
        Resposity.getUser(it)
    }

}