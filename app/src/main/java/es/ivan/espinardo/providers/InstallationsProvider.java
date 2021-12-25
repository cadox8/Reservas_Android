package es.ivan.espinardo.providers;

import es.ivan.espinardo.api.Installation;
import es.ivan.espinardo.api.Installations;

public class InstallationsProvider extends AbstractProvider {

    public Installation[] getAllInstallations() {
        return this.get(Installations.class, "installations").getInstallations();
    }
}
