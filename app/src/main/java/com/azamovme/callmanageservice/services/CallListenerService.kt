package com.azamovme.callmanageservice.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.azamovme.callmanageservice.R
import com.topjohnwu.superuser.Shell
import java.io.BufferedReader
import java.io.InputStreamReader

class CallListenerService : Service() {

    private lateinit var telephonyManager: TelephonyManager
    private lateinit var audioManager: AudioManager
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()

        // Notification Channel yaratamiz
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "CALL_LISTENER_CHANNEL",
                "Call Listener Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }

        // Notificationni yaratamiz
        val notification = NotificationCompat.Builder(this, "CALL_LISTENER_CHANNEL")
            .setContentTitle("Call Listener")
            .setContentText("Listening for ongoing calls...")
            .setSmallIcon(R.drawable.ic_clock)
            .build()

        // Foreground Service sifatida ishga tushiramiz
        startForeground(1, notification)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val callStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                when (state) {
                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                        // Qo'ng'iroq qabul qilindi
                        Log.d("CallListener", "TELEFONDA GAPLASHILYAPTI")
                        playMusicWithRoot()
                        playMedia()
                    }
                    TelephonyManager.CALL_STATE_RINGING -> {
                        // Qo'ng'iroq qabul qilindi
                        Log.d("CallListener", "TELEFONDA GAPLASHILYAPTI")
                        playMusicWithRoot()
                        playMedia()
                    }

                    TelephonyManager.CALL_STATE_IDLE -> {
                        // Qo'ng'iroq tugadi yoki rad etildi
                        stopMedia()
                    }
                }
            }
        }
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun playMedia() {
        mediaPlayer = MediaPlayer.create(this, R.raw.start_call)
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_VOICE_CALL)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    private fun stopMedia() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    // Audio yo'naltirish
    private fun playMusicWithRoot() {
        if (Shell.rootAccess()) {
            Shell.cmd("tinymix 'Voice Call' 'Music'").exec()
        } else {
            Log.e("Root Access", "Root kirish huquqi yo'q")
        }
    }

    // Root tekshirish
    private fun isDeviceRooted(): Boolean {
        val buildTags = Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys")) return true
        try {
            val process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val input = BufferedReader(InputStreamReader(process.inputStream))
            if (input.readLine() != null) return true
        } catch (e: Exception) {
            return false
        }
        return false
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
