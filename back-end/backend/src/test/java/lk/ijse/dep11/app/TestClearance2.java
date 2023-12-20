package lk.ijse.dep11.app;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * A class for testing data clearance by deleting records from the 'customer' table.
 */
public class TestClearance2 {

    /**
     * The main method of the program.
     *
     * @param args Command-line arguments (not used in this example).
     * @throws Exception If an exception occurs during execution.
     */
    public static void main(String[] args) throws Exception {
        // Load the MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish a connection to the MySQL database
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep11_filtering_app", "root", "mysql")) {
            // Create a statement for executing SQL queries
            Statement stm = connection.createStatement();

            // Execute a SQL DELETE statement to remove records that do not match the contact number pattern
            int deletedRows = stm.executeUpdate("DELETE FROM customer WHERE contact NOT REGEXP '^\\\\d{3}-\\\\d{7}$'");

            // Print the number of deleted rows
            System.out.println("Deleted Row Count: " + deletedRows);
        }
    }
}
