package com.lzp.jetpack.livedata

import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleMutableLiveData<T> : MutableLiveData<T?>() {

    private val mPending: AtomicBoolean = AtomicBoolean(false)

    private val mObserverMap = hashMapOf<Observer<in T?>, Observer<T?>>()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        val delegateObserver = Observer { t: T? ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
        mObserverMap[observer] = delegateObserver
        super.observe(owner, delegateObserver)
    }

    @MainThread
    override fun setValue(@Nullable t: T?) {
        if (hasActiveObservers()) {
            mPending.set(true)
        }
        super.setValue(t)
    }

    override fun removeObserver(observer: Observer<in T?>) {
        mObserverMap.remove(observer)
        super.removeObserver(observer)
    }

}