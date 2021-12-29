package es.ivan.espinardo.providers;

import java.util.concurrent.ExecutionException;

import es.ivan.espinardo.api.installations.Installation;
import es.ivan.espinardo.api.installations.Installations;

public class InstallationsProvider extends AbstractProvider {

    public Installation[] getAllInstallations() {
        Installation[] installations;

        try {
            installations = this.pool.submit(() -> this.get(Installations.class, "installations")).get().getInstallations();
        } catch (ExecutionException | InterruptedException e) {
            installations = null;
        }
        return installations;
    }
}
