package com.suresh.workmanager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.suresh.workmanager.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val workManager = WorkManager.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            oneTimeWorkRequest.setOnClickListener {
                oneTimeWorkRequest()
            }

            periodicWorkRequest.setOnClickListener {
                periodicWorkRequest()
            }

            cancelWorkRequest.setOnClickListener {
                workManager.cancelAllWorkByTag(REQUEST_TAG)
                Log.d(MainActivity.LOG_TAG, "Cancel both work request")
            }
        }
    }


    private fun periodicWorkRequest() {

        // This is optional i just used it to differentiate OneTime and Periodic task in worker class
        val inputData = Data.Builder()
            .putString(TASK_TYPE, PERIODIC)
            .build()


        val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorkManager::class.java, 15, TimeUnit.MINUTES) // 15 Min is minimum  time interval for periodic work request
                .setConstraints(constraint) // we can include more constraint
                .setInputData(inputData)    // Its identify our two request in Worker class
                .setInitialDelay(2000, TimeUnit.MILLISECONDS) // This initialDelay is specify for our first work request when execute
                .addTag(REQUEST_TAG) // Tag to cancel work request
                .build()

        workManager.enqueue(periodicWorkRequest)
    }

    private fun oneTimeWorkRequest() {

        // This is optional i just used it to differentiate OneTime and Periodic task in worker class
        val inputData = Data.Builder()
            .putString(TASK_TYPE, ONE_TIME)
            .build()


        val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val oneTimeRequest = OneTimeWorkRequest.Builder(MyWorkManager::class.java)
            .setConstraints(constraint) // we can include more constraint
            .setInputData(inputData)    // Its identify our two request in Worker class
            .setInitialDelay(2000, TimeUnit.MILLISECONDS) // This initialDelay is specify for our first work request when execute
            .addTag(REQUEST_TAG) // Tag to cancel work request
            .build()

        workManager.enqueue(oneTimeRequest)
    }

    companion object {
        const val TASK_TYPE = "task_type"
        const val ONE_TIME = "oneTime"
        const val PERIODIC = "periodic"
        const val REQUEST_TAG = "Request"
        const val LOG_TAG = "MainActivityLogTag"
    }
}