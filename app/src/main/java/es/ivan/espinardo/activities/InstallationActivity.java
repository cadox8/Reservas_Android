package es.ivan.espinardo.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;

import es.ivan.espinardo.R;
import es.ivan.espinardo.api.installations.Installation;
import es.ivan.espinardo.managers.SessionManager;

public class InstallationActivity extends AppCompatActivity {

    private static Installation installation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(0, 0); // smooth transitions
        this.setContentView(R.layout.activity_installation);

        final SessionManager sessionManager = new SessionManager(this);

        if (!sessionManager.canAccess()) {
            this.finish();
            return;
        }

        // --- Populate page ---
        ((TextView)this.findViewById(R.id.installation_name)).setText(installation.getName());
        ((TextView)this.findViewById(R.id.installation_price)).setText("Precio: " + installation.getPrice() + "€");

        // Chip moment!
        final Chip chip = this.findViewById(R.id.installation_tag);
        chip.setText(installation.getInstallationType().capitalize());
        chip.setTextColor(Color.BLACK);
        chip.setChipIconTint(ColorStateList.valueOf(Color.BLACK));

        switch (installation.getInstallationType()) {
            case VARIADO:
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#673ab7")));
                chip.setChipIcon(this.getDrawable(R.drawable.i_general));
                break;
            case FUTBOL:
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.GREEN));
                chip.setChipIcon(this.getDrawable(R.drawable.i_football));
                break;
            case BALONCESTO:
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.YELLOW));
                chip.setChipIcon(this.getDrawable(R.drawable.i_basket));
                break;
            case TENIS:
            case PADEL:
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.CYAN));
                chip.setChipIcon(this.getDrawable(R.drawable.i_tennis));
                break;
        }
    }

    public static InstallationActivity from(Installation installation) {
        InstallationActivity.installation = installation;
        return new InstallationActivity();
    }
}
