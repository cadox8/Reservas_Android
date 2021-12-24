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

    // --- Temp ---
    String[] maintitle ={
            "Title 1","Title 2",
            "Title 3"
    };

    String[] subtitle ={
            "Sub Title 1","Sub Title 2",
            "Sub Title 3"
    };

    int[] imgid={
            R.drawable.instalaciones,R.drawable.reservas,
            R.drawable.perfil
    };
    // --- ---

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
        installationsProvider.start();
        final Installation[] installations = installationsProvider.getAllInstallations();
        installationsProvider.interrupt();


        if (installations == null) {
            this.startActivity(new Intent(this, ErrorActivity.with("Server error").getClass()));
            this.finish();
            return;
        }

        final InstallationsAdapter adapter = new InstallationsAdapter(this, maintitle, subtitle, imgid);
        final ListView listView = this.findViewById(R.id.list_installations);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            this.startActivity(new Intent(this, InstallationActivity.from(installations[(int) id]).getClass()));
            this.finish();
        });
    }
}
