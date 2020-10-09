package com.magician.notificationdemo

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val channelId = "com.magician.notificationdemo.channel1"
    private var notificationManager: NotificationManager? = null
    private val REPLY_KEY = "reply_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelId, "DemoChannel", "This is Demo Project")


        button.setOnClickListener {
            displayNotification()
        }
    }

    private fun displayNotification() {

        val notificationId = 45
        val tapResultIntent = Intent(this, SecondActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            tapResultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //reply action
        val remoteInput = RemoteInput.Builder(REPLY_KEY).run {
            setLabel("Insert Your Name Here")
            build()
        }

        val replyAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
            0,
            "Reply",
            pendingIntent,
        ).addRemoteInput(remoteInput)
            .build()


        val detailsIntent = Intent(this, DetailActivity::class.java)
        val pendingDetailIntent = PendingIntent.getActivity(
            this,
            0,
            detailsIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val detailAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
            R.drawable.ic_detail,
            "Details",
            pendingDetailIntent
        ).build()

        val settingIntent = Intent(this, SettingActivity::class.java)
        val pendingSettingIntent = PendingIntent.getActivity(
            this,
            0,
            settingIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val settingAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
            R.drawable.ic_settings,
            "Settings",
            pendingSettingIntent
        ).build()

        val notification = NotificationCompat.Builder(this@MainActivity, channelId)
            .setContentTitle("Demo Title")
            .setContentText("This is a Demo Notification")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(replyAction)
            .addAction(detailAction)
            .addAction(settingAction)
            .build()
        notificationManager?.notify(notificationId, notification)
    }

    private fun createNotificationChannel(id: String, name: String, channelDescription: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance: Int = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}