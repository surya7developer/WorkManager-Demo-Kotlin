package com.suresh.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters


class MyWorkManager(private val context: Context, private val workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        // This task will we execute when we enqueue

        val taskType = inputData.getString(MainActivity.TASK_TYPE)
        Log.d(MainActivity.LOG_TAG, "doWork: taskType = $taskType")

        if (taskType == MainActivity.ONE_TIME) {

            // This block will call when we hit one time work request
            context.showNotification(
                "One Time",
                "This is OneTime WorkManager testing notification"
            )

        } else {

            // This block will call when we hit periodic time work request in each specific interval
            context.showNotification(
                "Periodic",
                "This is Periodic WorkManager testing notification"
            )

        }

        return Result.success()
    }

    private fun Context.showNotification(
        type: String,
        message: String
    ) {
        createNotificationChannel()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_notification_one)
            .setContentTitle(type)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(1, builder.build())
        Log.d(MainActivity.LOG_TAG, "Notification fire")
    }

    private fun Context.createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "WorkManager Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager: NotificationManager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}