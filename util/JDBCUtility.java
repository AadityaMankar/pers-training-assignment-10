package assignment10.util;

import java.sql.*;

public class JDBCUtility {
    private static Connection connection;

    static {
        try {
            Class.forName(JDBCConstants.driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        connection = DriverManager.getConnection(JDBCConstants.url, JDBCConstants.userName, JDBCConstants.password);
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null) connection.close();
    }

    public static void closeStatement(Statement statement) throws SQLException {
        if (statement != null) statement.close();
    }

    public static void closeStatement(PreparedStatement statement) throws SQLException {
        if (statement != null) statement.close();
    }

    public static void closeResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet != null) resultSet.close();
    }
}
