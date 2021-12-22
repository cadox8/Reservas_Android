package es.ivan.reservas.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Utils {

    private final AppCompatActivity activity;

    public Utils(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void askForPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this.activity.getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED) return;
        ActivityCompat.requestPermissions(this.activity, new String[]{permission}, PackageManager.PERMISSION_GRANTED);
    }

    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) this.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
