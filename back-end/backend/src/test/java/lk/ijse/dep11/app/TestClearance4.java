package lk.ijse.dep11.app;

import java.sql.*;

/**
 * A class for testing data clearance by deleting records with duplicate first and last names from the 'customer' table.
 */
public class TestClearance4 {

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
            // Create a statement for executing SQL queries to delete records with duplicate first and last names
            Statement stm = connection.createStatement();

            // Execute a SQL query to delete records with duplicate first and last names
            int deletedRowCount = stm.executeUpdate("DELETE FROM customer WHERE id IN (SELECT id FROM (SELECT DISTINCT c1.id FROM customer c1 INNER JOIN customer c2 ON c1.id <> c2.id AND c1.first_name = c2.first_name AND c1.last_name = c2.last_name WHERE c1.id > c2.id) AS C)");

            // Print the number of deleted rows
            System.out.println("Deleted Row Count: " + deletedRowCount);
        }
    }
}
