package es.ivan.espinardo.api.installations;

import es.ivan.espinardo.R;
import es.ivan.espinardo.api.AbstractAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class Installation extends AbstractAPI {

    private final String name;
    private final int type;

    private final double price;

    private final String location;

    private final String thumbnail;
    private final String[] carrousel;

    public InstallationType getInstallationType() {
        return InstallationType.parseType(this.type);
    }

    @RequiredArgsConstructor
    public enum InstallationType {
        VARIADO(0, 0, R.drawable.i_general),
        FUTBOL(1, 0, R.drawable.i_football),
        BALONCESTO(2, 0, R.drawable.i_basket),
        PADEL(3, 0, R.drawable.i_tennis),
        TENIS(4, 0, R.drawable.i_tennis);

        @Getter private final int id;
        @Getter private final int color;
        @Getter private final int icon;

        public static InstallationType parseType(int id) {
            for (InstallationType type : InstallationType.values()) if (type.getId() == id) return type;
            return VARIADO;
        }

        public String capitalize() {
            final String oldName = this.name().toLowerCase();
            return oldName.replace(oldName.charAt(0), Character.toUpperCase(oldName.charAt(0)));
        }
    }
}
