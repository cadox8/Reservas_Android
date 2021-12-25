package es.ivan.espinardo.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.chip.Chip;

import es.ivan.espinardo.R;
import es.ivan.espinardo.api.Installation;
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
        ((TextView)this.findViewById(R.id.installation_price)).setText(installation.getPrice() + "");

        // Chip moment!
        final Chip chip = this.findViewById(R.id.installation_tag);
        chip.setText(installation.getInstallationType().capitalize());
        chip.setBackgroundColor(installation.getInstallationType().getColor());
        chip.setChipIcon(AppCompatResources.getDrawable(this, installation.getInstallationType().getIcon()));
    }

    public static InstallationActivity from(Installation installation) {
        InstallationActivity.installation = installation;
        return new InstallationActivity();
    }
}
