package es.ivan.espinardo.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import es.ivan.espinardo.R;
import es.ivan.espinardo.activities.InstallationActivity;
import es.ivan.espinardo.activities.adapter.InstallationsAdapter;
import es.ivan.espinardo.activities.error.ErrorActivity;
import es.ivan.espinardo.api.installations.Installation;
import es.ivan.espinardo.managers.SessionManager;
import es.ivan.espinardo.providers.InstallationsProvider;
import es.ivan.espinardo.utils.Navigation;

public class InstallationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(0, 0); // smooth transitions
        this.setContentView(R.layout.activity_installations);

        final Navigation navigation = new Navigation(this);
        navigation.changePage(R.id.instalaciones);

        final SessionManager sessionManager = new SessionManager(this);

        if (!sessionManager.canAccess()) {
            this.finish();
            return;
        }

        final InstallationsProvider installationsProvider = new InstallationsProvider();
        final Installation[] installations = installationsProvider.getAllInstallations();

        if (installations == null) {
            this.startActivity(new Intent(this, ErrorActivity.with("Server error").getClass()));
            this.finish();
        } else {
            final InstallationsAdapter adapter = new InstallationsAdapter(this, installations);
            final ListView listView = this.findViewById(R.id.list_installations);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener((adapterView, view, position, id) -> {
                this.startActivity(new Intent(this, InstallationActivity.from(installations[position]).getClass()));
                this.finish();
            });
        }
    }
}
