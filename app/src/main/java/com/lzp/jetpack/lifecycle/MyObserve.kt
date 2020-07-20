package com.lzp.jetpack.lifecycle

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MyObserve: LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun activityCreate(){
        Log.e("lzp", "activityCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun activityStart(){
        Log.e("lzp", "activityStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun activityStop(){
        Log.e("lzp", "activityStop")
    }
}