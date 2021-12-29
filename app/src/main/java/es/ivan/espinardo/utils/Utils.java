package es.ivan.espinardo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Utils {

    private final AppCompatActivity activity;

    public void askForPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this.activity.getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED) return;
        ActivityCompat.requestPermissions(this.activity, new String[]{permission}, PackageManager.PERMISSION_GRANTED);
    }

    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) this.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showAlert(String title, @Nullable String message) {
        final AlertDialog dialog = new AlertDialog.Builder(this.activity.getApplicationContext())
                .setTitle(title).setMessage(message).setPositiveButton("Ok", null).create();
        dialog.show();
    }
}
