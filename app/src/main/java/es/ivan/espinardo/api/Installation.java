package es.ivan.espinardo.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class Installation extends AbstractAPI {

    private final String name;
    private final int installationType;

    private final double price;

    private final String location;

    private final String thumbnail;
    private final String[] carrousel;

    public InstallationType getInstallationType() {
        return InstallationType.parseType(this.installationType);
    }

    @RequiredArgsConstructor
    public enum InstallationType {
        VARIADO(0, 0, 0),
        FUTBOL(1, 0, 0),
        BALONCESTO(2, 0, 0),
        PADEL(3, 0, 0),
        TENIS(4, 0, 0);

        @Getter private final int id;
        @Getter private final int color;
        @Getter private final int icon;

        public static InstallationType parseType(int id) {
            for (InstallationType type : InstallationType.values()) if (type.getId() == id) return type;
            return null;
        }

        public String capitalize() {
            final String oldName = this.name().toLowerCase();
            return oldName.replace(oldName.charAt(0), Character.toUpperCase(oldName.charAt(0)));
        }
    }
}
