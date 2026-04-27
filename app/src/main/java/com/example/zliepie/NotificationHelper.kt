package com.example.zliepie

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "cart_channel"
        const val NOTIFICATION_ID = 1
    }

    init {
        createNotificationChannel()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Pesanan Keranjang",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi saat produk ditambahkan"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun showCartNotification(productName: String, imageResId: Int) {
        val pictureBitmap = BitmapFactory.decodeResource(context.resources, imageResId)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("ZLiePie - Berhasil!")
            .setContentText("$productName telah masuk ke keranjang.")
            .setLargeIcon(pictureBitmap)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(pictureBitmap)
                .bigLargeIcon(null as android.graphics.Bitmap?))
            .setAutoCancel(true)

            .setLargeIcon(pictureBitmap)

            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(pictureBitmap)
                .bigLargeIcon(null as android.graphics.Bitmap?))

        with(NotificationManagerCompat.from(context)) {
            try {
                notify(NOTIFICATION_ID, builder.build())
            } catch (e: SecurityException) {
            }
        }
    }
}