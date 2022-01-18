package es.ivan.espinardo.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Random;

import es.ivan.espinardo.R;
import es.ivan.espinardo.activities.helpers.BookingActivity;
import es.ivan.espinardo.activities.main.BookingsActivity;
import es.ivan.espinardo.api.bookings.Booking;

public class NotificationUtils {

    public Notification createNotification(Context context, String title) {
        return new Notification.Builder(context).setContentTitle(title).setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(PendingIntent.getActivity(context, new Random().nextInt(),
                        new Intent(context, BookingsActivity.class), PendingIntent.FLAG_ONE_SHOT)).setAutoCancel(true).build();
    }
}
