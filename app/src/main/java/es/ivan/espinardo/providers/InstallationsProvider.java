package es.ivan.espinardo.providers;

import es.ivan.espinardo.api.installations.Installation;
import es.ivan.espinardo.api.installations.Installations;

public class InstallationsProvider extends AbstractProvider {

    public Installation[] getAllInstallations() {
        return this.get(Installations.class, "installations").getInstallations();
    }
}
