package es.ivan.espinardo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import es.ivan.espinardo.activities.main.BookingActivity;
import es.ivan.espinardo.activities.user.LoginActivity;
import es.ivan.espinardo.activities.utils.ErrorActivity;
import es.ivan.espinardo.managers.SessionManager;
import es.ivan.espinardo.utils.Utils;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Utils utils = new Utils(this);

        utils.askForPermission(Manifest.permission.INTERNET);
        utils.askForPermission(Manifest.permission.ACCESS_NETWORK_STATE);

        if (!utils.isNetworkAvailable()) {
            this.startActivity(new Intent(this, ErrorActivity.with(this.getString(R.string.no_internet)).getClass()));
            this.finish();
            return;
        }

        final SessionManager sessionManager = new SessionManager(this);

        Intent intent;

        if (sessionManager.hasToken()) {
            intent = new Intent(this, BookingActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}