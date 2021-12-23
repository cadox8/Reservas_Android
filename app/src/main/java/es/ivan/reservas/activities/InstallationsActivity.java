package es.ivan.reservas.activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.ivan.reservas.R;
import es.ivan.reservas.activities.adapter.InstallationsAdapter;
import es.ivan.reservas.managers.SessionManager;
import es.ivan.reservas.utils.Navigation;

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

        final InstallationsAdapter adapter = new InstallationsAdapter(this, maintitle, subtitle, imgid);
        final ListView listView = this.findViewById(R.id.list_installations);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            Toast.makeText(this.getApplicationContext(), "Mostrar instalación: " + id, Toast.LENGTH_SHORT).show();
        });
    }
}
