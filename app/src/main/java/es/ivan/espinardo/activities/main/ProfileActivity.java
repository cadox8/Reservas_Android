package es.ivan.espinardo.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import es.ivan.espinardo.R;
import es.ivan.espinardo.activities.user.LoginActivity;
import es.ivan.espinardo.api.User;
import es.ivan.espinardo.managers.SessionManager;
import es.ivan.espinardo.utils.DataUtils;
import es.ivan.espinardo.utils.Navigation;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(0, 0); // smooth transitions
        this.setContentView(R.layout.activity_profile);

        final Navigation navigation = new Navigation(this);
        navigation.changePage(R.id.perfil);

        final User user = DataUtils.getUser();

        ((TextView)this.findViewById(R.id.profile_username)).setText(user.getUsername());
        ((TextView)this.findViewById(R.id.profile_email)).setText(user.getEmail());

        this.findViewById(R.id.button_logout).setOnClickListener(view -> {
            DataUtils.setUser(null);
            new SessionManager(this).removeToken();
            this.startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        });
    }
}
