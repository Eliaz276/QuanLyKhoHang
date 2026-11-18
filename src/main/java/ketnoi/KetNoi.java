
package ketnoi;

import java.sql.Connection;
import java.sql.DriverManager;

public class KetNoi {
    public static Connection ketNoi() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;"
                    + "databaseName=QuanLyKhoHang;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true";
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Ket noi SQL Server thanh cong!");
            return conn;
        } catch (Exception e) {
            System.out.println("Loi ket noi: " + e.getMessage());
            return null;
        }
    }
}
