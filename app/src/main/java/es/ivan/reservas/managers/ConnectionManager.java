package es.ivan.reservas.managers;

import java.sql.SQLException;

import es.ivan.reservas.utils.MySQL;
import lombok.Getter;

public class ConnectionManager {

    @Getter private static MySQL MYSQL;

    public static boolean connectToDatabase() {
        try {
            ConnectionManager.MYSQL = new MySQL("cadox8.es", "reservas", "reservas", "oLs4dkP}");
            ConnectionManager.MYSQL.openConnection();
            return MYSQL.checkConnection();
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
    }
}
