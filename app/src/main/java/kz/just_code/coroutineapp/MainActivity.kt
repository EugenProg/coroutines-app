package kz.just_code.coroutineapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.just_code.coroutineapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val jobList: MutableList<Job> = mutableListOf()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private val newScoupe = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val deferred = coroutineScope.async {
            calculate(5)
        }

        binding.stop.setOnClickListener {
            /*jobList.forEach {
                it.cancel()
            }*/
            newScoupe.launch {
                val result = deferred.await()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "result is: $result", Toast.LENGTH_SHORT).show()
                }
            }
        }

        /*coroutineScope.launch {
            run()
        }*/
    }

    private suspend fun calculate(count: Int): Int {
        var result = 0
        for (number in 0..count) {
            result += number
            delay(1000)
        }

        return result
    }

    private suspend fun run() = coroutineScope {
        val job = launch(Dispatchers.IO, start = CoroutineStart.LAZY) {
            for (index in 0..3) {
                Log.e(">>>", "Index: $index")
                delay(1200)
            }
        }
        job.start()
        jobList.add(job)

        Log.e(">>>", "Start")
        job.join()
        Log.e(">>>", "End")
    }
}

/*
class Coroutines {
    var valuesCount = 8
    var value = 0

    init {
        run()
    }

    fun run() {
        when(value) {
            0 -> {
                value++
                Log.e(">>>", "Index: 0")
                run()
            }
            1 -> {
                value++
                delay(1200)
                run()
            }
            2 -> {
                value++
                Log.e(">>>", "Index: 1")
                run()
            }
            3 -> {
                value++
                delay(1200)
                run()
            }
            4 -> {
                value++
                Log.e(">>>", "Index: 2")
            }
        }
    }
}*/
