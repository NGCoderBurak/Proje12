package _JDBC;

import Utilities.DBUtility;
import com.mongodb.DBCollection;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public void Question04() {
        DBUtility.executeQuery
                (" SELECT AVG(s.salary) FROM salaries s JOIN employees e ON s.emp_no=e.emp_no WHERE e.gender='M'; ");
        Assert.assertEquals(DBUtility.getRowCount(), 1);
    }

    @Test
    public void Question05() {
        DBUtility.executeQuery
                ("SELECT AVG(s.salary) FROM salaries s JOIN employees e ON s.emp_no=e.emp_no WHERE e.gender='F'; ");
        Assert.assertEquals(DBUtility.getRowCount(), 1);
    }

    @Test
    public void Question06() {
        DBUtility.executeQuery
                ("SELECT e.emp_no, e.first_name, e.last_name, s.salary FROM employees e JOIN salaries s ON e.emp_no=s.emp_no JOIN dept_emp de ON e.emp_no=de.emp_no JOIN departments d ON de.dept_no=d.dept_no WHERE d.dept_name='Sales' AND s.salary>70000; ");
        Assert.assertTrue(DBUtility.getRowCount() > 0);
    }

    @Test
    public void Question07() {
        DBUtility.executeQuery
                (" SELECT * FROM salaries WHERE salary BETWEEN 50000 AND 100000; ");
        Assert.assertTrue(DBUtility.getRowCount() > 0);
    }

    @Test
    public void Question08() {
        DBUtility.executeQuery
                (" SELECT de.dept_no, AVG(s.salary) FROM dept_emp de JOIN salaries s ON de.emp_no=s.emp_no GROUP BY de.dept_no; ");
        Assert.assertTrue(DBUtility.getRowCount() > 0);
    }

    @Test
    public void Question09() {
        DBUtility.executeQuery
                (" SELECT d.dept_name, AVG(s.salary) FROM departments d JOIN dept_emp de ON d.dept_no=de.dept_no JOIN salaries s ON de.emp_no=s.emp_no GROUP BY d.dept_name; ");
        Assert.assertTrue(DBUtility.getRowCount() > 0);
    }

    @Test
    public void Question10() {
        DBUtility.executeQuery
                (" SELECT * FROM salaries WHERE emp_no=10102 ORDER BY from_date; ");
        Assert.assertTrue(DBUtility.getRowCount() > 0);
    }

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

    // 21. Kaç farklı departman olduğunu bul.
    @Test
    public void Question21() {
        String sql = "SELECT COUNT(DISTINCT dept_name) FROM departments;";
        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "Sorgu sonucu boş döndü!");

        int departmanSayisi = Integer.parseInt(DBUtility.getCellValue(1, 1).toString());
        Assert.assertTrue(departmanSayisi > 0, "Veritabanında hiç departman bulunamadı!");

        System.out.println("Farklı Departman Sayısı: " + departmanSayisi);
    }

    // 22. Her departmandaki çalışan sayısını listele.
    @Test
    public void Question22() {
        String sql = "SELECT department_name, COUNT(employee_id) FROM employees GROUP BY department_name;";
        ArrayList<ArrayList<String>> tablo = DBUtility.getListData(sql);
        Assert.assertTrue(tablo.size() > 0, "Departman çalışan listesi boş döndü!");
        System.out.println("Departman Adı \t| Çalışan Sayısı");
        System.out.println("---------------------------------");
        for (ArrayList<String> satir : tablo) {
            System.out.println(satir.get(0) + " \t| " + satir.get(1));
        }
    }

    // 23. 20 Şubat 1990’dan geriye doğru son 5 yılda işe alınan çalışanları listele.
    @Test
    public void Question23() {
        String sql = "SELECT * FROM employees WHERE hire_date BETWEEN '1985-02-20' AND '1990-02-20' ORDER BY hire_date DESC;";
        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "Belirtilen tarihler arasında işe alınan çalışan bulunamadı!");

        for (int i = 1; i <= satirSayisi; i++) {
            String calisanAdi = DBUtility.getCellValue(i, 2).toString(); // 2. sütun: Adı
            String iseGirisTarihi = DBUtility.getCellValue(i, 5).toString(); // 5. sütun: İşe Giriş Tarihi
            System.out.println("Çalışan: " + calisanAdi + " | İşe Giriş: " + iseGirisTarihi);
        }
    }

    // 24. “Annemarie Redmiles” adlı çalışanın bilgilerini listele.
    @Test
    public void Question24() {
        String sql = "SELECT * FROM employees WHERE first_name = 'Annemarie' AND last_name = 'Redmiles';";
        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "Annemarie Redmiles isimli çalışan bulunamadı!");

        for (int i = 1; i <= satirSayisi; i++) {
            String ad = DBUtility.getCellValue(i, 2).toString(); // 2. sütun: Adı (first_name)
            String soyad = DBUtility.getCellValue(i, 3).toString(); // 3. sütun: Soyadı (last_name)
            String email = DBUtility.getCellValue(i, 4).toString(); // 4. sütun: Email

            System.out.println("Çalışan Bilgisi -> Ad: " + ad + " | Soyad: " + soyad + " | E-posta: " + email);
        }
    }

    // 25. “Annemarie Redmiles” adlı çalışanın tüm bilgilerini (maaş, departman, title dahil) listele.
    @Test
    public void Question25() {
        String sql = "SELECT e.*, s.salary, t.title, d.dept_name " +
                "FROM employees e " +
                "LEFT JOIN salaries s ON e.emp_no = s.emp_no " +
                "LEFT JOIN titles t ON e.emp_no = t.emp_no " +
                "LEFT JOIN dept_emp de ON e.emp_no = de.emp_no " +
                "LEFT JOIN departments d ON de.dept_no = d.dept_no " +
                "WHERE e.first_name = 'Annemarie' AND e.last_name = 'Redmiles';";

        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "Annemarie Redmiles adlı çalışan ve detaylı bilgileri bulunamadı!");

        for (int i = 1; i <= satirSayisi; i++) {
            String ad = DBUtility.getCellValue(i, 2).toString();         // first_name
            String soyad = DBUtility.getCellValue(i, 3).toString();      // last_name
            String maas = DBUtility.getCellValue(i, 7).toString();       // salary
            String unvan = DBUtility.getCellValue(i, 8).toString();      // title
            String departman = DBUtility.getCellValue(i, 9).toString();  // dept_name

            System.out.println("Çalışan Detayları -> Ad Soyad: " + ad + " " + soyad +
                    " | Maaş: " + maas +
                    " | Unvan: " + unvan +
                    " | Departman: " + departman);
        }
    }

    // 26. D005 departmanındaki tüm çalışanları ve yöneticileri listele.
    @Test
    public void Question26() {
        String sql = "SELECT e.emp_no, e.first_name, e.last_name, " +
                "CASE WHEN dm.emp_no IS NOT NULL THEN 'Yönetici (Manager)' ELSE 'Çalışan (Employee)' END AS rol " +
                "FROM dept_emp de " +
                "JOIN employees e ON de.emp_no = e.emp_no " +
                "LEFT JOIN dept_manager dm ON de.dept_no = dm.dept_no AND de.emp_no = dm.emp_no " +
                "WHERE de.dept_no = 'd005';";

        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "d005 departmanında çalışan veya yönetici bulunamadı!");

        for (int i = 1; i <= satirSayisi; i++) {
            String empNo = DBUtility.getCellValue(i, 1).toString();   // emp_no
            String ad = DBUtility.getCellValue(i, 2).toString();      // first_name
            String soyad = DBUtility.getCellValue(i, 3).toString();   // last_name
            String rol = DBUtility.getCellValue(i, 4).toString();     // rol (Yönetici mi, Çalışan mı)

            System.out.println("ID: " + empNo + " | Ad Soyad: " + ad + " " + soyad + " | Rol: " + rol);
        }
    }

    // 27. 24.02.1994’ten sonra işe alınmış ve maaşı 50.000’den yüksek çalışanları listele.
    @Test
    public void Question27() {
        String sql = "SELECT e.emp_no, e.first_name, e.last_name, e.hire_date, s.salary " +
                "FROM employees e " +
                "JOIN salaries s ON e.emp_no = s.emp_no " +
                "WHERE e.hire_date > '1994-02-24' AND s.salary > 50000;";

        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "Belirtilen tarihten sonra işe giren ve maaşı 50.000'den yüksek olan çalışan bulunamadı!");

        for (int i = 1; i <= satirSayisi; i++) {
            String empNo = DBUtility.getCellValue(i, 1).toString();       // emp_no
            String ad = DBUtility.getCellValue(i, 2).toString();          // first_name
            String soyad = DBUtility.getCellValue(i, 3).toString();       // last_name
            String iseGiris = DBUtility.getCellValue(i, 4).toString();    // hire_date
            String maas = DBUtility.getCellValue(i, 5).toString();        // salary

            System.out.println("ID: " + empNo + " | Ad Soyad: " + ad + " " + soyad +
                    " | İşe Giriş: " + iseGiris + " | Maaş: " + maas);
        }
    }

    // 28. “Sales” departmanında “Manager” unvanına sahip çalışanları listele.
    @Test
    public void Question28() {
        String sql = "SELECT e.emp_no, e.first_name, e.last_name, t.title, d.dept_name " +
                "FROM employees e " +
                "JOIN titles t ON e.emp_no = t.emp_no " +
                "JOIN dept_emp de ON e.emp_no = de.emp_no " +
                "JOIN departments d ON de.dept_no = d.dept_no " +
                "WHERE d.dept_name = 'Sales' AND t.title = 'Manager';";

        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "Sales departmanında Manager unvanına sahip çalışan bulunamadı!");

        for (int i = 1; i <= satirSayisi; i++) {
            String empNo = DBUtility.getCellValue(i, 1).toString();      // emp_no
            String ad = DBUtility.getCellValue(i, 2).toString();         // first_name
            String soyad = DBUtility.getCellValue(i, 3).toString();      // last_name
            String unvan = DBUtility.getCellValue(i, 4).toString();      // title
            String departman = DBUtility.getCellValue(i, 5).toString();  // dept_name

            System.out.println("ID: " + empNo + " | Ad Soyad: " + ad + " " + soyad +
                    " | Unvan: " + unvan + " | Departman: " + departman);
        }
    }

    // 29. 10102 numaralı çalışanın tüm pozisyon geçmişini listele.
    @Test
    public void Question29() {
        String sql = "SELECT emp_no, title, from_date, to_date FROM titles WHERE emp_no = 10102 ORDER BY from_date DESC;";

        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "10102 numaralı çalışanın pozisyon geçmişi bulunamadı!");

        for (int i = 1; i <= satirSayisi; i++) {
            String empNo = DBUtility.getCellValue(i, 1).toString();    // emp_no
            String unvan = DBUtility.getCellValue(i, 2).toString();    // title
            String baslangic = DBUtility.getCellValue(i, 3).toString(); // from_date
            String bitis = DBUtility.getCellValue(i, 4).toString();     // to_date

            System.out.println("Çalışan ID: " + empNo + " | Unvan: " + unvan +
                    " | Başlangıç: " + baslangic + " | Bitiş: " + bitis);
        }
    }

    // 30. Ortalama çalışan yaşını hesapla.
    @Test
    public void Question30() {
        String sql = "SELECT ROUND(AVG(2026 - YEAR(birth_date)), 1) AS OrtalamaYas FROM employees;";
        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "Ortalama çalışan yaşı hesaplanamadı!");

        // 1. satır, 1. sütundaki ortalama yaş değerini alıyoruz
        double ortalamaYas = Double.parseDouble(DBUtility.getCellValue(1, 1).toString());

        Assert.assertTrue(ortalamaYas > 0, "Hesaplanan yaş sıfırdan büyük olmalıdır!");
        System.out.println("Çalışanların Ortalama Yaşı: " + ortalamaYas);
    }

    // 31. Her departmandaki çalışan sayısını bul.
    @Test
    public void Question31() {
        String sql = "SELECT d.dept_name, COUNT(de.emp_no) AS CalisanSayisi " +
                "FROM departments d " +
                "JOIN dept_emp de ON d.dept_no = de.dept_no " +
                "GROUP BY d.dept_name;";

        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "Departman çalışan sayıları listelenemedi!");

        for (int i = 1; i <= satirSayisi; i++) {
            String departmanAdi = DBUtility.getCellValue(i, 1).toString(); // dept_name
            String calisanSayisi = DBUtility.getCellValue(i, 2).toString(); // CalisanSayisi

            System.out.println("Departman: " + departmanAdi + " | Çalışan Sayısı: " + calisanSayisi);
        }
    }

    // 32. 110022 numaralı çalışanın yöneticilik geçmişini listele.
    @Test
    public void Question32() {
        String sql = "SELECT dm.emp_no, d.dept_name, dm.from_date, dm.to_date " +
                "FROM dept_manager dm " +
                "JOIN departments d ON dm.dept_no = d.dept_no " +
                "WHERE dm.emp_no = 110022 " +
                "ORDER BY dm.from_date DESC;";

        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "110022 numaralı çalışanın yöneticilik geçmişi bulunamadı!");

        for (int i = 1; i <= satirSayisi; i++) {
            String empNo = DBUtility.getCellValue(i, 1).toString();      // emp_no
            String departman = DBUtility.getCellValue(i, 2).toString();  // dept_name
            String baslangic = DBUtility.getCellValue(i, 3).toString();  // from_date
            String bitis = DBUtility.getCellValue(i, 4).toString();      // to_date

            System.out.println("Yönetici ID: " + empNo + " | Departman: " + departman +
                    " | Görev Başlangıcı: " + baslangic + " | Bitiş: " + bitis);
        }
    }

    // 33. Her çalışanın toplam çalışma süresini hesapla.
    @Test
    public void Question33() {
        String sql = "SELECT e.emp_no, e.first_name, e.last_name, e.hire_date, " +
                "DATEDIFF(CASE WHEN de.to_date IS NULL OR de.to_date > '2026-07-16' THEN '2026-07-16' ELSE de.to_date END, e.hire_date) AS ToplamCalismaGunu " +
                "FROM employees e " +
                "JOIN dept_emp de ON e.emp_no = de.emp_no;";

        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "Çalışanların çalışma süreleri hesaplanamadı!");

        for (int i = 1; i <= satirSayisi; i++) {
            String empNo = DBUtility.getCellValue(i, 1).toString();         // emp_no
            String ad = DBUtility.getCellValue(i, 2).toString();            // first_name
            String soyad = DBUtility.getCellValue(i, 3).toString();         // last_name
            String iseGiris = DBUtility.getCellValue(i, 4).toString();      // hire_date
            String toplamGun = DBUtility.getCellValue(i, 5).toString();     // ToplamCalismaGunu

            System.out.println("ID: " + empNo + " | Çalışan: " + ad + " " + soyad +
                    " | İşe Giriş: " + iseGiris + " | Toplam Çalışma Süresi: " + toplamGun + " gün");
        }
    }

    // 34. Her çalışanın en güncel unvanını listele.
    @Test
    public void Question34() {
        String sql = "SELECT e.emp_no, e.first_name, e.last_name, t.title AS EnGuncelUnvan " +
                "FROM employees e " +
                "JOIN titles t ON e.emp_no = t.emp_no " +
                "WHERE t.to_date = (SELECT MAX(sub_t.to_date) FROM titles sub_t WHERE sub_t.emp_no = e.emp_no);";

        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "Çalışanların en güncel unvanları listelenemedi!");

        for (int i = 1; i <= satirSayisi; i++) {
            String empNo = DBUtility.getCellValue(i, 1).toString();         // emp_no
            String ad = DBUtility.getCellValue(i, 2).toString();            // first_name
            String soyad = DBUtility.getCellValue(i, 3).toString();         // last_name
            String unvan = DBUtility.getCellValue(i, 4).toString();         // EnGuncelUnvan

            System.out.println("ID: " + empNo + " | Çalışan: " + ad + " " + soyad + " | Güncel Unvan: " + unvan);
        }
    }

    // 35. D005 departmanındaki yöneticilerin ad ve soyadlarını listele.
    @Test
    public void Question35() {
        String sql = "SELECT e.first_name, e.last_name, d.dept_name " +
                "FROM dept_manager dm " +
                "JOIN employees e ON dm.emp_no = e.emp_no " +
                "JOIN departments d ON dm.dept_no = d.dept_no " +
                "WHERE dm.dept_no = 'd005';";

        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "D005 departmanında yönetici bulunamadı!");

        for (int i = 1; i <= satirSayisi; i++) {
            String ad = DBUtility.getCellValue(i, 1).toString();         // first_name
            String soyad = DBUtility.getCellValue(i, 2).toString();      // last_name
            String departman = DBUtility.getCellValue(i, 3).toString();  // dept_name

            System.out.println("Yönetici: " + ad + " " + soyad + " | Departman: " + departman);
        }
    }

    // 36. Çalışanları doğum tarihine göre sırala.
    @Test
    public void Question36() {
        String sql = "SELECT emp_no, first_name, last_name, birth_date FROM employees ORDER BY birth_date ASC;";

        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "Çalışan listesi boş döndü!");

        for (int i = 1; i <= satirSayisi; i++) {
            String empNo = DBUtility.getCellValue(i, 1).toString();       // emp_no
            String ad = DBUtility.getCellValue(i, 2).toString();          // first_name
            String soyad = DBUtility.getCellValue(i, 3).toString();       // last_name
            String dogumTarihi = DBUtility.getCellValue(i, 4).toString();  // birth_date

            System.out.println("ID: " + empNo + " | Çalışan: " + ad + " " + soyad + " | Doğum Tarihi: " + dogumTarihi);
        }
    }

    // 37. Nisan 1992’de işe alınan çalışanları listele.
    @Test
    public void Question37() {
        String sql = "SELECT emp_no, first_name, last_name, hire_date " +
                "FROM employees " +
                "WHERE hire_date BETWEEN '1992-04-01' AND '1992-04-30' " +
                "ORDER BY hire_date ASC;";

        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "Nisan 1992'de işe alınan çalışan bulunamadı!");

        for (int i = 1; i <= satirSayisi; i++) {
            String empNo = DBUtility.getCellValue(i, 1).toString();       // emp_no
            String ad = DBUtility.getCellValue(i, 2).toString();          // first_name
            String soyad = DBUtility.getCellValue(i, 3).toString();       // last_name
            String iseGiris = DBUtility.getCellValue(i, 4).toString();    // hire_date

            System.out.println("ID: " + empNo + " | Çalışan: " + ad + " " + soyad + " | İşe Giriş: " + iseGiris);
        }
    }

    // 38. 10102 numaralı çalışanın çalıştığı tüm departmanları listele.
    @Test
    public void Question38() {
        String sql = "SELECT de.emp_no, d.dept_name, de.from_date, de.to_date " +
                "FROM dept_emp de " +
                "JOIN departments d ON de.dept_no = d.dept_no " +
                "WHERE de.emp_no = 10102 " +
                "ORDER BY de.from_date DESC;";

        DBUtility.executeQuery(sql);

        int satirSayisi = DBUtility.getRowCount();
        Assert.assertTrue(satirSayisi > 0, "10102 numaralı çalışanın departman geçmişi bulunamadı!");

        for (int i = 1; i <= satirSayisi; i++) {
            String empNo = DBUtility.getCellValue(i, 1).toString();      // emp_no
            String departman = DBUtility.getCellValue(i, 2).toString();  // dept_name
            String baslangic = DBUtility.getCellValue(i, 3).toString();  // from_date
            String bitis = DBUtility.getCellValue(i, 4).toString();      // to_date

            System.out.println("ID: " + empNo + " | Departman: " + departman +
                    " | Giriş: " + baslangic + " | Çıkış: " + bitis);
        }
    }

    @AfterClass
    public void tearDown() {
        DBUtility.JDBC_Close();
    }

}