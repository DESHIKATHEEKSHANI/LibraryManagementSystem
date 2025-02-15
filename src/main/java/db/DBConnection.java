package db;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;

    private final String URL ="jdbc:mysql://localhost:3306/librarymanagementsystem";

    private final String userName = "root";

    private final String password = "1234";
    @Getter
    private Connection connection;

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection(URL,userName,password);

    }

    public static DBConnection getInstance() throws SQLException {
        return instance==null?instance = new DBConnection():instance;
    }
}
