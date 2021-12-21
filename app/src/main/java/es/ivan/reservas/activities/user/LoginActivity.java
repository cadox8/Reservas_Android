package es.ivan.reservas.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.ivan.reservas.R;
import es.ivan.reservas.activities.HomeActivity;
import es.ivan.reservas.managers.SessionManager;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        final SessionManager sessionManager = new SessionManager(this);

        if (sessionManager.canAccess()) {
            this.startActivity(new Intent(this, HomeActivity.class));
            this.finish();
            return;
        }

        this.findViewById(R.id.button_login_login).setOnClickListener(view -> {

        });

        this.findViewById(R.id.button_register_login).setOnClickListener(view -> {
            this.startActivity(new Intent(this, RegisterActivity.class));
            this.finish();
        });
    }
}
