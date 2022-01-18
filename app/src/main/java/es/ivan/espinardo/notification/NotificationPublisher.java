package es.ivan.espinardo.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationPublisher extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification notification = intent.getParcelableExtra("notification");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel("espinardo_info", "expinardo_book", NotificationManager.IMPORTANCE_HIGH);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        int id = intent.getIntExtra("espinardo_info", 0);
        assert notificationManager != null;
        notificationManager.notify(id, notification);
    }
}
