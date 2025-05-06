package tc.tlouro_c.util;

import tc.tlouro_c.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionFactory {
    private static final String URL = Config.getInstance().getDatabaseUrl();

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
