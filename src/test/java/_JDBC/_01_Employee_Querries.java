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
        String sql = "SELECT e.emp_no, e.first_name, e.last_name, de.dept_no FROM employees AS e JOIN dept_emp AS de ON e.emp_no=de.emp_no WHERE de.dept_no='d001';";
        System.out.println("sql = " + sql);
        ResultSet sonuc = sorguEkrani.executeQuery(sql);

        // Assert.assertTrue(DBUtility.getRowCount() < 0);
        //burada result set yazılı olmadıgı için çalışmadı

        while (sonuc.next()) { // result set ilerletiyor, her bir satır yazılıyor
            System.out.println("sonuc = " + sonuc.getString("emp_no"));
        }
        // result setin sonuna geldigi için, DBUtility.getRowCount() calışmadı
    }

    @Test
    // Human Resources departmanındaki tüm çalışanları listeleyiniz.
    public void Question02() {
        String sql = " SELECT e.emp_no, e.first_name, e.last_name FROM employees e JOIN dept_emp de ON e.emp_no=de.emp_no JOIN departments d ON de.dept_no=d.dept_no WHERE d.dept_name='Human Resources'; ";
        DBUtility.executeQuery(sql);
    }

    @Test
    public void Question03() {
        DBUtility.executeQuery(" SELECT AVG(salary) OrtalamaMaas FROM salaries; ");
        // List<HashMap<String, String>> data = DBUtility.getListData();
        // Assert.assertEquals(data.size(), 1);
    }

    @Test
    public void Question04(){
        DBUtility.executeQuery
                (" SELECT AVG(s.salary) FROM salaries s JOIN employees e ON s.emp_no=e.emp_no WHERE e.gender='M'; ");
        // Assert.assertEquals(DBUtility.getRowCount(),1);
    }

    @Test
    public void Question05(){
        DBUtility.executeQuery
                ("SELECT AVG(s.salary) FROM salaries s JOIN employees e ON s.emp_no=e.emp_no WHERE e.gender='F'; ");
        // Assert.assertEquals(DBUtility.getRowCount(),1);
    }

    @Test
    public void Question06(){
        DBUtility.executeQuery
                ("SELECT e.emp_no, e.first_name, e.last_name, s.salary FROM employees e JOIN salaries s ON e.emp_no=s.emp_no JOIN dept_emp de ON e.emp_no=de.emp_no JOIN departments d ON de.dept_no=d.dept_no WHERE d.dept_name='Sales' AND s.salary>70000; ");
        // Assert.assertTrue(DBUtility.getRowCount()>0);
    }

    @Test
    public void Question07(){
        DBUtility.executeQuery
                (" SELECT * FROM salaries WHERE salary BETWEEN 50000 AND 100000; ");
        // Assert.assertTrue(DBUtility.getRowCount()>0);
    }

    @Test
    public void Question08(){
        DBUtility.executeQuery
                (" SELECT de.dept_no, AVG(s.salary) FROM dept_emp de JOIN salaries s ON de.emp_no=s.emp_no GROUP BY de.dept_no; ");
        // Assert.assertTrue(DBUtility.getRowCount()>0);
    }

    @Test
    public void Question09(){
        DBUtility.executeQuery
                (" SELECT d.dept_name, AVG(s.salary) FROM departments d JOIN dept_emp de ON d.dept_no=de.dept_no JOIN salaries s ON de.emp_no=s.emp_no GROUP BY d.dept_name; ");
        // Assert.assertTrue(DBUtility.getRowCount()>0);
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



    @AfterClass
    public void tearDown() {
        DBUtility.JDBC_Close();
    }

}