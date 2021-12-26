package es.ivan.espinardo.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import es.ivan.espinardo.R;
import es.ivan.espinardo.activities.main.BookingActivity;
import es.ivan.espinardo.activities.utils.ErrorActivity;
import es.ivan.espinardo.api.User;
import es.ivan.espinardo.managers.SessionManager;
import es.ivan.espinardo.providers.UserProvider;
import es.ivan.espinardo.utils.DataUtils;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(0, 0); // smooth transitions
        this.setContentView(R.layout.activity_register);

        final SessionManager sessionManager = new SessionManager(this);

        if (sessionManager.hasToken()) {
            this.startActivity(new Intent(this, BookingActivity.class));
            this.finish();
            return;
        }

        this.findViewById(R.id.button_register_register).setOnClickListener(view -> {
            final UserProvider userProvider = new UserProvider();

            new Thread(() -> {
                final TextInputEditText username = this.findViewById(R.id.edit_user_register_text);
                final TextInputEditText email = this.findViewById(R.id.edit_email_register_text);
                final TextInputEditText password = this.findViewById(R.id.edit_password_register_text);
                final User user = userProvider.register(username.getText().toString(), email.getText().toString(), password.getText().toString());

                if (user == null) {
                    this.startActivity(new Intent(this, ErrorActivity.with("Server error").getClass()));
                    this.finish();
                    return;
                }

                if (user.getError() != null && !user.getError().trim().isEmpty()) {
                    this.runOnUiThread(() -> DynamicToast.makeError(this, user.getError(), Toast.LENGTH_SHORT).show());
                    return;
                }

                DataUtils.setUser(user);
                sessionManager.saveToken(user.getToken());
                this.runOnUiThread(() ->DynamicToast.makeSuccess(this, "Usuario creado", Toast.LENGTH_SHORT).show());
                this.startActivity(new Intent(this, BookingActivity.class));
                this.finish();
            }).start();
        });

        this.findViewById(R.id.button_login_register).setOnClickListener(view -> {
            this.startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        });
    }
}
