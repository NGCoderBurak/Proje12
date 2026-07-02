package Utilities;

import java.sql.*;
import java.util.ArrayList;

public class DBUtility {

    // ham bilgilerin tutulduğu yerden benim verdiğim kritere göre billeri al
    // 2 boyutlu bir list olarak bana ver
    // getDataFromDB metodu oluşturmak üzere

    public static ArrayList<ArrayList<String>> getListData(String sorgu){
        ArrayList<ArrayList<String>> tablo=new ArrayList<>();
        DBConnectionOpen();

        try {
            ResultSet rs = sorguEkrani.executeQuery(sorgu);
            ResultSetMetaData rsmd = rs.getMetaData();
            int kolonSayisi = rsmd.getColumnCount();

            while (rs.next()) {
                ArrayList<String> satir=new ArrayList<>();
                for (int i = 1; i <= kolonSayisi; i++)
                    satir.add(rs.getString(i));

                tablo.add(satir);
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        DBConnectionClose();
        return tablo;
    }


    public static Connection connection;
    public static Statement sorguEkrani;

    public static void DBConnectionOpen(){
        String serverDBURL="jdbc:mysql://acela.proxy.rlwy.net:15150/sakila";
        String username="sdet";
        String password="SDET123!";

        try {
            connection = DriverManager.getConnection(serverDBURL, username, password);
            sorguEkrani = connection.createStatement();
        }
        catch (Exception ex)
        {
            System.out.println("ex.getMessage() = " + ex.getMessage());
        }
    }

    public static void DBConnectionClose(){
        try {
            connection.close();
        }
        catch (Exception ex)
        {
            System.out.println("ex.getMessage() = " + ex.getMessage());
        }
    }

}
