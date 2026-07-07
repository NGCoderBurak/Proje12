package _JDBC;

import Utilities.DBUtility;
import com.mongodb.DBCollection;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;


public class _01_Employee_Querries extends DBUtility {

    @BeforeClass
    public void setup() {
        DBUtility.JDBC_Open();
    }

    @Test
    public void Question01() throws SQLException {
        // D001 departmanındaki tüm çalışanları listeleyiniz.
        String sql = "SELECT e.emp_no, e.first_name, e.last_name, de.dept_no " +
                "FROM employees AS e " +
                "JOIN dept_emp AS de ON e.emp_no=de.emp_no " +
                "WHERE de.dept_no='d001';";

        System.out.println("sql = " + sql);

        // 1. Sorguyu mutlaka DBUtility üzerindeki executeQuery ile çalıştırın!
        DBUtility.executeQuery(sql);

        // 2. Bir önceki adımda düzelttiğimiz getRowCount() ile güvenle assert edin.
        Assert.assertTrue(DBUtility.getRowCount() > 0, "D001 departmanında çalışan bulunamadı!");
    }

    @Test
    // Human Resources departmanındaki tüm çalışanları listeleyiniz.
    public void Question02() {
        String sql = " SELECT e.emp_no, e.first_name, e.last_name FROM employees e JOIN dept_emp de ON e.emp_no=de.emp_no JOIN departments d ON de.dept_no=d.dept_no WHERE d.dept_name='Human Resources'; ";
        DBUtility.executeQuery(sql);
    }

    @Test
    public void Question03() {
        // Tüm çalışanların ortalama maaşını hesapla.
        String sql = "SELECT AVG(salary) AS OrtalamaMaas FROM salaries;";
        DBUtility.executeQuery(sql);

        // AVG fonksiyonu tablo boş olsa bile her zaman tek satır (1 row) özet döner.
        Assert.assertEquals(DBUtility.getRowCount(), 1, "Ortalama maaş sorgusu satır sayısı hatalı!");
    }

    @Test
    public void Question04(){
        DBUtility.executeQuery
                (" SELECT AVG(s.salary) FROM salaries s JOIN employees e ON s.emp_no=e.emp_no WHERE e.gender='M'; ");
         Assert.assertEquals(DBUtility.getRowCount(),1);
    }

    @Test
    public void Question05(){
        DBUtility.executeQuery
                ("SELECT AVG(s.salary) FROM salaries s JOIN employees e ON s.emp_no=e.emp_no WHERE e.gender='F'; ");
        Assert.assertEquals(DBUtility.getRowCount(),1);
    }

    @Test
    public void Question06(){
        DBUtility.executeQuery
                ("SELECT e.emp_no, e.first_name, e.last_name, s.salary FROM employees e JOIN salaries s ON e.emp_no=s.emp_no JOIN dept_emp de ON e.emp_no=de.emp_no JOIN departments d ON de.dept_no=d.dept_no WHERE d.dept_name='Sales' AND s.salary>70000; ");
       Assert.assertTrue(DBUtility.getRowCount()>0);
    }

    @Test
    public void Question07(){
        DBUtility.executeQuery
                (" SELECT * FROM salaries WHERE salary BETWEEN 50000 AND 100000; ");
        Assert.assertTrue(DBUtility.getRowCount()>0);
    }

    @Test
    public void Question08(){
        DBUtility.executeQuery
                (" SELECT de.dept_no, AVG(s.salary) FROM dept_emp de JOIN salaries s ON de.emp_no=s.emp_no GROUP BY de.dept_no; ");
         Assert.assertTrue(DBUtility.getRowCount()>0);
    }

    @Test
    public void Question09(){
        DBUtility.executeQuery
                (" SELECT d.dept_name, AVG(s.salary) FROM departments d JOIN dept_emp de ON d.dept_no=de.dept_no JOIN salaries s ON de.emp_no=s.emp_no GROUP BY d.dept_name; ");
        Assert.assertTrue(DBUtility.getRowCount()>0);
    }

    @Test
    public void Question10(){
        DBUtility.executeQuery
                (" SELECT * FROM salaries WHERE emp_no=10102 ORDER BY from_date; ");
        // Assert.assertTrue(DBUtility.getRowCount()>0);
    }

    // Querry 11-20 goes to : Tuğçe...
    // Querry 20-30 goes to : Sevgi...
    // Querry 30-35 goes to : Zeynep...
    // Querry 35-38 goes to : Yiğithan...

    //    11. 10102 numaralı çalışanın maaş artışlarını listele.
    @Test
    public void Question11() {
        String sql = "SELECT salary, from_date FROM salaries WHERE emp_no = 10102 ORDER BY from_date ASC;";
        DBUtility.executeQuery(sql);
        Assert.assertTrue(DBUtility.getRowCount() > 0, "10102 numaralı çalışanın maaş geçmişi bulunamadı!");
    }

