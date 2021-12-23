package es.ivan.reservas.api;

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
    private final String image;

    public InstallationType getInstallationType() {
        return InstallationType.parseType(this.installationType);
    }

    @RequiredArgsConstructor
    public enum InstallationType {
        FUTBOL(0),
        BALONCESTO(1),
        PADEL(2),
        TENIS(3);

        @Getter private final int id;

        public static InstallationType parseType(int id) {
            for (InstallationType type : InstallationType.values()) if (type.getId() == id) return type;
            return null;
        }
    }
}
