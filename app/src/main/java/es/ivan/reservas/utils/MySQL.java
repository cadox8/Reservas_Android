package es.ivan.reservas.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private final String user, database, password, port, hostname;
    protected Connection connection;

    public MySQL(String hostname, String database, String username, String password) {
        this.hostname = hostname;
        this.port = "3306";
        this.database = database;
        this.user = username;
        this.password = password;
    }

    public boolean checkConnection() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean closeConnection() throws SQLException {
        if (connection == null) return false;
        connection.close();
        return true;
    }

    public Connection openConnection() throws SQLException, ClassNotFoundException {
        if (checkConnection()) return connection;

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database + "?autoReconnect=true", this.user, this.password);
        return connection;
    }


    // --- User Management ---
    public void registerUser() {

    }

    public boolean userIsRegistered() {
        return false;
    }

    // --- Reservas  ---
}
