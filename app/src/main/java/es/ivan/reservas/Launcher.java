package es.ivan.reservas;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import es.ivan.reservas.activities.ReservationsActivity;
import es.ivan.reservas.activities.user.LoginActivity;
import es.ivan.reservas.managers.SessionManager;
import es.ivan.reservas.utils.Utils;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Utils utils = new Utils(this);

        utils.askForPermission(Manifest.permission.INTERNET);
        utils.askForPermission(Manifest.permission.ACCESS_NETWORK_STATE);

        if (!utils.isNetworkAvailable()) {
            this.setContentView(R.layout.activity_error);
            return;
        }

        final SessionManager sessionManager = new SessionManager(this);

        Intent intent;

        if (sessionManager.hasToken()) {
            intent = new Intent(this, ReservationsActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}