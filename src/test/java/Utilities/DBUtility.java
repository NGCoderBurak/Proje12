package Utilities;

import java.sql.*;
import java.util.ArrayList;

public class DBUtility {

    // ham bilgilerin tutulduğu yerden benim verdiğim kritere göre billeri al
    // 2 boyutlu bir list olarak bana ver...
    // Neden List, Neden Array? : Çünkü Db bilgileri Başlık altında birer column ve Row by Row , satır sdatır gerliyor da ondan...
    // getDataFromDB metodu oluşturmak üzere
    public static Connection connection;
    public static Statement sorguEkrani;
    private static ResultSet resultSet;

    public static void JDBC_Open() {
        String serverDBURL = "jdbc:mysql://acela.proxy.rlwy.net:15150/employees";
        String username = "sdet";
        String password = "SDET123!";

        try {
            connection = DriverManager.getConnection(serverDBURL, username, password);
            sorguEkrani = connection.createStatement();
        } catch (Exception ex) {
            System.out.println("ex.getMessage() = " + ex.getMessage());
        }
    }


    public static void JDBC_Close() {
        try {
            connection.close();
        } catch (Exception ex) {
            System.out.println("ex.getMessage() = " + ex.getMessage());
        }
    }


    public static ArrayList<ArrayList<String>>

    getListData(String sorgu) {
        ArrayList<ArrayList<String>> tablo = new ArrayList<>();

        try {
            ResultSet rs = sorguEkrani.executeQuery(sorgu);
            ResultSetMetaData rsmd = rs.getMetaData();
            int kolonSayisi = rsmd.getColumnCount();

            while (rs.next()) {
                ArrayList<String> satir = new ArrayList<>();
                for (int i = 1; i <= kolonSayisi; i++)
                    satir.add(rs.getString(i));

                tablo.add(satir);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return tablo;
    }


    public static void executeQuery(String sql) {
        try {
            sorguEkrani = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            resultSet = sorguEkrani.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getRowCount() {
        try {
            if (resultSet == null) return 0; // NullPointer almamak için güvenlik kontrolü

            resultSet.last();                // 1. ÖNCE imleci en son satıra götür.
            int rowCount = resultSet.getRow(); // 2. SONRA durduğun son satırın numarasını al (Bu bize toplam satır sayısını verir).
            resultSet.beforeFirst();         // 3. İmleci tekrar en başa çek (Daha sonra verileri okumak isterseniz diye).

            return rowCount;                 // Doğru satır sayısını dön.
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
