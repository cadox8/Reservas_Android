package es.ivan.espinardo.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.ivan.espinardo.R;
import es.ivan.espinardo.activities.InstallationActivity;
import es.ivan.espinardo.activities.adapter.InstallationsAdapter;
import es.ivan.espinardo.activities.utils.ErrorActivity;
import es.ivan.espinardo.api.Installation;
import es.ivan.espinardo.managers.SessionManager;
import es.ivan.espinardo.providers.InstallationsProvider;
import es.ivan.espinardo.utils.Navigation;

public class InstallationsActivity extends AppCompatActivity {

     private Installation[] installations = new Installation[0];

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


        new Thread(() -> {
            installations = installationsProvider.getAllInstallations();

            if (installations == null) {
                this.startActivity(new Intent(this, ErrorActivity.with("Server error").getClass()));
                this.finish();
            } else {
                this.runOnUiThread(this::updateUI);
            }
        }).start();
    }

    private void updateUI() {
        final InstallationsAdapter adapter = new InstallationsAdapter(this, installations);
        final ListView listView = this.findViewById(R.id.list_installations);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            this.startActivity(new Intent(this, InstallationActivity.from(installations[(int) id]).getClass()));
            this.finish();
        });
    }
}
