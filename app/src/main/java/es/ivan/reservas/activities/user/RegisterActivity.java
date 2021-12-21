package es.ivan.reservas.activities.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import es.ivan.reservas.R;
import es.ivan.reservas.activities.HomeActivity;
import es.ivan.reservas.managers.SessionManager;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_register);

        final SessionManager sessionManager = new SessionManager(this);

        if (sessionManager.canAccess()) {
            this.startActivity(new Intent(this, HomeActivity.class));
            this.finish();
            return;
        }

        this.findViewById(R.id.button_register_register).setOnClickListener(view -> {

        });

        this.findViewById(R.id.button_login_register).setOnClickListener(view -> {
            this.startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        });
    }
}
