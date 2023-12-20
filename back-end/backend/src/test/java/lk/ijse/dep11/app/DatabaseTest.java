package lk.ijse.dep11.app;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for database-related functionality.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseTest {

    private Connection connection;

    /**
     * Set up method executed before each test.
     * Establishes a connection to the database.
     *
     * @throws Exception if an error occurs during setup.
     */
    @BeforeEach
    void setUp() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep11_filtering_app", "root", "mysql");
    }

    /**
     * Tear down method executed after each test.
     * Closes the database connection.
     *
     * @throws Exception if an error occurs during teardown.
     */
    @AfterEach
    void tearDown() throws Exception {
        connection.close();
    }

    /**
     * Test to verify that the database contains at least 1000 records in the "customer" table.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Order(1)
    @Test
    void testDBExceeds1000Records() throws Exception {
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT COUNT(*) FROM customer");
        rst.next();
        int numberOfRecords = rst.getInt(1);
        System.out.println(numberOfRecords);
        assertTrue(numberOfRecords >= 1000);
    }

    /**
     * Test to verify that contact numbers in the "customer" table follow a valid pattern.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Order(2)
    @Test
    void testValidContactNumbers() throws Exception {
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM customer WHERE contact NOT REGEXP '^\\\\d{3}-\\\\d{7}$'");
        assertFalse(rst.next());
    }

    /**
     * Test to verify that contact numbers in the "customer" table are unique.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Order(3)
    @Test
    void testUniqueContactNumbers() throws Exception {
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT contact, COUNT(contact) AS count FROM customer GROUP BY contact HAVING count > 1");
        assertFalse(rst.next());
    }

    /**
     * Test to verify that customer names in the "customer" table are unique.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Order(4)
    @Test
    void testUniqueCustomerNames() throws Exception {
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT CONCAT(first_name,' ', last_name) AS name, COUNT(*) AS count FROM customer GROUP BY first_name, last_name HAVING count > 1");
        assertFalse(rst.next());
    }
}
