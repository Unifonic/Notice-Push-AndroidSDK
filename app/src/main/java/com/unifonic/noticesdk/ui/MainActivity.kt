package com.unifonic.noticesdk.ui

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.crashlytics.internal.common.CommonUtils.hideKeyboard
import com.google.firebase.messaging.FirebaseMessaging
import com.unifonic.noticesdk.R
import com.unifonic.noticesdk.base.BaseActivity
import com.unifonic.noticesdk.base.BaseViewModel
import com.unifonic.noticesdk.di.ViewModelFactory
import com.unifonic.noticesdk.extensions.*
import com.unifonic.noticesdk.models.NotificationReadType
import com.unifonic.noticesdk.network.Status
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity() {

    companion object {
        const val NOTIFICATION_RECEIVED = "NOTIFICATION_RECEIVED"
        val NOTIFICATION_RECEIVED_INTENT_FILTER = IntentFilter(NOTIFICATION_RECEIVED)
        const val PAYLOAD = "payload"
    }

    override fun inject() {
        component.inject(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: DashBoardViewModel

    override fun getViewModel(): BaseViewModel? = viewModel

    override fun layoutId(): Int? = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[DashBoardViewModel::class.java]


//        init()

        registerReceiver(notificationReceivedBroadCast, NOTIFICATION_RECEIVED_INTENT_FILTER)

        btnRegister.setOnClickListener {
            ilAppId.error = ""
            ilIdentifier.error = ""

            if (etAppId.text.toString().isBlank()) {
                ilAppId.error = "App id is mandatory"
            } else if (etIdentifier.text.toString().isBlank()) {
                ilIdentifier.error = "Identifier is required"
            } else {
                viewModel.register(etAppId.text.toString(), etIdentifier.text.toString()).observe(
                    this,
                    {
                        when (it.status) {
                            Status.LOADING -> {
                                hideKeyboard(this, etIdentifier)
                                showLoading()
                            }
                            Status.SUCCESS -> {
                                hideLoading()
                                textView.append(it.data?.toString().plus("\n"))
                                init()
                            }
                            Status.ERROR -> {
                                hideLoading()
                                Toast.makeText(
                                    this,
                                    it.message ?: return@observe,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun init() {
        if (getSdkToken() != null) {
            btnRegister.isEnabled = false
            etAppId.setText(getAppId())
            etIdentifier.setText(getIdentifier())
            etFirebaseToken.setText(getFirebaseToken())
            etSdkKey.setText(getSdkToken())
            if (getFirebaseToken() == null) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener {
                    saveFirebaseToken(it.result)
                    createUpdateToken(true)
                }
            } else if (getOldFirebaseToken() != null && getOldFirebaseToken() != getFirebaseToken()) {
                createUpdateToken(false)
            } else if (isUserRegisteredWithFirebaseToken().not()) {
                createUpdateToken(true)
            } else if (intent.getStringExtra(PAYLOAD) != null) {
                savePushNotificationStatus(intent?.extras?.getString(PAYLOAD), false)
                intent.removeExtra(PAYLOAD)
            } else if (areNotificationsEnabled(NotificationManagerCompat.from(this)).not()) {
                if (isNotificationsDisableStatusNotified().not())
                    notifyNotificationDisabled(true)
            } else {
                if (isNotificationsDisableStatusNotified())
                    notifyNotificationDisabled(false)
            }
        }
    }

    private fun notifyNotificationDisabled(enabled: Boolean) {
        viewModel.disableNotification(enabled).observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    showLoading()
                }
                Status.SUCCESS -> {
                    hideLoading()
                    textView.append(it.data?.toString().plus("\n"))
                    Toast.makeText(
                        this,
                        "Successfully ${
                            if (enabled) {
                                notificationsDisableStatusNotified(true)
                                "unsubscribed"
                            } else {
                                notificationsDisableStatusNotified(false);
                                "subscribed"
                            }
                        }",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                Status.ERROR -> {
                    hideLoading()
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        init()
    }

    private fun createUpdateToken(create: Boolean = false) {
        viewModel.saveToken(create.not()).observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    showLoading()
                }
                Status.SUCCESS -> {
                    hideLoading()
                    textView.append(it.data?.toString().plus("\n"))
                    userRegisteredWithFirebaseToken()
                }
                Status.ERROR -> {
                    hideLoading()
                    showDialog(
                        R.string.error,
                        it.message ?: "Some error occurred",
                        "Retry!",
                        { init() },
                        cancelable = false
                    )
                }
            }
        })
    }

    private fun savePushNotificationStatus(messageId: String?, fromBroadCast: Boolean) {
        viewModel.markNotification(
            messageId ?: return Toast.makeText(
                this,
                "Invalid Message Id",
                Toast.LENGTH_LONG
            ).show(),
            if (fromBroadCast) NotificationReadType.RECEIVED else NotificationReadType.READ
        ).observe(this, {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    textView.append(it.data?.toString().plus("\n"))
                }
                Status.ERROR -> {
                    textView.append(it.message?.plus("\n"))

                }
            }
        })
    }

    private val notificationReceivedBroadCast = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            savePushNotificationStatus(intent?.getStringExtra(PAYLOAD), true)
        }
    }

    private fun areNotificationsEnabled(notificationManager: NotificationManagerCompat) = when {
        notificationManager.areNotificationsEnabled().not() -> false
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
            notificationManager.notificationChannels.firstOrNull { channel ->
                channel.importance == NotificationManager.IMPORTANCE_NONE
            } == null
        }
        else -> true
    }
}