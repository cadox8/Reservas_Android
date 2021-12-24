package es.ivan.espinardo.activities.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import es.ivan.espinardo.R;
import es.ivan.espinardo.managers.SessionManager;
import es.ivan.espinardo.utils.Navigation;

public class BookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(0, 0); // smooth transitions
        this.setContentView(R.layout.activity_booking);

        final Navigation navigation = new Navigation(this);
        navigation.changePage(R.id.reservas);

        final SessionManager sessionManager = new SessionManager(this);

        if (!sessionManager.canAccess()) {
            this.finish();
            return;
        }
    }
}
