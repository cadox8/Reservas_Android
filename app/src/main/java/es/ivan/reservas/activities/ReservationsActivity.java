package es.ivan.reservas.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import es.ivan.reservas.R;
import es.ivan.reservas.managers.SessionManager;
import es.ivan.reservas.utils.Navigation;

public class ReservationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(0, 0); // smooth transitions
        this.setContentView(R.layout.activity_home);

        final Navigation navigation = new Navigation(this);
        navigation.changePage(R.id.reservas);

        final SessionManager sessionManager = new SessionManager(this);

        if (!sessionManager.canAccess()) {
            this.finish();
            return;
        }
    }
}
