package com.lzp.jetpack

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.*
import com.lzp.jetpack.dsl.main
import com.lzp.jetpack.lifecycle.MyObserve
import com.lzp.jetpack.work.SimpleWorker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // 带有构造参数的ViewModel
        sp = getPreferences(Context.MODE_PRIVATE)
        val counter = sp.getInt("counter", 0)

        viewModel = ViewModelProviders.of(this, MainViewModelFactory(counter))
            .get(MainViewModel::class.java)
        viewModel.setValue(0)
//        refreshCounter()
        plus.setOnClickListener {
            viewModel.plus()
        }

        clear.setOnClickListener {
            viewModel.clear()
        }

        register.setOnClickListener {
            viewModel.counter.observe(this, observer)
        }

        unregister.setOnClickListener {
            viewModel.counter.removeObserver(observer)
        }

        randomUser.setOnClickListener {
            viewModel.randomUser()
        }

        doWorkBtn.setOnClickListener {
            val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java)
                .setInitialDelay(3, TimeUnit.MINUTES) // 设置任务延迟3分钟开始执行
                .addTag("Simple")  // 为任务添加标记位，方便统一取消
                .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.MINUTES) // 表示任务失败后，会以延迟的方式执行
                .build()

            // 周期任务，循环周期不可小于15分钟
//             val request2 = PeriodicWorkRequest.Builder(SimpleWorker::class.java, 15, TimeUnit.MINUTES).build()

            // 创建链式任务, 如果在前面的人任务失败，之后的任务不会得到在执行
            WorkManager.getInstance(this)
                .beginWith(request)
                .then(request)
                .then(request).enqueue()

            WorkManager.getInstance(this).enqueue(request)

            WorkManager.getInstance(this).enqueueUniqueWork("test1", ExistingWorkPolicy.APPEND, request)

            // 取消任务的方式
            WorkManager.getInstance(this).cancelAllWork()
            WorkManager.getInstance(this).cancelAllWorkByTag("Simple")
            WorkManager.getInstance(this).cancelWorkById(request.id)
            WorkManager.getInstance(this).cancelUniqueWork("test1")

            // 监听任务的执行状态
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.id)
                .observe(this, Observer {
                    when (it.state) {
                        WorkInfo.State.SUCCEEDED -> {
                        }
                        WorkInfo.State.FAILED -> {
                        }
                        else -> {
                        }
                    }
                })

        }

        dsl.setOnClickListener {
            main()
        }

        lifecycle.addObserver(MyObserve())

        viewModel.userNameLiveData.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private val observer = Observer<Int?> {
        refreshCounter()
    }


    private fun refreshCounter() {
        text.text = viewModel.counter.value.toString()
        sp.edit().putInt("counter", viewModel.counter.value ?: 0).apply()
    }
}
