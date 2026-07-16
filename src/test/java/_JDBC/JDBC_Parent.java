package _JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class JDBC_Parent {
    public static Connection connection;
    public static Statement sorguEkrani;

    public static void DBConnectionOpen() {
        String db_url = "jdbc:mysql://acela.proxy.rlwy.net:15150/employees";
        String username = "sdet";
        String password = "SDET123!";

        try {
            // Bağlantı zaten açık ise tekrar açmaya çalışıp sunucuyu yormayalım
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(db_url, username, password);
                sorguEkrani = connection.createStatement();
            }
        } catch (Exception ex) {
            System.out.println("Bağlantı açılırken hata oluştu: " + ex.getMessage());
        }
    }

    public static void DBConnectionClose() {
        try {
            // NullPointerException almamak için önce null ve kapalı olma kontrolü yapıyoruz
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception ex) {
            System.out.println("Bağlantı kapatılırken hata oluştu: " + ex.getMessage());
        }
    }
}
