package com.unifonic.noticesdk

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.multidex.MultiDexApplication
import com.unifonic.noticesdk.di.components.AppComponent
import com.unifonic.noticesdk.di.components.DaggerAppComponent
import com.unifonic.noticesdk.di.modules.AppModule


class AndroidApp : MultiDexApplication() {
    init {
        instance = this
    }

    val component: AppComponent by lazy {

        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }


    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if ((getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).getNotificationChannel(
                    "unifonic_noti"
                ) == null
            )
                createNotificationChannel()
        }

    }

    companion object {
        lateinit var instance: AndroidApp
        fun context(): Context {
            return instance.applicationContext
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(): NotificationChannel {
//        val sound: Uri =
//            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/" + R.raw.neworder) //Here is FILE_NAME is the name of file that you want to play

        val name: CharSequence = "Unifonic"
        val description = "Unifonic push updates"
        val importance =
            NotificationManager.IMPORTANCE_DEFAULT
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        val channel = NotificationChannel("unifonic_noti", name, importance)
        channel.description = description
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.setSound(
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
            audioAttributes
        )
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
            channel
        )
        return channel
    }

}