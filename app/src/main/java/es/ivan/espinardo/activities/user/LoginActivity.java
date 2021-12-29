package es.ivan.espinardo.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import es.ivan.espinardo.R;
import es.ivan.espinardo.activities.main.BookingActivity;
import es.ivan.espinardo.activities.error.ErrorActivity;
import es.ivan.espinardo.api.User;
import es.ivan.espinardo.managers.SessionManager;
import es.ivan.espinardo.providers.UserProvider;
import es.ivan.espinardo.utils.DataUtils;

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
            final UserProvider userProvider = new UserProvider();

            final TextInputEditText username = this.findViewById(R.id.edit_user_login_text);
            final TextInputEditText password = this.findViewById(R.id.edit_password_login_text);

            final User user = userProvider.login(username.getText().toString(), password.getText().toString());

            if (user == null) {
                this.startActivity(new Intent(this, ErrorActivity.with("Server error").getClass()));
                this.finish();
                return;
            }

            if (user.getError() != null && !user.getError().trim().isEmpty()) {
                DynamicToast.makeError(this, user.getError(), Toast.LENGTH_SHORT).show();
                return;
            }

            DataUtils.setUser(user);
            sessionManager.saveToken(user.getToken());
            this.startActivity(new Intent(this, BookingActivity.class));
            this.finish();

            // --- Old Code ---
/*            new Thread(() -> {
                final TextInputEditText username = this.findViewById(R.id.edit_user_login_text);
                final TextInputEditText password = this.findViewById(R.id.edit_password_login_text);
                final User user = userProvider.login(username.getText().toString(), password.getText().toString());

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
                this.startActivity(new Intent(this, BookingActivity.class));
                this.finish();
            }).start();*/
        });

        this.findViewById(R.id.button_register_login).setOnClickListener(view -> {
            this.startActivity(new Intent(this, RegisterActivity.class));
            this.finish();
        });
    }
}
