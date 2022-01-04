package es.ivan.espinardo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.stripe.android.PaymentConfiguration;

import es.ivan.espinardo.activities.main.BookingsActivity;
import es.ivan.espinardo.activities.user.LoginActivity;
import es.ivan.espinardo.activities.error.ErrorActivity;
import es.ivan.espinardo.managers.SessionManager;
import es.ivan.espinardo.providers.UserProvider;
import es.ivan.espinardo.utils.DataUtils;
import es.ivan.espinardo.utils.Utils;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Utils utils = new Utils(this);

        // Pedimos los permisos necesarios
        utils.askForPermission(Manifest.permission.INTERNET);
        utils.askForPermission(Manifest.permission.ACCESS_NETWORK_STATE);

        // Comprobamos si tiene o no internet
        if (!utils.isNetworkAvailable()) {
            this.startActivity(new Intent(this, ErrorActivity.with(this.getString(R.string.no_internet)).getClass()));
            this.finish();
            return;
        }

        // Iniciamos el Stripe
        PaymentConfiguration.init(this, "pk_test_51KAvnNGlkGQsIde6dBTwGmngYqdtCexiZbNMFZOAXjU8F8iDXXF3RAf0EENkdvtglufMqMsRw60wbdhLdGm6pUXs0074OKPgcH");

        // Cargamos las sessiones
        final SessionManager sessionManager = new SessionManager(this);

        Intent intent;

        if (sessionManager.hasToken()) {
            intent = new Intent(this, BookingsActivity.class);
            DataUtils.setUser(new UserProvider().fetchUserByToken(sessionManager.getToken()));
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        this.startActivity(intent);
        this.finish();
    }
}