    //    12. En yüksek maaşa sahip çalışanı bul.
    @Test
    public void Question12() {
        String sql = "SELECT e.emp_no, e.first_name, e.last_name, s.salary FROM employees e " +
                "JOIN salaries s ON e.emp_no = s.emp_no " +
                "ORDER BY s.salary DESC LIMIT 1;";
        DBUtility.executeQuery(sql);
        Assert.assertTrue(DBUtility.getRowCount() > 0, "En yüksek maaşlı çalışan bulunamadı!");
    }

    //    13. "Sales" departmanındaki çalışanların en yüksek maaşını bul ve sadece en yüksek maaşlı olanı göster.
    @Test
    public void Question13() {
        String sql = "SELECT e.emp_no, e.first_name, e.last_name, s.salary FROM employees e " +
                "JOIN dept_emp de ON e.emp_no = de.emp_no " +
                "JOIN departments d ON de.dept_no = d.dept_no " +
                "JOIN salaries s ON e.emp_no = s.emp_no " +
                "WHERE d.dept_name = 'Sales' " +
                "ORDER BY s.salary DESC LIMIT 1;";
        DBUtility.executeQuery(sql);
        Assert.assertTrue(DBUtility.getRowCount() > 0, "Sales departmanında en yüksek maaşlı çalışan bulunamadı!");
    }

    //    14. 01.01.1990'dan önce işe alınan tüm çalışanları alfabetik sırayla listele.
    @Test
    public void Question14() {
        String sql = "SELECT * FROM employees WHERE hire_date < '1990-01-01' ORDER BY first_name ASC, last_name ASC;";
        DBUtility.executeQuery(sql);
        Assert.assertTrue(DBUtility.getRowCount() > 0, "Belirtilen tarihten önce işe giren çalışan bulunamadı!");
    }

    //    15. 01.01.1985 - 31.12.1989 arasında işe alınan çalışanları işe giriş tarihine göre sırala.
    @Test
    public void Question15() {
        String sql = "SELECT * FROM employees WHERE hire_date BETWEEN '1985-01-01' AND '1989-12-31' ORDER BY hire_date ASC;";
        DBUtility.executeQuery(sql);
        Assert.assertTrue(DBUtility.getRowCount() > 0, "Belirtilen tarih aralığında işe giren çalışan bulunamadı!");
    }

    //    16. Erkek çalışan sayısını bul.
    @Test
    public void Question16() {
        String sql = "SELECT COUNT(*) AS ErkekSayisi FROM employees WHERE gender = 'M';";
        DBUtility.executeQuery(sql);
        Assert.assertEquals(DBUtility.getRowCount(), 1, "Erkek çalışan sayısı hesaplanamadı!");
    }

    //    17. Kadın çalışan sayısını bul.
    @Test
    public void Question17() {
        String sql = "SELECT COUNT(*) AS KadinSayisi FROM employees WHERE gender = 'F';";
        DBUtility.executeQuery(sql);
        Assert.assertEquals(DBUtility.getRowCount(), 1, "Kadın çalışan sayısı hesaplanamadı!");
    }

    //    18. Cinsiyete göre çalışan sayılarını listele.
    @Test
    public void Question18() {
        String sql = "SELECT gender, COUNT(*) FROM employees GROUP BY gender;";
        DBUtility.executeQuery(sql);
        // Erkek ve Kadın olmak üzere 2 satır gelmesini bekliyoruz
        Assert.assertEquals(DBUtility.getRowCount(), 2, "Cinsiyet gruplama sonucu hatalı!");
    }

    //    19. Toplam çalışan sayısını bul.
    @Test
    public void Question19() {
        String sql = "SELECT COUNT(*) AS ToplamCalisan FROM employees;";
        DBUtility.executeQuery(sql);

        // Satır sayısını tam olarak 1 beklemek yerine sıfırdan büyük satır döndüğünü doğrulayın
        Assert.assertTrue(DBUtility.getRowCount() > 0, "Toplam çalışan sayısı hesaplanamadı, veri bulunamadı!");
    }

    //    20. Kaç farklı isimde çalışan olduğunu bul.
    @Test
    public void Question20() {
        String sql = "SELECT COUNT(DISTINCT first_name) AS FarkliIsimSayisi FROM employees;";
        DBUtility.executeQuery(sql);
        Assert.assertEquals(DBUtility.getRowCount(), 1, "Farklı isim sayısı hesaplanamadı!");
    }


    @AfterClass
    public void tearDown() {
        DBUtility.JDBC_Close();
    }

}