package Chuc_nang;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String URL = "jdbc:sqlserver://LAPTOP-448VUAA7\\SQLEXPRESS;databaseName=Job_Skill;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456"; 

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("⚠ Không tìm thấy Driver SQL Server!");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("❌ Kết nối SQL thất bại!");
            e.printStackTrace();
            return null;
        }
    }
}
