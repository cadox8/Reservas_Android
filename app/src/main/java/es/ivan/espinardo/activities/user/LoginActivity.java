package es.ivan.espinardo.activities.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import es.ivan.espinardo.R;
import es.ivan.espinardo.activities.main.BookingActivity;
import es.ivan.espinardo.managers.SessionManager;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(0, 0); // smooth transitions
        this.setContentView(R.layout.activity_login);

        final SessionManager sessionManager = new SessionManager(this);

        if (sessionManager.hasToken()) {
            this.startActivity(new Intent(this, BookingActivity.class));
            this.finish();
            return;
        }

        this.findViewById(R.id.button_login_login).setOnClickListener(view -> {
            sessionManager.saveToken("test");
            this.startActivity(new Intent(this, BookingActivity.class));
        });

        this.findViewById(R.id.button_register_login).setOnClickListener(view -> {
            this.startActivity(new Intent(this, RegisterActivity.class));
            this.finish();
        });
    }
}
