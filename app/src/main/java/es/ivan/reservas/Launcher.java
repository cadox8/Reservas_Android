package es.ivan.reservas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import es.ivan.reservas.activities.HomeActivity;
import es.ivan.reservas.activities.user.LoginActivity;
import es.ivan.reservas.managers.ConnectionManager;
import es.ivan.reservas.managers.SessionManager;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!ConnectionManager.connectToDatabase()) {
            this.setContentView(R.layout.activity_error);
            return;
        }

        final SessionManager sessionManager = new SessionManager(this);

        Intent intent;

        if (sessionManager.hasToken()) {
            intent = new Intent(this, HomeActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}